package Heartbreaker.engine.collision;


import java.util.ArrayList;

public class CollisionManager {

    /** An ArrayList containing all colliders in a scene */
    private ArrayList<Collider> colliders;

    /** Default constructor that initializes colliders */
    public CollisionManager(){
        colliders = new ArrayList<>();
    }

    /**
     * Updates all colliders by comparing each Collider with every other Collider, determining if they can collide based
     * on what they can hit and can be hit by. If they are allowed to collide, then {@link Collision} will determine if there
     * is an intersection between the two Colliders. If they do intersect, then a {@link CollisionData} object is created and is sent to
     * each collider through {@link Collider#collided(CollisionData)}.
     */
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
                        //TODO Add double-compound collisions when needed
                    } else {
                        //Currently, if they are not the same hitbox type, then it is as Circle Polygon collision
                        //Todo Add Circle-Compound and Poly-Compound collisions when needed.
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
                    collider2.collided(colData.inverted());
                }
            }
        }
    }

    /**
     * Determines whether two given colliders are allowed to collide with each other.
     *
     * @param collider1 Collider 1
     * @param collider2 Collider 2
     * @return True if colliders are allowed to collide with each other
     */
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

    /** Adds a collider to {@link #colliders} */
    public void addCollider(Collider collider){
        colliders.add(collider);
    }

    /** Removes a collider from {@link #colliders} */
    public boolean removeCollider(Collider collider){
        if(collider == null){
            return false;
        } else {
            colliders.remove(collider);
            return true;
        }
    }

    /** Clears {@link #colliders} */
    public void clear(){
        colliders.clear();
    }

    /** @return Returns the size of {@link #colliders} */
    public int size(){
        return colliders.size();
    }

    /** @returns Returns an ArrayList containing all Colliders */
    public ArrayList<Collider> getColliders(){
        return colliders;
    }


}
