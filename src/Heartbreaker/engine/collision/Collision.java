package Heartbreaker.engine.collision;
import Heartbreaker.engine.vectors.*;


import java.awt.geom.*;

/*
Resources Used:
    https://www.youtube.com/watch?v=emfGoBgE020&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=4
    https://www.youtube.com/watch?v=Zgf1DYrmSnk&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=6
    https://www.youtube.com/watch?v=vWs33LVrs74&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=8
 */


public class Collision {

    public static boolean circleCircle(Point2D centerA, double radiusA, Point2D centerB, double radiusB){
        double x = centerB.getX() - centerA.getX();
        double y = centerB.getY() - centerA.getY();
        double distance = Math.sqrt(x * x + y * y);
        double radii = radiusA + radiusB;

        return distance < radii;
    }

    public static boolean circlePolygon(Point2D circleCenter, double radius, Point2D[] verts){

        //Polygon
        for(int i = 0; i < verts.length; i++){
            //Get projection lines
            Vector vA = new Vector(verts[i].getX(),verts[i].getY());
            Vector vB = new Vector(verts[ (i+1) % verts.length ].getX(),verts[ (i+1) % verts.length ].getY());

            //Gets the edge of the two points and gets its axis
            Vector edge = Vector.difference(vB,vA);
            Vector axis = new Vector(-edge.y, edge.x);

            //Get the mins and maxes
            double[] minMaxA = projectVerts(verts, axis);
            double minA = minMaxA[0];
            double maxA = minMaxA[1];

            double[] minMaxB = projectCircle(circleCenter,radius, axis);
            double minB = minMaxB[0];
            double maxB = minMaxB[1];

            if(minA >= maxB || minB >= maxA){
                return false;
            }
        }

        int cpi = findClosetVert(circleCenter,verts);
        Vector closetsPoint = new Vector(verts[cpi].getX(),verts[cpi].getY());

        Vector axis = Vector.difference(closetsPoint,new Vector(circleCenter.getX(),circleCenter.getY()));

        //Get the mins and maxes
        double[] minMaxA = projectVerts(verts, axis);
        double minA = minMaxA[0];
        double maxA = minMaxA[1];

        double[] minMaxB = projectCircle(circleCenter,radius, axis);
        double minB = minMaxB[0];
        double maxB = minMaxB[1];

        if(minA >= maxB || minB >= maxA){
            return false;
        }

        return true;
    }

    private static int findClosetVert(Point2D center, Point2D[] verts){
        int result = -1;
        double minDistance = Double.MAX_VALUE;

        for(int i = 0; i < verts.length; i++){
            Vector v = new Vector(verts[i].getX(),verts[i].getY());
            double distance = VectorMath.distance(v,new Vector(center.getX(),center.getY()));

            if(distance < minDistance){
                minDistance = distance;
                result = i;
            }
        }
        return result;
    }

    private static double[] projectCircle(Point2D center, double radius, Vector axis){
        Vector direction = VectorMath.normalize(axis);
        Vector directionAndRadius = Vector.multiply(direction,radius);

        Vector p1 = Vector.sum(new Vector(center.getX(), center.getY()), directionAndRadius);
        Vector p2 = Vector.difference(new Vector(center.getX(), center.getY()), directionAndRadius);

        //projection
        double min = VectorMath.dot(p1,axis);
        double max = VectorMath.dot(p2,axis);

        if(min > max){
            double temp = min;
            min = max;
            max = temp;
        }
        return new double[] {min,max};
    }

    public static boolean polygonPolygon(Point2D[] vertsA, Point2D[] vertsB){
        
        //Polygon A
        for(int i = 0; i < vertsA.length; i++){
            //Get projection lines
            Vector vA = new Vector(vertsA[i].getX(),vertsA[i].getY());
            Vector vB = new Vector(vertsA[ (i+1) % vertsA.length ].getX(),vertsA[ (i+1) % vertsA.length ].getY());

            //Gets the edge of the two points and gets its axis
            Vector edge = Vector.difference(vB,vA);
            Vector axis = new Vector(-edge.y, edge.x);

            //Get the mins and maxes
            double[] minMaxA = projectVerts(vertsA, axis);
            double minA = minMaxA[0];
            double maxA = minMaxA[1];

            double[] minMaxB = projectVerts(vertsB, axis);
            double minB = minMaxB[0];
            double maxB = minMaxB[1];

            if(minA >= maxB || minB >= maxA){
                return false;
            }
        }

        //Polygon B
        for(int i = 0; i < vertsB.length; i++){
            //Get projection lines
            Vector vA = new Vector(vertsB[i].getX(),vertsB[i].getY());
            Vector vB = new Vector(vertsB[ (i+1) % vertsB.length ].getX(),vertsB[ (i+1) % vertsB.length ].getY());

            //Gets the edge of the two points and gets its axis
            Vector edge = Vector.difference(vB,vA);
            Vector axis = new Vector(-edge.y, edge.x);

            //Get the mins and maxes
            double[] minMaxA = projectVerts(vertsA, axis);
            double minA = minMaxA[0];
            double maxA = minMaxA[1];

            double[] minMaxB = projectVerts(vertsB, axis);
            double minB = minMaxB[0];
            double maxB = minMaxB[1];

            if(minA >= maxB || minB >= maxA){
                return false;
            }
        }
        return true;
    }

    private static double[] projectVerts(Point2D[] verts, Vector axis){
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;


        for (Point2D vert : verts) {
            //Projects the verts to the axis
            Vector v = new Vector(vert.getX(), vert.getY());
            double projection = VectorMath.dot(v, axis);

            //Sets the min and max based on the projected verts
            if (projection < min) {
                min = projection;
            }
            if (projection > max) {
                max = projection;
            }
        }
        return new double[] {min,max};
    }
}

