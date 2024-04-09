package Heartbreaker.engine.collision;

import java.awt.geom.Point2D;

/**
 * Interface to be used by GameObjects that should collide with others.
 * When added to a scene, implementers are automatically added to the colliders ArrayList of the scene's {@link CollisionManager} if it exists.
 *
 * @author tuckt
 */

public interface Collider {

    //Temporary, inelegant solution. Will be replaced with a system where a collider can exist on multiple layers and have multiple masks.
    //This is sufficient for now.

    /** Exists on all Layers */
    int LAYER_ALL = 0;

    /** Exists on Layer 1 */
    int LAYER_1 = 1;

    /** Exists on Layer 2 */
    int LAYER_2 = 2;

    /** Exists on Layer 3 */
    int LAYER_3 = 3;

    /** Can collide with all Layers */
    int MASK_All = 0;

    /** Can collide with Layer 1 */
    int MASK_1 = 1;

    /** Can collide with Layer 2 */
    int MASK_2 = 2;

    /** Can collide with Layer 3 */
    int MASK_3 = 3;

    /** No hitbox */
    int NO_HITBOX = 0;

    /** Circle Hitbox */
    int CIRCLE = 1;

    /** Polygon Hitbox */
    int POLYGON = 2;

    /** Compound Hitbox. Meaning the hitbox is made of multiple hitboxes, currently unimplemented. */
    int COMPOUND = 3;

    /** Called by {@link CollisionManager} whenever the collider takes part in a collision. */
    void collided(CollisionData colData);

    /**
     * Returns what the collider can hit.
     * @return Either {@link #LAYER_ALL}, {@link #LAYER_1}, {@link #LAYER_2}, or {@link #LAYER_3}.
     */
    int getCollisionLayer();

    /**
     * Returns what the collider can be hit by.
     * @return Either {@link #MASK_All}, {@link #MASK_1}, {@link #MASK_2}, or {@link #MASK_3}.
     */
    int getCollisionMask();

    /**
     * Returns what kind of hitbox the collider has.
     * @return Either {@link #NO_HITBOX}, {@link #CIRCLE}, {@link #POLYGON}, or {@link #COMPOUND}.
     */
    int getHitBoxType();

    /**
     * Returns whether the collider is a static body or not.
     * @return True if is a static body
     */
    boolean getStatic();

    /** @return How much damage the collider deals upon collision. */
    int getDamage();

    /** @return The X-velocity of the collider. */
    double getXVelocity();

    /** @return The Y-velocity of the collider. */
    double getYVelocity();

    /** @return the X-position of the collider. */
    double getXPosition();

    /** @return the Y-position of the collider. */
    double getYPosition();

    /** @return The position of the collider as a Point2D.Double. */
    Point2D.Double getPosition();

    /** The radius of the hitbox if the collider has a circle hitbox. */
    double getRadius();

    /**
     * Returns the mass of the collider
     * @return By default 0;
     */
    default int getMass(){
        return 0;
    }

    /** The verticies of the hitbox if the collider has a polygon hitbox. */
    Point2D.Double[] getPoints();





}
