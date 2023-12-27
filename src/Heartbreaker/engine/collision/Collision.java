package Heartbreaker.engine.collision;
import Heartbreaker.engine.vectors.*;


import java.awt.geom.*;

/*
References:
    https://www.youtube.com/watch?v=emfGoBgE020&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=4
    https://www.youtube.com/watch?v=Zgf1DYrmSnk&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=6
    https://www.youtube.com/watch?v=vWs33LVrs74&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=8
 */


/**
 * Collision is the class that {@link CollisionManager} uses to determine if two colliders have collided
 *
 * @author tuckt
 */

public class Collision {

    // Collision Types

    /** Circle colliding with Circle */
    static final int CIRCLE_CIRCLE = 0;

    /** Polygon colliding with Polygon */
    static final int POLY_POLY = 1;

    /** Circle colliding with Polygon */
    static final int CIRCLE_POLY = 2;


    /**
     * Determines if two circle type Colliders are colliding.
     *
     * @param centerA Center of Collider A's hitbox
     * @param radiusA Radius of Collider A's hitbox
     * @param centerB Center of Collider B's hitbox
     * @param radiusB Radius of COllider B's hitbox
     * @return CollisionData with type Circle_Circle if colliding, otherwise null
     */
    public static CollisionData circleCircle(Point2D centerA, double radiusA, Point2D centerB, double radiusB){

        Vector normal = Vector.ZERO;
        double depth = 0;

        double x = centerB.getX() - centerA.getX();
        double y = centerB.getY() - centerA.getY();
        double distance = Math.sqrt(x * x + y * y);
        double radii = radiusA + radiusB;

        if(!(distance < radii)){
            return null;
        }
        normal = VectorMath.normalize(Vector.difference(new Vector(centerB),new Vector(centerA)));
        depth = radii + distance;
        return new CollisionData(depth,normal,CIRCLE_CIRCLE);
    }

    /**
     * Determines if a circle Collider and Polygon collider are colliding using Separating Axis Theorem.
     *
     * @param circleCenter Center of circle Collider
     * @param radius Radius of circle Collider
     * @param verts Verticies of Polygon collider
     * @return CollisionData with type Circle_Poly if colliding, otherwise null
     */
    public static CollisionData circlePolygon(Point2D circleCenter, double radius, Point2D[] verts){

        Vector normal = Vector.ZERO;
        double depth = Float.MAX_VALUE;

        //Polygon
        for(int i = 0; i < verts.length; i++){
            //Get projection lines
            Vector vA = new Vector(verts[i]);
            Vector vB = new Vector(verts[(i+1) % verts.length]);

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
                return null;
            }

            double axisDepth = Math.min(maxB - minA, maxA - minB);

            if(axisDepth < depth){
                depth = axisDepth;
                normal = axis;
            }
        }

        int cpi = findClosetVert(circleCenter,verts);
        Vector closetsPoint = new Vector(verts[cpi]);

        Vector axis = Vector.difference(closetsPoint,new Vector(circleCenter));

        //Get the mins and maxes
        double[] minMaxA = projectVerts(verts, axis);
        double minA = minMaxA[0];
        double maxA = minMaxA[1];

        double[] minMaxB = projectCircle(circleCenter,radius, axis);
        double minB = minMaxB[0];
        double maxB = minMaxB[1];

        if(minA >= maxB || minB >= maxA){
            return null;
        }

        double axisDepth = Math.min(maxB - minA, maxA - minB);

        if(axisDepth < depth){
            depth = axisDepth;
            normal = axis;
        }
        depth /= VectorMath.length(normal);
        normal = VectorMath.normalize(normal);
        Vector polyCenter = findArithmeticMean(verts);
        Vector direction = Vector.difference(polyCenter,new Vector(circleCenter));

