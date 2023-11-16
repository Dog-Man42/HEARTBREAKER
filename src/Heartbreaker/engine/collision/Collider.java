package Heartbreaker.engine.collision;

import java.awt.geom.Point2D;

public interface Collider {

    int HIT_BY_PLAYER = 0;
    int HIT_BY_ENEMY = 1;
    int HIT_BY_ALL = 2;
    int HIT_BY_NONE = 2;

    int HITS_ENEMY = 0;
    int HITS_PLAYER = 1;
    int HITS_ALL = 2;
    int HITS_NONE = 3;

    int NO_HITBOX = 0;
    int CIRCLE = 1;
    int POLYGON = 2;
    int COMPOUND = 3;


    void collided(CollisionData colData);
    int getCanHit();
    int getHitBy();
    int getHitBoxType();
    boolean getStatic();
    int getDamage();
    double getXVelocity();
    double getYVelocity();
    double getXPosition();
    double getYPosition();

    Point2D.Double getPosition();

    double getRadius();
    Point2D.Double[] getPoints();





}
