package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

public class VictoryState extends State {
    Texture background;

    public VictoryState(GameStateManager stateManager) {
        super(stateManager);
        background = new Texture("pixil-layer-Background.png");
        cam.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
    }


    @Override
    protected void handleInput() {
        gsm.set(new MenuState(gsm));
        dispose();
    }

    @Override
    public void update(float dt) {
        if(Gdx.input.justTouched()) {
            handleInput();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background,0,0);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();

    }
}
