package Heartbreaker.engine.vectors;

import java.awt.geom.Point2D;

/**
 * An implementation of 2D Vectors
 *
 * @author tuckt
 */
public class Vector2 {

    /** X component */
    public final double x;

    /** Y component */
    public final double y;

    /** Vector2 with x = 0 and y = 0 */
    public final static Vector2 ZERO = new Vector2(0,0);

    /**
     * Constructor using doubles.
     * @param x x value
     * @param y y value
     */
    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor using a Point2D.
     * @param p2d Point2D
     */
    public Vector2(Point2D p2d){
        this.x = p2d.getX();
        this.y = p2d.getY();
    }

    /**
     * Sum of Vector2 a and Vector2 b.
     *
     * @param a Vector2 a
     * @param b Vector2 b
     *
     * @return Sum of both Vectors.
     */
    public static Vector2 sum(Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    /**
     * Subtracts Vector2 b from Vector2 a.
     *
     * @param a Vector2 a
     * @param b Vector2 b
     *
     * @return The difference of both Vectors.
     */
    public static Vector2 difference(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    /**
     * Multiplies both components of Vector2 a by a scalar value.
     *
     * @param a Vector2 a
     * @param scalar Scalar value
     *
     * @return A vector with the x and y components multiplied by scalar.
     */
    public static Vector2 multiply(Vector2 a, double scalar) {
        return new Vector2(a.x * scalar, a.y * scalar);
    }

    /**
     * Product of Vector2 a and Vector2 b.
     *
     * @param a Vector2 a
     * @param b Vector2 b
     *
     * @return The product of Vector2 a and Vector2 b.
     */
    public static Vector2 multiply(Vector2 a, Vector2 b) {
        return new Vector2(a.x * b.x, a.y * b.y);
    }

    /**
     * Turns both components of Vector2 a into negative values.
     *
     * @param a Vector2 a
     *
     * @return Vector2 a with -x and -y.
     */
    public static Vector2 negate(Vector2 a) {
        return new Vector2(-a.x, -a.y);
    }

    /**
     * Divides both components of Vector2 a by a scalar value.
     *
     * @param a Vector2 a
     * @param scalar Scalar
     *
     * @return A Vector2 which both x and y have been divided by scalar.
     */
    public static Vector2 divide(Vector2 a, double scalar) {
        return new Vector2(a.x / scalar, a.y / scalar);
    }

    /** Divides Vector2 a by Vector2 b.
     *
     * @param a Vector2 a
     * @param b Vector2 b
     *
     * @return Vector2 A divided Vector2 b.
     */
    public static Vector2 divide(Vector2 a, Vector2 b) {
        return new Vector2(a.x /b.x, a.y / b.y);
    }

    /**
     * Creates a Point2D.Double using the x and y components of the Vector2
     * @return Point2D from x and y
     */
    public Point2D.Double toPoint(){
        return new Point2D.Double(x,y);
    }

    /**
     * Returns whether the components of this vector is equal to Vector2 b.
     * @param b Vector2 b
     * @return true if Vector2.x == VectorB.x and Vector2.y == VectorB.y.
     */
    public boolean equals(Vector2 b) {
        return this.x == b.x && this.y == b.y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

}
