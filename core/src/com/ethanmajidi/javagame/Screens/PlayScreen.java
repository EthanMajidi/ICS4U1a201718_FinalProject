package com.ethanmajidi.javagame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ethanmajidi.javagame.JavaGame;
import com.ethanmajidi.javagame.Scenes.Hud;
import com.ethanmajidi.javagame.Sprites.Goomba;
import com.ethanmajidi.javagame.Sprites.Java;
import com.ethanmajidi.javagame.Sprites.Mario;
import com.ethanmajidi.javagame.Tools.B2WorldCreator;

import com.ethanmajidi.javagame.Tools.WorldContactListener;



/**
 * Created by EthanMajidi on 2017-12-17.
 */

public class PlayScreen  implements Screen {
    //Reference to our game for setting screens
    private JavaGame game;
    private TextureAtlas atlas;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //box2d var
    private World world;
    private Box2DDebugRenderer b2dr;

    //Sprites
    private Java player;
    private Goomba goomba;

    //audio.music
    //private Music music;


    public PlayScreen(JavaGame game){

        atlas = new TextureAtlas("Player_and_Enemies.txt");

        this.game = game;
        //Creates camera to follow mario
        gamecam = new OrthographicCamera();

        //creates the fitviewport so that the game always fits the screen
        gamePort = new FitViewport(JavaGame.V_WIDTH /JavaGame.PPM,JavaGame.V_HEIGHT/JavaGame.PPM, gamecam);

        //creates the hud for the score and time etc...
        hud = new Hud(game.batch);

        //loads our map and renderer
        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/JavaGame.PPM);


        //initially set our gamecam to be centered right
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() /2, 0);


        //create our 2d World
        world = new World(new Vector2(0, -10), true);
        //allows debug lines in our world
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);
        //Creates our player in the game world

        player = new Java(this);

        world.setContactListener(new WorldContactListener());


        //audio.music
        //music = JavaGame.manager.get("audio/music/JavaGameMusic.wav", Music.class);
        //music.setLooping(true);
        //music.play();


        goomba = new Goomba(this, 32/JavaGame.PPM, 32/JavaGame.PPM);

    }

    public TextureAtlas getAtlas(){
        return atlas;
    }


    @Override
    public void show() {

    }
    //maybe use double instead of float
    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f),player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0),player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)&& player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0),player.b2body.getWorldCenter(), true);
    }

    public void update(float dt){
        //handle user input
        handleInput(dt);

        //takes 1 step in the physics simulation (60 times per second)
        world.step(1/60f, 6, 2);

        //connecting hud to timer
        player.update(dt);
        goomba.update(dt);
        hud.update(dt);

        //attach our gamecam to our player x coordinates
        gamecam.position.x = player.b2body.getPosition().x;
        //update our gamecam after changes
        gamecam.update();
        //tell our render what our camera can see
        renderer.setView(gamecam);

    }

    @Override
    public void render(float delta) {
        //seperate logic from render
        update(delta);

        //clear the game screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        //goomba.draw(game.batch);
        game.batch.end();

        //Set our batch to now draw what the hud sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}

