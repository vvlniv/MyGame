package com.vavilin.firstgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.vavilin.firstgame.MyGdxGame;
import com.vavilin.firstgame.sprites.Body;
import com.vavilin.firstgame.sprites.Tube;

/**
 * Created by user on 10.12.2015.
 */
public class PlayState extends State {

    public static final int TUBE_SPACING = 125;
    public static final int  TUBE_COUNT = 4;

    private Body body;
    private Texture bg;
    private Texture ground;

    private static final int GROUND_Y_OFFSET = -30;

    private Vector2 groundPos1, groundPos2;


    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        body = new Body(50, 300);
        camera.setToOrtho(false, MyGdxGame.WIDTH /2, MyGdxGame.HEIGHT /2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        tubes= new Array<Tube>();
        for(int i=1; i<=TUBE_COUNT;i++){
            tubes.add(new Tube(i *(TUBE_SPACING + Tube.TUBE_WIDTH)));
        }

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            body.jump();
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        body.update(dt);
        camera.position.x = body.getPosition().x+80;

        for(int i = 0; i < tubes.size; i++){
            Tube tube = tubes.get(i);
            if (camera.position.x - (camera.viewportWidth /2) > tube.getPosTopTube().x+tube.getTopTube().getWidth()){
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING)*TUBE_COUNT));
            }
            if (tube.collides(body.getBounds()))
                gsm.set(new PlayState(gsm));
        }
        if(body.getPosition().y <= ground.getHeight()+GROUND_Y_OFFSET){
            gsm.set(new PlayState(gsm));
        }
        camera.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth / 2), 0);
      //  sb.draw(bg, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        sb.draw(body.getBody(), body.getPosition().x, body.getPosition().y);
        for(Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosBotTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        sb.end();

    }

    @Override
    public void dispose() {
        bg.dispose();
        body.dispose();
        ground.dispose();
        for (Tube tube : tubes)
            tube.dispose();
        System.out.println("PlayState Disposed");

    }
    private void updateGround(){
        if (camera.position.x - (camera.viewportWidth / 2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if (camera.position.x - (camera.viewportWidth / 2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }
}
