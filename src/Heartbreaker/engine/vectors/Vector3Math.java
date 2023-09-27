package Heartbreaker.engine.vectors;

/*
Resources Used:
    https://www.youtube.com/watch?v=TJVA2gKFxH0&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=3
 */
public class Vector3Math {

    public static double length(Vector3 a){
        return Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
    }

    public static double distance(Vector3 a, Vector3 b){
        double dx = (b.x - a.x);
        double dy = (b.y - a.y);
        double dz = (b.z - a.z);
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static Vector3 normalize(Vector3 a){
        double length = Vector3Math.length(a);
        return new Vector3(a.x / length,a.y / length, a.z / length);
    }

    public static double dot(Vector3 a, Vector3 b){
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static double cross(Vector3 a, Vector3 b){
        double ax = a.y * b.z - b.z * b.y;
        double ay = a.z * b.x - b.x * b.z;
        double az = a.x * b.y - b.y * b.x;

        return a.x * b.y - a.y * b.x;
    }
}

