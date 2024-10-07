package drawer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Object3D {
    public ArrayList<Double[]> vertices;
    public Double[] massPoint;
    public Double scale;
    public Double[] rotatation;
    String filename;

//    public  void rotateAxis(char axis, double angle) {
//        ArrayList<Double[]> rotatedPoints = new ArrayList<>();
//        double x, y, z, x1, y1, z1;
//        for (Double[] point : vertices) {
//            x1 = x = point[0];
//            y1 = y = point[1];
//            z1 = z = point[2];
//            switch (axis) {
//                case 'x':
//                    y1 = y * Math.cos(angle) - z * Math.sin(angle);
//                    z1 = y * Math.sin(angle) + z * Math.cos(angle);
//                    break;
//                case 'y':
//                    x1 = x * Math.cos(angle) + z * Math.sin(angle);
//                    z1 = -x * Math.sin(angle) + z * Math.cos(angle);
//                    break;
//                case 'z':
//                    x1 = x * Math.cos(angle) - y * Math.sin(angle);
//                    y1 = x * Math.sin(angle) + y * Math.cos(angle);
//                    break;
//
//            }
//            rotatedPoints.add(new Double[]{x1, y1, z1});
//        }
//        this.vertices = rotatedPoints;
//    }

    public static void rotatePivot(ArrayList<Double[]> points, Double[] pivot, char axis, double angle) {
        double x, y, z, x1, y1, z1;
        for (Double[] point : points) {
            x1 = x = point[0] - pivot[0];
            y1 = y = point[1] - pivot[1];
            z1 = z = point[2] - pivot[2];
            switch (axis) {
                case 'x':
                    y1 = y * Math.cos(angle) - z * Math.sin(angle);
                    z1 = y * Math.sin(angle) + z * Math.cos(angle);
                    break;

                case 'y':
                    x1 = x * Math.cos(angle) + z * Math.sin(angle);
                    z1 = -x * Math.sin(angle) + z * Math.cos(angle);
                    break;

                case 'z':
                    x1 = x * Math.cos(angle) - y * Math.sin(angle);
                    y1 = x * Math.sin(angle) + y * Math.cos(angle);
                    break;

            }
            point[0] = x1 + pivot[0];
            point[1]= y1 + pivot[1];
            point[2]= z1 + pivot[2];

        }
    }
    public static Double[] calcMassPoint(ArrayList<Double[]> points){
        Double[] centerPoint = new Double[3];
        Arrays.fill(centerPoint, 0.0);
        for (Double[] point : points) {
            centerPoint[0]+= point[0];
            centerPoint[1]+= point[1];
            centerPoint[2]+= point[2];
        }
        centerPoint[0]/= points.size();
        centerPoint[1]/= points.size();
        centerPoint[2]/= points.size();

        return centerPoint;
    }
    public static void scale(ArrayList<Double[]> points, double coeff, Double[] massCenter){

        for(Double[] point: points){

            point[0]=(point[0]-massCenter[0])*coeff+ massCenter[0];
            point[1]=(point[1]-massCenter[1])*coeff+ massCenter[1];
            point[2]=(point[2]-massCenter[2])*coeff+ massCenter[2];

        }
    }
    public static void move(ArrayList<Double[]> points, Double[] vector){
        for (Double[] point : points) {
            point[0] += vector[0];
            point[1] += vector[1];
            point[2] += vector[2];
        }
    }
    public static void moveTo(ArrayList<Double[]> points, Double[] point, Double[] massCenter){
        Double[] movePnt = new Double[]{
                point[0]-massCenter[0],
                point[1]-massCenter[1],
                point[2]-massCenter[2]};

        for (Double[] pnt: points){
            pnt[0] += movePnt[0];
            pnt[1] += movePnt[1];
            pnt[2] += movePnt[2];

        }
        massCenter[0] += movePnt[0];
        massCenter[1] += movePnt[1];
        massCenter[2] += movePnt[2];

    }
    public static ArrayList<Double[]> readVertex(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            ArrayList<Double[]> vertices = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (!line.startsWith("v ")) {
                    continue;
                }
                String[] coords = line.split(" ");
                vertices.add(new Double[]{Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3])});
            }
            return vertices;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return null;
    }

}
