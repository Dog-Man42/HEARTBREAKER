package Heartbreaker.engine;

public interface UsesPolar {

    double radialPosition = 0;
    double radialVelocity = 0;
    double theta = 0;
    double angularVelocity = 0;

    default double cartesianToRadius(double x, double y){
        return Math.sqrt(x * x + y * y);
    }
    default double cartesianToTheta(double x, double y){
        return Math.toDegrees(Math.atan2(x ,y)) - 90;
    }
    default double polarToCartesianX(){
        return radialPosition * Math.sin(Math.toRadians(theta));
    }
    default double polarToCartesianY(){
        return radialPosition * Math.cos(Math.toRadians(theta));
    }
}
