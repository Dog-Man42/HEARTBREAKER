package Heartbreaker.engine.collision;

public interface Collider {


    int collisionType = 0;

    default int getColliderType(){
        return collisionType;
    }

}
