package com.mygdx.game.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Game implements ApplicationListener {
    public SpriteBatch batch;
    private Texture playerTexture;
    private int playerX;
    private int playerY;
    private Sprite player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        FileHandle playerFileHandle = Gdx.files.internal("walksprite.png");
        playerTexture = new Texture(playerFileHandle);
        player = new Sprite(playerTexture, 0, 158, 32, 64);
        playerX = 0;
        playerY = 0;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(player, playerX, playerY);
        batch.end();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}

