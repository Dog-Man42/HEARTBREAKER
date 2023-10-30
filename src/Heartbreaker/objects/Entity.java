package Heartbreaker.objects;

import Heartbreaker.engine.collision.Collider;
import Heartbreaker.engine.collision.CollisionData;

import java.awt.*;
import java.awt.geom.Point2D;

public class Entity extends BaseObject implements Collider {




    @Override
    public void update() {

    }

    public void damage(int dmg) {

    }

    @Override
    public void draw(Graphics2D g) {

    }
    @Override
    public void collided(CollisionData colData) {

    }

    @Override
    public int getCanHit() {
        return 0;
    }

    @Override
    public int getHitBy() {
        return 0;
    }

    @Override
    public int getHitBoxType() {
        return 0;
    }

    @Override
    public boolean getStatic() {
        return false;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public float getXVelocity() {
        return 0;
    }

    @Override
    public float getYVelocity() {
        return 0;
    }

    @Override
    public float getRadius() {
        return 0;
    }

    @Override
    public Point2D.Float[] getPoints() {
        return new Point2D.Float[0];
    }

}
