package drawer;

import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

public class Drawer {
    //2d section
    private int x_prev = -1;
    private int y_prev = -1;
    private static double fontRatio = Math.round((float) 18 / 8);

    //fade
    Queue<List<Double>> toErase = new LinkedList<>();

    //3d section▓█
    private static char[] gradient = "`.-':_,^=;><+!rc*/z?sLTv)J7(|Fi{C}fI31tlu[neoZ5Yxjya]2ESwqkP6h9d4VpOGbUAKXHm8RD#$Bg0MNWQ%&@".toCharArray();
    private static float[][]  depthMap = new float[50][200];
    public static ArrayList<Double[]> rawFrame = new ArrayList<>();


    public boolean draw(double x, double y, char c) {
        int x_int = (int) Math.round(x * fontRatio);
        int y_int = (int) Math.round(51 - y);


        if ((x_prev == x_int && y_prev == y_int)) {
            return false;
        }
        if ((x_int > 210 || y_int > 52 || x_int < 0 || y_int < 0)) {
            return false;
        }


        System.out.printf(String.format("\033[%d;%dH", y_int, x_int));
        System.out.print(c);
        this.x_prev = x_int;
        this.y_prev = y_int;

        return true;
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    public boolean draw_erase(double x, double y, int distance, char c) {
        if (toErase == null) {
            toErase = new LinkedList<>();
        }

        if (draw(x, y, c)) {

            toErase.add(Arrays.asList(x, y));
            if (toErase.size() > distance) {
                var temp = toErase.remove();
                return draw(temp.getFirst(), temp.getLast(), ' ');
            }
            return true;
        }
        return false;

    }
    public static int getGradientLenght(){
        return gradient.length;
    }


    public void draw(double x, double y, double z, double minValue, double maxValue) {

        z = normalize(z, minValue, maxValue, 0, gradient.length - 1);
        draw(x, y, gradient[(int) (z)]);
    }

    public static void makePerspective(double distance, Double[] pivot){
        for(Double[] point : rawFrame){
            point[0] = (point[0]*distance)/(point[2] + distance) + pivot[0];
            point[1] = (point[1]*distance)/(point[2] + distance) + pivot[1];
        }

    }

    public static void prepareFrame() {

        if (depthMap == null) {
            depthMap = new float[50][200];
        }

        for(float[] floats: depthMap) {
            Arrays.fill(floats, (float) (-1./0));
        }



        for (Double[] doubles : rawFrame) {
            int x_int = (int) Math.round(doubles[0] * fontRatio);
            int y_int = (int) Math.round(49 - doubles[1]);
            if ((x_int >= depthMap[0].length || y_int >= depthMap.length || x_int < 0 || y_int < 0)) {
                continue;
            }
            depthMap[y_int][x_int] = (float) Math.max(doubles[2], depthMap[y_int][x_int]);
//            System.out.printf("%d\t%d\t%f\n", x_int, y_int, doubles[2]);
        }

    }

    public static void sendFrame() {
        float maxValue=-Float.MIN_VALUE, minValue=Float.MAX_VALUE;

        for (float[] floats : depthMap) {
            for (float val : floats) {
                if (val+500 > -1./0) {
                    maxValue = Math.max(maxValue, val);
                    minValue = Math.min(minValue, val);
                }
            }
        }
        char aChar;
        StringBuilder stringBuilder = new StringBuilder();
        for (float[] floats : depthMap) {
            for (float val : floats) {
                if (val +500 > -1./0) {
                    aChar = gradient[normalize(val, minValue, maxValue, 0, gradient.length - 1)];
                }else{
                    aChar = ' ';
                }
                stringBuilder.append(aChar);
            }
            stringBuilder.append("\n");
        }

        System.out.print("\033[H");
        System.out.print(stringBuilder);

        rawFrame.clear();
//        depthMap = null;
    }

    private static int normalize(double value, double minA, double maxA, int minB, int maxB) {
        return (int) Math.round(((value - minA) * (maxB - minB)) / (maxA - minA)) + minB;
    }

    public void drawLine(double x1, double y1, double x2, double y2, char c) {
        int x1_int = (int) Math.round(x1 * fontRatio);
        int y1_int = (int) Math.round(y1);
        int x2_int = (int) Math.round(x2 * fontRatio);
        int y2_int = (int) Math.round(y2);

        int dx = Math.abs(x2_int - x1_int);
        int dy = Math.abs(y2_int - y1_int);

        int sx = x1_int < x2_int ? 1 : -1;
        int sy = y1_int < y2_int ? 1 : -1;

        int err = dx - dy;

        while (true) {
            draw(x1_int / fontRatio, y1_int, c); // Draw the current point

            if (x1_int == x2_int && y1_int == y2_int) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1_int += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1_int += sy;
            }
        }
    }

    public void fade(int ms) {
        while (!toErase.isEmpty()) {
            var x = toErase.remove();
            draw(x.getFirst(), x.getLast(), ' ');
            try {
                Thread.sleep(ms);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void switchColor(String color) {
        switch (color) {
            case "red":
                System.out.print("\033[0;31m");
                break;
            case "green":
                System.out.print("\033[0m");
                break;
            case "blue":
                System.out.print("\033[0;34m");
                break;
            case "yellow":
                System.out.print("\033[0;33m");
                break;
            case "white":
                System.out.print("\033[1;37m");
                break;
            case "clear":
                System.out.print("\033[0m");
            default:
                System.out.printf("\033[38;5;%sm", color);
                break;
        }
    }

    public static void switchColor(Integer color) {
        System.out.printf("\033[38;5;%dm", color);
    }

}
