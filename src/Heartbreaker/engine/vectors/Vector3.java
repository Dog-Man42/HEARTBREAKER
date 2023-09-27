package Heartbreaker.engine.vectors;

/*
Resources Used
    https://www.youtube.com/watch?v=lzI7QUyl66g&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=1
 */
public class Vector3 {
    public final double x;
    public final double y;
    public final double z;

    public final static Vector3 Zero = new Vector3(0,0,0);

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 sum(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }
    public static Vector3 difference(Vector3 a, Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }
    public static Vector3 multiply(Vector3 a,double scalar) {
        return new Vector3(a.x * scalar, a.y * scalar, a.z * scalar);
    }
    public static Vector3 multiply(Vector3 a,Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }
    public static Vector3 negate(Vector3 a) {
        return new Vector3(-a.x, -a.y, -a.z);
    }
    public static Vector3 divide(Vector3 a,double scalar) {
        return new Vector3(a.x / scalar, a.y / scalar, a.z / scalar);
    }
    public static Vector3 divide(Vector3 a, Vector3 b) {
        return new Vector3(a.x /b.x, a.y / b.y, a.z / b.z);
    }

    public boolean equals(Vector3 b) {
        return this.x == b.x && this.y == b.y && this.z == b.z;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }

}
