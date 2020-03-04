package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Controller;
import com.mygdx.game.states.State;

public class Player {

    private Vector3 position;
    private Texture walkCharacter, jumpCharacter;

    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> walk, jump;
    float stateTime;
    private Controller controller;


    private String Activity, LastActivity;
    private boolean faceRight;
    public BodyDef bodyDef;
    public Body playerBody;
    public FixtureDef fixtureDef;
    public Vector2 vel;
    public int health;
    public TextureRegion walkRegion;
    public TextureRegion jumpRegion;
    public TextureRegion region;
    private final float MAX_VELOCITY = 3f;
    private final float MAX_VELOCITY_Y = .05f;
    public PolygonShape polygon;
    public Polygon bounds;
    public int getWidth() {
        return width;
    }

    private int width;

    public int getHeight() {
        return height;
    }
    private int height;
    World world;

    public Player(float x, float y, World world){
        this.world = world;
        controller = new Controller();
        jumpCharacter = new Texture("jumpSprite.png");
        walkCharacter = new Texture("walkSprite.png");

        TextureRegion[][] tmp1 = TextureRegion.split(walkCharacter, walkCharacter.getWidth() / 2, walkCharacter.getHeight() / 3);
        TextureRegion[][] tmp2 = TextureRegion.split(jumpCharacter, jumpCharacter.getWidth() / 2, jumpCharacter.getHeight() / 3);

        Array<TextureRegion> walkFrames = new Array<TextureRegion>();
        Array<TextureRegion> jumpFrames = new Array<TextureRegion>();

        int count = 0;

        // For loop to add frames from the split TextureRegion into the Array
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (count < 6) {
                    walkFrames.add(tmp1[j][i]); // Fill in Array with frames
                    count++;
                }
            }
        }

        int count2 = 0;

        // For loop to add frames from the split TextureRegion into the Array
        for (int k = 0; k < 2; k++) {
            for (int l = 0; l < 3; l++) {
                if (count2 < 6) {
                    jumpFrames.add(tmp2[l][k]); // Fill in Array with frames
                    count2++;
                }
            }
        }
        walk = new Animation<TextureRegion>(0.125f, walkFrames); // Set Animations to use Array of frames
        jump = new Animation<TextureRegion>(0.125f, jumpFrames); // Set Animations to use Array of frames

        region = new TextureRegion();

        stateTime = 0;
        health = 10;
        Activity = "none";
        LastActivity = "none";
        width = getTexture(Gdx.graphics.getDeltaTime()).getRegionWidth();
        height = getTexture(Gdx.graphics.getDeltaTime()).getRegionHeight();

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        playerBody = world.createBody(bodyDef);

        polygon = new PolygonShape();
        bounds = new Polygon();
        bounds.setVertices(new float[] {((getWidth()/8) * State.PIXEL_TO_METER), 5 * State.PIXEL_TO_METER,((getWidth()-40) * State.PIXEL_TO_METER), 5 * State.PIXEL_TO_METER,
                ((getWidth()-40) * State.PIXEL_TO_METER), ((getHeight()/2) * State.PIXEL_TO_METER), ((getWidth()/8) * State.PIXEL_TO_METER), ((getHeight()/2) * State.PIXEL_TO_METER)});
        polygon.set(bounds.getVertices());

        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;

        playerBody.createFixture(fixtureDef);
        position = new Vector3(playerBody.getPosition(), 0);
        playerBody.setFixedRotation(true);
        vel = this.playerBody.getLinearVelocity();
        bounds.setPosition(playerBody.getPosition().x, playerBody.getPosition().y);

        faceRight = false;
        Activity = "Walking";
    }

    public void update(float dt){
        if( playerBody.getLinearVelocity().y == 0){
            Activity = "Walking";
        }
        position.set(playerBody.getPosition(), 0);
        bounds.setPosition(playerBody.getPosition().x, playerBody.getPosition().y);
        flipFrames();
    }

    public void walkLeft() {
        if(vel.x > -MAX_VELOCITY) {
            playerBody.applyLinearImpulse(-.05f, 0f, getPosition().x / 2, getPosition().y / 2, true);
        }
        else {
            playerBody.applyLinearImpulse(.05f, 0f, getPosition().x / 2, getPosition().y / 2, true);
        }
    }
    public void walkRight() {
        if(vel.x < MAX_VELOCITY){
            playerBody.applyLinearImpulse(.05f,0f,getPosition().x/2,getPosition().y/2,true);
        }
        else {
            playerBody.applyLinearImpulse(-.05f, 0f, getPosition().x / 2, getPosition().y / 2, true);
        }
    }
    public void jump() {
        Activity = "Jumping";
        if(vel.y == 0) {
            playerBody.applyLinearImpulse(0f, 1.5f, getPosition().x / 2, getPosition().y / 2, true);
        }
    }
    public void climb() {
        if(vel.y < MAX_VELOCITY_Y){
            playerBody.applyLinearImpulse(0f, .5f, getPosition().x / 2, getPosition().y / 2, true);
        }
    }


    public TextureRegion getTexture(float dt) {
        walkRegion = walk.getKeyFrame(stateTime, true);
        jumpRegion = jump.getKeyFrame(stateTime,false);
        region = walkRegion;
        if (Activity.equals("Jumping")) {
            region = jumpRegion;
        } else if (Activity.equals("Walking")) {
            region = walkRegion;
        }

        stateTime = LastActivity.equals(Activity) ? stateTime + dt : 0;
        LastActivity = Activity;
        return region;
    }

    public void flipFrames(){
        if ((playerBody.getLinearVelocity().x < 0 && faceRight)) {
            flipAll(walk);
            flipAll(jump);
            faceRight = false;
        } else if ((playerBody.getLinearVelocity().x > 0 && !faceRight)) {
            flipAll(walk);
            flipAll(jump);
            faceRight = true;
        }
    }

    private void flipAll (Animation anim) {
        for (Object obj : anim.getKeyFrames()) {
            TextureRegion reg = (TextureRegion) obj;
            reg.flip(true, false);
        }
    }


    public Vector3 getPosition() {
        return position;
    }

    public void dispose() {
        walkCharacter.dispose();
        jumpCharacter.dispose();
    }
}
