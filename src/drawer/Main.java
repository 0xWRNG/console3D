package drawer;

import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        
    }
    public void draw3dModel(String filename, Double scale, double x, double y, double z) {

        ArrayList<Double[]> vertices = ManipulatePoints.readVertex(String.format("models3D/%s", filename ));
        Double[] massPoint = ManipulatePoints.calcMassPoint(vertices);
        ManipulatePoints.moveTo(vertices, new Double[]{x,y,z}, massPoint);
        ManipulatePoints.scale(vertices, scale, massPoint);

        Drawer.clearConsole();
        Drawer.rawFrame = new ArrayList<>(vertices);
        Drawer.prepareFrame();
        Drawer.sendFrame();
    }

}