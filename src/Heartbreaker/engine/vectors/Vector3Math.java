package Heartbreaker.engine.vectors;

/**
 * Additional methods for math using 3D Vectors.
 * @author tuckt
 */
public class Vector3Math {

    /**
     * Calculates the length of Vector2 a
     * @param a Vector2 a
     * @return The length of Vector2 A
     */
    public static double length(Vector3 a){
        return Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
    }

    /**
     * Calculates distance the between two Vectors.
     * @param a Vector2 a
     * @param b Vector2 b
     * @return The distance between Vector2 a and Vector2 b.
     */
    public static double distance(Vector3 a, Vector3 b){
        double dx = (b.x - a.x);
        double dy = (b.y - a.y);
        double dz = (b.z - a.z);
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Changes the components of Vector2 a to where the length of the resulting vector is 1.
     * @param a Vector2 a
     * @return A vector with the same direction but with a magnitude of one.
     */
    public static Vector3 normalize(Vector3 a){
        double length = length(a);
        return new Vector3(a.x / length,a.y / length, a.z / length);
    }

    /**
     * Calculates the Dot Product of Vector2 a and Vector2 b.
     * @param a Vector2 a
     * @param b Vector2 b
     * @return The dot product of Vector2 a and Vector2 b
     */
    public static double dot(Vector3 a, Vector3 b){
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    /**
     * Calculates the Cross Product of Vector2 a and Vector2 b.
     * @param a Vector2 a
     * @param b Vector2 b
     * @return Cross Product of Vector2 a and Vector2 b.
     */
    public static Vector3 cross(Vector3 a, Vector3 b){
        double cx = a.y * b.z - a.z * b.y;
        double cy = a.z * b.x - a.x * b.z;
        double cz = a.x * b.y - a.y * b.x;

        return new Vector3(cx,cy,cz);
    }
}

