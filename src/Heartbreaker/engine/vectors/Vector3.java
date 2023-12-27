package Heartbreaker.engine.vectors;

/**
 * An implementation of 3D Vectors
 *
 * @author tuckt
 */
public class Vector3 {

    /** X component */
    public final double x;

    /** Y component */
    public final double y;

    /** Z component */
    public final double z;

    /** Vector where x = 0, y = 0, and z = 0 */
    public final static Vector3 Zero = new Vector3(0,0,0);

    /**
     * Constructor using 3 doubles.
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Sum of two Vectors.
     * @param a Vector a
     * @param b Vector b
     * @return Sum of Vector a and Vector b.
     */
    public static Vector3 sum(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * Difference of two Vectors.
     * @param a Vector a
     * @param b Vector b
     * @return The difference of Vector a and Vector b.
     */
    public static Vector3 difference(Vector3 a, Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    /**
     * The product of Vector a and a scalar value.
     * @param a Vector a
     * @param scalar scalar value
     * @return Both components of Vector a multiplied by scalar.
     */
    public static Vector3 multiply(Vector3 a,double scalar) {
        return new Vector3(a.x * scalar, a.y * scalar, a.z * scalar);
    }

    /**
     * The product of Vector a and Vector B.
     * @param a Vector a
     * @param b Vector b
     * @return The product of Vector a and Vector b.
     */
    public static Vector3 multiply(Vector3 a,Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    /**
     * Turns the components of Vector a into negative values.
     * @param a Vector a
     * @return A Vector3 with -x, -y, and -z
     */
    public static Vector3 negate(Vector3 a) {
        return new Vector3(-a.x, -a.y, -a.z);
    }

    /**
     * Divides the components of Vector a by a scalar value.
     *
     * @param a Vector a
     * @param scalar Scalar value
     * @return A Vector3 where the components have been divided by scalar.
     */
    public static Vector3 divide(Vector3 a,double scalar) {
        return new Vector3(a.x / scalar, a.y / scalar, a.z / scalar);
    }

    /**
     * Divides Vector a by Vector b
     *
     * @param a Vector a
     * @param b Vector b
     * @return Returns a Vector3 where the components are the quotient of the components of Vector a divided by those of Vector b.
     */
    public static Vector3 divide(Vector3 a, Vector3 b) {
        return new Vector3(a.x /b.x, a.y / b.y, a.z / b.z);
    }

    /**
     * Returns whether the components of this vector is equal to Vector b.
     * @param b Vector b
     * @return true if Vector.x == VectorB.x, Vector.y == VectorB.y, and Vector.z == VectorB.z.
     */
    public boolean equals(Vector3 b) {
        return this.x == b.x && this.y == b.y && this.z == b.z;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }

}
