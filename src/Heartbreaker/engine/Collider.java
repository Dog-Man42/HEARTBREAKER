package Heartbreaker.engine;

public interface Collider {
    int NONE = 0;
    int CIRCLE = 1;
    int POLYGON = 2;

    int collisionType = 0;

    default int getColliderType(){
        return collisionType;
    }
}
