package Heartbreaker.engine.collision;

import Heartbreaker.engine.vectors.Vector;

/**
 * Contains useful data from collision events
 *
 * @author tuckt
 */
public class CollisionData {

    /** Depth of the collision, can be used for collision resolution */
    private double depth;

    /** Normal of the collision, can be used for collision resolution */
    private Vector normal;

    /** Type of the collision. Can be {@link Collision#CIRCLE_CIRCLE}, {@link Collision#CIRCLE_POLY}, or {@link Collision#POLY_POLY}*/
    private int collisionType;

    /** Reference to the other Collider in the event */
    private Collider collider;

    /** Default Constructor without a provided collider */
    public CollisionData(double depth, Vector normal, int collisionType){
        this.depth = depth;
        this.normal = normal;
        this.collisionType = collisionType;
        collider = null;
    }

    /** Constructor with a provided collider */
    public CollisionData(double depth, Vector normal, int collisionType, Collider collider){
        this.depth = depth;
        this.normal = normal;
        this.collisionType = collisionType;
        this.collider = collider;
    }

    /** @return The depth of the intersection */
    public double getDepth(){
        return depth;
    }

    /** @return The normal of the other collider */
    public Vector getNormal(){
        return normal;
    }

    /** @return An int representing the collision type */
    public int getCollisionType(){return collisionType;}

    /** @return The other collider in the event */
    public Collider getCollider(){
        return collider;
    }

    /** Sets the other collider in the event */
    public void setCollider(Collider collider){
        this.collider = collider;
    }

    /** @return A copy of this CollisionData with its normal inverted */
    public CollisionData inverted(){
        return new CollisionData(depth, Vector.multiply(normal,-1),collisionType,collider);
    }

    @Override
    public String toString(){
        return "[Collider: " + collider.toString() + ", Collision Type: " + collisionType + ", Depth: " + depth + ", Normal: " + normal.toString() + "]";
    }

}
