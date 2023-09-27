package Heartbreaker.engine.vectors;

/*
Resources Used:
    https://www.youtube.com/watch?v=TJVA2gKFxH0&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=3
 */
public class VectorMath {

    public static double length(Vector a){
        return Math.sqrt(a.x * a.x + a.y * a.y);
    }

    public static double distance(Vector a, Vector b){
        double dx = (b.x - a.x);
        double dy = (b.y - a.y);
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static Vector normalize(Vector a){
        double length = VectorMath.length(a);
        return new Vector(a.x / length,a.y / length);
    }

    public static double dot(Vector a, Vector b){
        return a.x * b.x + a.y * b.y;
    }

    public static double cross(Vector a, Vector b){
        return a.x * b.y - a.y * b.x;
    }
}

