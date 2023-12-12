package Heartbreaker.engine.collision;


import java.util.ArrayList;

public class CollisionManager {

    private ArrayList<Collider> colliders;

    public CollisionManager(){
        colliders = new ArrayList<>();
    }

    public void updateColliders(){
        for(int i = 0; i < colliders.size(); i++){
            Collider collider1 = colliders.get(i);
            for(int j = i+1; j < colliders.size(); j++){
                Collider collider2 = colliders.get(j);
                CollisionData colData = null;
                //Determines if the colliders can hit each other
                if(canCollide(collider1,collider2)){
                    //If hitboxes are the same type
                    if(collider1.getHitBoxType() == collider2.getHitBoxType()){
                        if(collider1.getHitBoxType() == Collider.CIRCLE){
                            colData = Collision.circleCircle(collider1.getPosition(),collider1.getRadius(),collider2.getPosition(),collider2.getRadius());
                        }
                        if(collider1.getHitBoxType() == Collider.POLYGON){
                            colData = Collision.polygonPolygon(collider1.getPoints(),collider2.getPoints());
                        }
                        //double compound hitbox will go here and may god have mercy on my soul when I add that.
                        //This method will have a cursed big O notation
                    } else {
                        //Currently, if they are not the same hitbox type, then it is as Circle Polygon collision
                        if(collider1.getHitBoxType() == Collider.CIRCLE){
                            colData = Collision.circlePolygon(collider1.getPosition(),collider1.getRadius(),collider2.getPoints());
                        } else {
                            colData = Collision.circlePolygon(collider2.getPosition(),collider2.getRadius(),collider1.getPoints());
                        }
                    }

                }
                if(colData != null){
                    colData.setCollider(collider2);
                    collider1.collided(colData);
                    colData.setCollider(collider1);
                    collider2.collided(colData);
                }
            }
        }
    }

    public static boolean canCollide(Collider collider1, Collider collider2){
        boolean hNone1 = collider1.getCanHit() == Collider.HIT_BY_NONE;
        boolean hNone2 = collider2.getCanHit() == Collider.HIT_BY_NONE;
        if(hNone1 && hNone2){
            return false;
        }

        if(collider1.getCanHit() == Collider.HITS_ALL || collider2.getCanHit() == Collider.HITS_ALL){
            return true;
        }
        if(collider1.getCanHit() == collider2.getHitBy() || hNone2){
            return true;
        }
        if(collider2.getCanHit() == collider1.getHitBy() || hNone1){
            return true;
        }
        return false;
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

    public void clear(){
        colliders.clear();
    }

    public int size(){
        return colliders.size();
    }

    public ArrayList<Collider> getColliders(){
        return colliders;
    }


}
