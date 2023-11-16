package Heartbreaker.engine.collision;

import Heartbreaker.engine.vectors.Vector;

public class CollisionData {

    private double depth;
    private Vector normal;
    private int collisionType;
    private int sign = 0;
    private Collider collider;


    public CollisionData(double depth, Vector normal, int collisionType){
        this.depth = depth;
        this.normal = normal;
        this.collisionType = collisionType;
        collider = null;
    }
    public CollisionData(double depth, Vector normal, int collisionType, Collider collider){
        this.depth = depth;
        this.normal = normal;
        this.collisionType = collisionType;
        this.collider = collider;
    }

    public double getDepth(){
        return depth;
    }
    public Vector getNormal(){
        return normal;
    }
    public int getCollisionType(){return collisionType;}
    public Collider getCollider(){
        return collider;
    }
    public void setCollider(Collider collider){
        this.collider = collider;
    }

    public static CollisionData invertNormal(CollisionData colData){
        return new CollisionData(colData.getDepth(), Vector.multiply(colData.getNormal(),-1),colData.getCollisionType(),colData.getCollider());
    }

}
