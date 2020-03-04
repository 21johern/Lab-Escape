package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by missionbit on 6/22/17.
 */

public class Animations {
    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int frame;



    public Animations(TextureRegion region, int frameCount, float cycleTime, int row, int col){
        frames = new Array<TextureRegion>();
        int frameWidth = region.getRegionWidth() / col;
        int frameHeight = region.getRegionHeight()/ row;
        for (int r = 0; r < row; r++){
            for (int c = 0; c < col; c++) {
                frames.add(
                        new TextureRegion(
                                region,
                                c * frameWidth,
                                r * frameHeight,
                                frameWidth,
                                frameHeight

                        )
                );
            }
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float dt){
        currentFrameTime += dt;
        if(currentFrameTime > maxFrameTime){
            frame++;
            currentFrameTime = 0;
        }
        if(frame >= frameCount)
            frame = 0;
    }

    public void flipFrames() {
        for (TextureRegion textureRegion : frames) {
            textureRegion.flip(true, false);
        }
    }
    public TextureRegion getFrame(){
        return frames.get(frame);
    }

    public void pause() {
        frame = 0;
    }

    public void resume(float dt) {
        update(dt);
    }
}