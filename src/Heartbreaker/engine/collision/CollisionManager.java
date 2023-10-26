package Heartbreaker.engine.collision;

import java.util.ArrayList;

public class CollisionManager {

    private ArrayList<Collider> colliders;

    CollisionManager(){
        colliders = new ArrayList<>();
    }

    public void addCollider(Collider collider){
        colliders.add(collider);
    }

    public boolean removeCollider(Collider collider){
        if(collider == null){
            return false;
        } else {
            colliders.remove(collider);
            return true;
        }
    }


}
