package com.vavilin.firstgame.sprites;

import com.badlogic.gdx.Gdx;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by user on 10.12.2015.
 */
public class Body
{
    public static final int MOVEMENT = 100;
    public static final int GRAVITY = -15;
    private Vector3 position;
    private  Vector3 velocity;
    private Animation bodyAnimation;

    private Texture  texture;

    private Sound flap;

    private Rectangle bounds;

    public Body(int x, int y) {

        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        texture = new Texture("birdanimation.png");
        bodyAnimation = new Animation(new TextureRegion(texture),3,0.5f);
        bounds = new Rectangle(x,y,texture.getWidth()/3,texture.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));

    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getBody() {
        return bodyAnimation.getFrame();
    }

    public void update(float dt){
        bodyAnimation.update(dt);
        if (position.y > 0)
            velocity.add(0,GRAVITY,0);
        velocity.scl(dt);
        position.add(MOVEMENT*dt, velocity.y, 0);
        if (position.y < 0)
            position.y=0;

        bounds.setPosition(position.x,position.y);

        velocity.scl(1/dt);
    }

    public void jump(){
        velocity.y = 250;
        flap.play(0.5f);
    }

    public Rectangle getBounds(){
        return bounds;
    }


    public void dispose() {
        texture.dispose();
        flap.dispose();
    }
}
