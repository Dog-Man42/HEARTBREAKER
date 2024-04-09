package Heartbreaker.objects;

import Heartbreaker.engine.collision.CollisionData;
import Heartbreaker.engine.vectors.Vector2;
import Heartbreaker.engine.vectors.VectorMath;

import java.awt.*;

public class RichochetBullet extends Bullet{

    private int hp = 0;

    public RichochetBullet(double xpos,double ypos, double shooterXvel, double shooterYvel, double angle,double speed, int limit,boolean player){
        super(xpos,ypos,shooterXvel,shooterYvel,angle,speed,limit,player);
        hp = 4;
    }

    @Override
    public void collided(CollisionData colData){
        if(hp <= 0){
            getScene().removeObject(this);
        } else {
            changeXPos(-(-colData.getNormal().x * colData.getDepth()));
            changeYPos(-(-colData.getNormal().y * colData.getDepth()));
            double speed = VectorMath.length(new Vector2(getXVelocity(),getYVelocity()));
            setXvel(speed * colData.getNormal().x);
            setYvel(speed * colData.getNormal().y);
            Vector2 v = VectorMath.normalize(new Vector2(getXVelocity(), getYVelocity()));
            setRotation(Math.toDegrees(-Math.atan2(v.x, v.y)));
            hp--;
        }
    }
    @Override
    public int getCollisionLayer() {
        return colLayer;
    }

    @Override
    public int getCollisionMask() {
        return colMask;
    }

    @Override
    public void draw(Graphics2D g, double delta){
        g.setColor(Color.ORANGE);
        g.drawPolygon(realizePoly());
    }



}
