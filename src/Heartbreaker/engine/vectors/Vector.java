package Heartbreaker.engine.vectors;

/*
Resources Used
    https://www.youtube.com/watch?v=lzI7QUyl66g&list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs&index=1
 */
public class Vector {
    public final double x;
    public final double y;

    public final static Vector Zero = new Vector(0,0);

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static Vector sum(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y);
    }
    public static Vector difference(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y);
    }
    public static Vector multiply(Vector a,double scalar) {
        return new Vector(a.x * scalar, a.y * scalar);
    }
    public static Vector multiply(Vector a,Vector b) {
        return new Vector(a.x * b.x, a.y * b.y);
    }
    public static Vector negate(Vector a) {
        return new Vector(-a.x, -a.y);
    }
    public static Vector divide(Vector a,double scalar) {
        return new Vector(a.x / scalar, a.y / scalar);
    }
    public static Vector divide(Vector a, Vector b) {
        return new Vector(a.x /b.x, a.y / b.y);
    }

    public boolean equals(Vector b) {
        return this.x == b.x && this.y == b.y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

}
