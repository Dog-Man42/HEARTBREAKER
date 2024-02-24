package Heartbreaker.engine.vectors;

/**
 * Additional methods for math using 2D Vectors.
 * @author tuckt
 */
public class VectorMath {

    /**
     * Calculates the length of Vector2 a
     * @param a Vector2 a
     * @return The length of Vector2 A
     */
    public static double length(Vector2 a){
        return Math.sqrt(a.x * a.x + a.y * a.y);
    }

    /**
     * Calculates distance the between two Vectors.
     * @param a Vector2 a
     * @param b Vector2 b
     * @return The distance between Vector2 a and Vector2 b.
     */
    public static double distance(Vector2 a, Vector2 b){
        double dx = (b.x - a.x);
        double dy = (b.y - a.y);
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Changes the components of Vector2 a to where the length of the resulting vector is 1.
     * @param a Vector2 a
     * @return A vector with the same direction but with a magnitude of one.
     */
    public static Vector2 normalize(Vector2 a){
        double length = length(a);
        return new Vector2(a.x / length,a.y / length);
    }

    /**
     * Calculates the Dot Product of Vector2 a and Vector2 b
     * @param a Vector2 a
     * @param b Vector2 b
     * @return The Dot Product of Vector2 a and Vector2 b
     */
    public static double dot(Vector2 a, Vector2 b){
        return a.x * b.x + a.y * b.y;
    }

    /**
     * Cross Product of 2 Vector2's. Returns the signed area of a parallelogram formed by Vector2 a and Vector2 b
     * @param a Vector2 A
     * @param b Vector2 B
     * @return Area of parallelogram formed by Vector2 a and Vector2 b
     */
    public static double cross(Vector2 a, Vector2 b){
        return a.x * b.y - a.y * b.x;
    }
}

