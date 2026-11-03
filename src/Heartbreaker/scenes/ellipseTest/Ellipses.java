package Heartbreaker.scenes.ellipseTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Ellipses {

    static double increment = (Math.PI) / 500_000;
    static double sMajorLen = 8;
    static double minorLength = 0;
    static double c;
    static double centerX = 0;
    static double centerY = 0;  
    static double x1 = -5;
    static double y1 = 0;
    static double x2 = 5;
    static double y2 = 0;
    static double phi = 0;
    public static java.util.function.Consumer<double[]> traceHook = null; // TEST SCENE HOOK
    public static void main(String[] args){

        Scanner in = new Scanner(System.in);
        double inX1 = in.nextDouble();
        double inY1 = in.nextDouble();
        double inX2 = in.nextDouble();
        double inY2 = in.nextDouble();
        double inMajorLength = in.nextDouble();

        if(inX1 > inX2){
            x1 = inX2;
            y1 = inY2;
            x2 = inX1;
            y2 = inY1;
        } else {
            x1 = inX1;
            y1 = inY1;
            x2 = inX2;
            y2 = inY2;
        }
    
        sMajorLen = inMajorLength / 2.0;





        c = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) / 2.0;
        minorLength = Math.sqrt(Math.pow(sMajorLen, 2) - Math.pow(c, 2));
        centerX = (x1 + x2) / 2.0;
        centerY = (y1 + y2) / 2.0;

        phi = Math.atan2(y2 - y1, x2 - x1);


        double xResult = ternarySearch(-2*Math.PI, 2*Math.PI,(int) 0);
        double yResult = ternarySearch(-2*Math.PI, 2*Math.PI,(int) 1);

        double minX = 2 * centerX - xResult;
        double minY = 2 * centerY - yResult;
        System.out.println(minX + " " + minY + " " + xResult + " " + yResult);



    }

    private static double[] getPoint(double theta){
        double x = sMajorLen * Math.cos(theta);
        double y = minorLength * Math.sin(theta);
        return new double[]{x,y};
    }

    public static double[] finalPoint(double theta){
        double[] point = getPoint(theta);
        double x = centerX + (point[0] * Math.cos(phi)) - (point[1] * Math.sin(phi));
        double y = centerY + (point[0] * Math.sin(phi)) + (point[1] * Math.cos(phi));
        return new double[]{x,y};
    }

    public static double ternarySearch(double start,double end, int axis){

        double max = Double.NEGATIVE_INFINITY;


        double third = (end - start) / 3.0;
        double midLeft = start + third;
        double midRight = end - third;
        if(traceHook != null) traceHook.accept(new double[]{axis, start, end, midLeft, midRight}); // TEST SCENE HOOK

        if(Math.abs(start - end ) < .0001){
            double center = (start + end) / 2.0;
            return finalPoint(center)[axis];
            /*
            for(double i = start; i <= end; i+= increment){
                double[] point = getPoint(i);
                if(point[axis] > max){
                    max = point[axis];
                    if(finalPoint(i+increment)[axis] < finalPoint(i)[axis]){
                        return finalPoint(i)[axis];
                    }
                }
            }
            return max;

             */
        }


        //Looking at the slope to check which direction to look. Possibly unoptimal 
        int mlDir = (int) Math.signum(finalPoint(midLeft + increment)[axis] - finalPoint(midLeft - increment)[axis]);
        int mrDir = (int) Math.signum(finalPoint(midRight + increment)[axis] - finalPoint(midRight - increment)[axis]);

        if(mrDir > 0){
            return ternarySearch(midRight, end, axis);
        } else if(mlDir < 0){
            return ternarySearch(start,midLeft, axis);
        } else {
            return ternarySearch(midLeft, midRight, axis);
        }
    }

}
