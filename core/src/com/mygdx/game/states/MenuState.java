package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;


public class MenuState extends State {
    BitmapFont font;
    Texture background1;
    private Texture playBtn;

    public MenuState(GameStateManager stateManager) {
        super(stateManager);
        cam.position.set(MyGdxGame.WIDTH/2,MyGdxGame.HEIGHT/2,0);
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().scale(3);
        background1 = new Texture("lvl4.1.png");
        playBtn = new Texture("playBtn.png");
    }

    @Override
    public void update(float dt) {
        if(Gdx.input.justTouched()) {
            handleInput();
        }
    }

    @Override
    protected void handleInput() {
        gsm.set(new TutorialState1(gsm));
        dispose();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background1,0,0,640,480);
        sb.draw(playBtn, (MyGdxGame.WIDTH / 2), MyGdxGame.HEIGHT / 2);
        font.draw(sb, "Lab Escape", (MyGdxGame.WIDTH / 2)-145, (MyGdxGame.HEIGHT / 2)+100 );
        sb.end();
    }

    @Override
    public void dispose() {
        background1.dispose();
    }
}
