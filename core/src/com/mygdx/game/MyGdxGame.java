package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MenuState;

public class MyGdxGame extends ApplicationAdapter {
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final String TITLE = "Chronicles of Time";
	private GameStateManager gsm;
	public SpriteBatch batch;
	public static float xPos = 2;
	public static float yPos = 1;
	public static int keysHeld = 0;
	public static boolean key1Held = false;
	public static boolean key2Held = false;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
