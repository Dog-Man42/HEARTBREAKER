package Heartbreaker.engine.vectors;

/**
 * Additional methods for math using 2D Vectors.
 * @author tuckt
 */
public class VectorMath {

    /**
     * Calculates the length of Vector a
     * @param a Vector a
     * @return The length of Vector A
     */
    public static double length(Vector a){
        return Math.sqrt(a.x * a.x + a.y * a.y);
    }

    /**
     * Calculates distance the between two Vectors.
     * @param a Vector a
     * @param b Vector b
     * @return The distance between Vector a and Vector b.
     */
    public static double distance(Vector a, Vector b){
        double dx = (b.x - a.x);
        double dy = (b.y - a.y);
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Changes the components of Vector a to where the length of the resulting vector is 1.
     * @param a Vector a
     * @return A vector with the same direction but with a magnitude of one.
     */
    public static Vector normalize(Vector a){
        double length = length(a);
        return new Vector(a.x / length,a.y / length);
    }

    /**
     * Calculates the Dot Product of Vector a and Vector b
     * @param a Vector a
     * @param b Vector b
     * @return The Dot Product of Vector a and Vector b
     */
    public static double dot(Vector a, Vector b){
        return a.x * b.x + a.y * b.y;
    }

    /**
     * Cross Product of 2 Vector2's. Returns the signed area of a parallelogram formed by Vector a and Vector b
     * @param a Vector A
     * @param b Vector B
     * @return Area of parallelogram formed by Vector a and Vector b
     */
    public static double cross(Vector a, Vector b){
        return a.x * b.y - a.y * b.x;
    }
}

