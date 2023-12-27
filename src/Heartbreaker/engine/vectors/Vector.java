package Heartbreaker.engine.vectors;

import java.awt.geom.Point2D;

/**
 * An implementation of 2D Vectors
 *
 * @author tuckt
 */
public class Vector {

    /** X component */
    public final double x;

    /** Y component */
    public final double y;

    /** Vector with x = 0 and y = 0 */
    public final static Vector ZERO = new Vector(0,0);

    /**
     * Constructor using doubles.
     * @param x x value
     * @param y y value
     */
    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor using a Point2D.
     * @param p2d Point2D
     */
    public Vector(Point2D p2d){
        this.x = p2d.getX();
        this.y = p2d.getY();
    }

    /**
     * Sum of Vector a and Vector b.
     *
     * @param a Vector a
     * @param b Vector b
     *
     * @return Sum of both Vectors.
     */
    public static Vector sum(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y);
    }

    /**
     * Subtracts Vector b from Vector a.
     *
     * @param a Vector a
     * @param b Vector b
     *
     * @return The difference of both Vectors.
     */
    public static Vector difference(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y);
    }

    /**
     * Multiplies both components of Vector a by a scalar value.
     *
     * @param a Vector a
     * @param scalar Scalar value
     *
     * @return A vector with the x and y components multiplied by scalar.
     */
    public static Vector multiply(Vector a, double scalar) {
        return new Vector(a.x * scalar, a.y * scalar);
    }

    /**
     * Product of Vector a and Vector b.
     *
     * @param a Vector a
     * @param b Vector b
     *
     * @return The product of Vector a and Vector b.
     */
    public static Vector multiply(Vector a, Vector b) {
        return new Vector(a.x * b.x, a.y * b.y);
    }

    /**
     * Turns both components of Vector a into negative values.
     *
     * @param a Vector a
     *
     * @return Vector a with -x and -y.
     */
    public static Vector negate(Vector a) {
        return new Vector(-a.x, -a.y);
    }

    /**
     * Divides both components of Vector a by a scalar value.
     *
     * @param a Vector a
     * @param scalar Scalar
     *
     * @return A Vector which both x and y have been divided by scalar.
     */
    public static Vector divide(Vector a, double scalar) {
        return new Vector(a.x / scalar, a.y / scalar);
    }

    /** Divides Vector a by Vector b.
     *
     * @param a Vector a
     * @param b Vector b
     *
     * @return Vector A divided Vector b.
     */
    public static Vector divide(Vector a, Vector b) {
        return new Vector(a.x /b.x, a.y / b.y);
    }

    /**
     * Returns whether the components of this vector is equal to Vector b.
     * @param b Vector b
     * @return true if Vector.x == VectorB.x and Vector.y == VectorB.y.
     */
    public boolean equals(Vector b) {
        return this.x == b.x && this.y == b.y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

}
