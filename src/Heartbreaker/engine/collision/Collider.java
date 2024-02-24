package Heartbreaker.engine.collision;

import java.awt.geom.Point2D;

/**
 * Interface to be used by GameObjects that should collide with others.
 * When added to a scene, implementers are automatically added to the colliders ArrayList of the scene's {@link CollisionManager} if it exists.
 *
 * @author tuckt
 */

public interface Collider {

    /** Can be hit by the player */
    int HIT_BY_PLAYER = 0;

    /** Can be hit by an enemy */
    int HIT_BY_ENEMY = 1;

    /** Can be hit by everything */
    int HIT_BY_ALL = 2;

    /** Cannot be hit. Note: This could be replaced by something else eventually. */
    int HIT_BY_NONE = 3;

    /** Can hit enemies */
    int HITS_ENEMY = 0;

    /** Can hit the player */
    int HITS_PLAYER = 1;

    /** Can hit everything */
    int HITS_ALL = 2;

    /** Cannot hit anything. Note: This could be replaced by something else eventually. */
    int HITS_NONE = 3;

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
     * @return Either {@link #HITS_PLAYER}, {@link #HITS_ENEMY}, {@link #HITS_ALL}, or {@link #HITS_NONE}.
     */
    int getCanHit();

    /**
     * Returns what the collider can be hit by.
     * @return Either {@link #HIT_BY_PLAYER}, {@link #HIT_BY_ENEMY}, {@link #HIT_BY_ALL}, or {@link #HIT_BY_NONE}.
     */
    int getHitBy();

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

    /** The verticies of the hitbox if the collider has a polygon hitbox. */
    Point2D.Double[] getPoints();





}