        if(VectorMath.dot(direction,normal) < 0.0){
            normal = Vector.multiply(normal,-1);
        }
        return new CollisionData(depth,normal,CIRCLE_POLY);
    }

    /**
     * Finds the vertex closes to the center of the circle
     * @param center circle center
     * @param verts verticies of a polygon
     * @return index of vertex
     */
    private static int findClosetVert(Point2D center, Point2D[] verts){
        int result = -1;
        double minDistance = Double.MAX_VALUE;

        for(int i = 0; i < verts.length; i++){
            Vector v = new Vector(verts[i].getX(),verts[i].getY());
            double distance = VectorMath.distance(v,new Vector(center));

            if(distance < minDistance){
                minDistance = distance;
                result = i;
            }
        }
        return result;
    }

    /**
     * Projects a circle onto an axis for Separating Axis Theorem.
     *
     * @param center center of circle
     * @param radius radius of circle
     * @param axis axis to project too
     * @return The minimum point of the cirlce and the maximum point of the circle on the projection
     */
    private static double[] projectCircle(Point2D center, double radius, Vector axis){
        Vector direction = VectorMath.normalize(axis);
        Vector directionAndRadius = Vector.multiply(direction,radius);

        Vector p1 = Vector.sum(new Vector(center), directionAndRadius);
        Vector p2 = Vector.difference(new Vector(center), directionAndRadius);

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

    /**
     * Determines if two polygon Colliders are colliding using Separating Axis Theorem.
     *
     * @param vertsA Vertices of Polygon A
     * @param vertsB Vertices of Polygon B
     * @return CollisionData with type Poly_Poly if colliding, otherwise null
     */
    public static CollisionData polygonPolygon(Point2D[] vertsA, Point2D[] vertsB){

        Vector normal = Vector.ZERO;
        double depth = Float.MAX_VALUE;

        //Polygon A
        for(int i = 0; i < vertsA.length; i++){
            //Get projection lines
            Vector vA = new Vector(vertsA[i]);
            Vector vB = new Vector(vertsA[(i+1) % vertsA.length]);

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
                return null;
            }

            double axisDepth = Math.min(maxB - minA, maxA - minB);
            if(axisDepth < depth){
                depth = axisDepth;
                normal = axis;
            }
        }

        //Polygon B
        for(int i = 0; i < vertsB.length; i++){
            //Get projection lines
            Vector vA = new Vector(vertsB[i]);
            Vector vB = new Vector(vertsB[(i+1) % vertsB.length]);

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
                return null;
            }

            double axisDepth = Math.min(maxB - minA, maxA - minB);
            if(axisDepth < depth){
                depth = axisDepth;
                normal = axis;
            }
        }

        depth /= VectorMath.length(normal);
        normal = VectorMath.normalize(normal);
        Vector centerA = findArithmeticMean(vertsA);
        Vector centerB = findArithmeticMean(vertsB);
        Vector direction = Vector.difference(centerB,centerA);

        if(VectorMath.dot(direction,normal) < 0.0){
            normal = Vector.multiply(normal,-1);
        }


        return new CollisionData(depth,normal,POLY_POLY);
    }

    /**
     * Determines the center of a polygon by averaging the x and y components of its vertices separately.
     * @param verts The Vertices of a polygon
     * @return A vector representing the center of a polygon
     */
    private static Vector findArithmeticMean(Point2D[] verts){
        double sumX = 0;
        double sumY = 0;

        for(int i = 0; i < verts.length; i++){
            Vector v = new Vector(verts[i]);
            sumX += v.x;
            sumY += v.y;
        }

        return new Vector(sumX / (double)verts.length, sumY / (double)verts.length);
    }

    /**
     * Projects the vertices of a polygon onto an axis and determines the minimum point and the maximum point.
     * @param verts Vertices of a polygon.
     * @param axis Axis to project onto.
     * @return minimum value at index 0 and maximum value at index 1.
     */
    private static double[] projectVerts(Point2D[] verts, Vector axis){
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;


        for (Point2D vert : verts) {
            //Projects the verts to the axis
            Vector v = new Vector(vert);
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

