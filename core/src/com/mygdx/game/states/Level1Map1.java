package com.mygdx.game.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Controller;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.Player;

public class Level1Map1 extends State{
    public static final String TAG = Level1Map1.class.getName();
    public World world;
    public Box2DDebugRenderer debugRenderer;
    OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private Player player;

    private Controller controller;
    private ShapeRenderer shapeRenderer;
    public Array<Body> floors;
    public BodyDef floorDef;
    public PolygonShape floorShape;
    public MapObjects floorObjects;

    public MapObjects spikesObjects;
    public Array<Polygon> spikes;

    public MapObjects exitObjects;
    public Array<Polygon> exit;

    public Level1Map1(GameStateManager stateManager) {
        super(stateManager);

        // Change image to the correct background once we have it
        cam.setToOrtho(false, 15, 10) ;

        Box2D.init();
        world = new World(new Vector2(0, -9.8f), true);
        // the y value here is the gravity
        debugRenderer = new Box2DDebugRenderer();
        player = new Player(MyGdxGame.xPos,MyGdxGame.yPos, world);


        map = new TmxMapLoader().load("lvl1.1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, State.PIXEL_TO_METER);
        shapeRenderer = new ShapeRenderer();
        Gdx.app.log(TAG, "Application Listener Created");
        //mapObjects = .getLayers().get("Collision");
        controller = new Controller();
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Gdx.input.setInputProcessor(controller);
        }

        floorObjects = map.getLayers().get("Ground").getObjects();

        spikesObjects = map.getLayers().get("Spikes").getObjects();
        spikes = new Array<Polygon>();

        exitObjects = map.getLayers().get("Exit").getObjects();
        exit = new Array<Polygon>();



        floors = new Array<Body>();
        floorDef = new BodyDef();
        floorShape = new PolygonShape();

        int counter = 0;
        for (PolygonMapObject obj : floorObjects.getByType(PolygonMapObject.class)) {
            floorDef.position.set(obj.getPolygon().getX() * State.PIXEL_TO_METER, obj.getPolygon().getY() * State.PIXEL_TO_METER);
            floors.add(world.createBody(floorDef));
            float[] vertices = obj.getPolygon().getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] * State.PIXEL_TO_METER;
            }
            floorShape.set(vertices);
            floors.get(counter).createFixture(floorShape, 0.0f);
            counter++;
        }
        counter = 0;
        for (PolygonMapObject obj : spikesObjects.getByType(PolygonMapObject.class)) {
            float[] vertices = obj.getPolygon().getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] * State.PIXEL_TO_METER;
            }
            Polygon temp = new Polygon();
            temp.setVertices(vertices);
            temp.setPosition(obj.getPolygon().getX() * State.PIXEL_TO_METER, obj.getPolygon().getY() * State.PIXEL_TO_METER);
            spikes.add(temp);
            counter++;
        }
        counter = 0;
        for (PolygonMapObject obj : exitObjects.getByType(PolygonMapObject.class)) {
            float[] vertices = obj.getPolygon().getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] * State.PIXEL_TO_METER;
            }
            Polygon temp = new Polygon();
            temp.setVertices(vertices);
            temp.setPosition(obj.getPolygon().getX() * State.PIXEL_TO_METER, obj.getPolygon().getY() * State.PIXEL_TO_METER);
            exit.add(temp);
            counter++;
        }

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        MyGdxGame.xPos = player.playerBody.getPosition().x;
        MyGdxGame.yPos = player.playerBody.getPosition().y;
        spikesCheck();
        exitCheck();

//
//        if ((player.playerBody.getPosition().x <= (200 * State.PIXEL_TO_METER)) &&
//                (player.playerBody.getPosition().y >= (360 * State.PIXEL_TO_METER))) {
//            cam.position.set(200 * State.PIXEL_TO_METER,360 * State.PIXEL_TO_METER, 0);
//        } else if ((player.playerBody.getPosition().x <= (200 * State.PIXEL_TO_METER)) &&
//                (player.playerBody.getPosition().y <= (120 * State.PIXEL_TO_METER))) {
//            cam.position.set(200 * State.PIXEL_TO_METER,120 * State.PIXEL_TO_METER, 0);
//        } else if ((player.playerBody.getPosition().x >= (600 * State.PIXEL_TO_METER)) &&
//                (player.playerBody.getPosition().y >= (360 * State.PIXEL_TO_METER))) {
//            cam.position.set(600 * State.PIXEL_TO_METER,360 * State.PIXEL_TO_METER, 0);
//        } else if ((player.playerBody.getPosition().x >= (600 * State.PIXEL_TO_METER)) &&
//                (player.playerBody.getPosition().y <= (120 * State.PIXEL_TO_METER))) {
//            cam.position.set(600 * State.PIXEL_TO_METER,120 * State.PIXEL_TO_METER, 0);
//        } else if ((player.playerBody.getPosition().x <= (200 * State.PIXEL_TO_METER))) {
//            cam.position.set(200 * State.PIXEL_TO_METER,player.playerBody.getPosition().y, 0);
//        } else if ((player.playerBody.getPosition().x >= (600 * State.PIXEL_TO_METER))) {
//            cam.position.set(600 * State.PIXEL_TO_METER,player.playerBody.getPosition().y, 0);
//        }
//        else if ((player.playerBody.getPosition().y >= (360 * State.PIXEL_TO_METER))) {
//            cam.position.set(player.playerBody.getPosition().x,360 * State.PIXEL_TO_METER, 0);
//        }
//        else if ((player.playerBody.getPosition().y <= (120 * State.PIXEL_TO_METER))) {
//            cam.position.set(player.playerBody.getPosition().x,120 * State.PIXEL_TO_METER, 0);
//        }
//        else{
//            cam.position.set(player.playerBody.getPosition(), 0);
//        }
//
//        cam.update();

        if (controller.isAtkPressed()) {
            gsm.set(new Level1Map2(gsm));
            dispose();
        }
        else if(controller.isUpPressed()) {
            player.jump();
        } else if(controller.isLeftPressed()) {
            player.walkLeft();
        } else if(controller.isRightPressed()) {
            player.walkRight();
        }
//        else{
//            player.still();
//        }
        player.update(dt);
    }

    public void spikesCheck(){
        for (Polygon deathSpikes : spikes){
            if(Intersector.overlapConvexPolygons(player.bounds, deathSpikes)){
                MyGdxGame.xPos = 1;
                MyGdxGame.yPos = 9;
                gsm.set(new Level1Map1(gsm));
                dispose();
            }
        }
    }

    public void exitCheck(){
        for (Polygon nextLevel : exit){
            if(Intersector.overlapConvexPolygons(player.bounds, nextLevel)){
                gsm.set(new Level1Map1(gsm));
                dispose();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        renderer.setView(cam);
        renderer.render();
        sb.begin();
        sb.draw(player.getTexture(Gdx.graphics.getDeltaTime()),player.getPosition().x,player.getPosition().y,1, 1);
        sb.end();

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //    shapeRenderer.rect(player.AtkHitbox.getX(),player.AtkHitbox.getY(),player.AtkHitbox.getWidth(),player.AtkHitbox.getHeight());
        shapeRenderer.end();
        controller.draw();
        cam.update();
//        debugRenderer.render(world, cam.combined);
        world.step(1/60f, 6, 2);
    }

    @Override
    public void dispose() {
        world.dispose();
        map.dispose();
        shapeRenderer.dispose();
        renderer.dispose();
//        debugRenderer.dispose();
        floorShape.dispose();
        player.dispose();
    }
}