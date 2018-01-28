package com.ethanmajidi.javagame.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ethanmajidi.javagame.JavaGame;
import com.ethanmajidi.javagame.Screens.PlayScreen;
import com.ethanmajidi.javagame.Sprites.Brick;
import com.ethanmajidi.javagame.Sprites.Coin;
import com.ethanmajidi.javagame.Sprites.Enemy;
import com.ethanmajidi.javagame.Sprites.Goomba;
import com.ethanmajidi.javagame.Sprites.Pole;
import com.ethanmajidi.javagame.Sprites.Turtle;

/**
 * Created by EthanMajidi on 2018-01-21.
 */

public class B2WorldCreator {
    private com.badlogic.gdx.utils.Array<Goomba> goombas;
    private com.badlogic.gdx.utils.Array<Turtle> turtles;


    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //ground bodies/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect  = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2) / JavaGame.PPM, (rect.getY() + rect.getHeight() / 2)/JavaGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/JavaGame.PPM, rect.getHeight() / 2/JavaGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        //pipe bodies/fixtures
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect  = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2) /JavaGame.PPM, (rect.getY() + rect.getHeight() / 2)/JavaGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/JavaGame.PPM, rect.getHeight() / 2/JavaGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = JavaGame.OBJECT_BIT;
            body.createFixture(fdef);
        }
        //brick bodies/fixtures
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new Brick(screen,object);

        }
        //coin bodies/fixtures
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            new Coin(screen, object);

        }
        //create all goombas
        goombas = new com.badlogic.gdx.utils.Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect  = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / JavaGame.PPM, rect.getY() / JavaGame.PPM));

        }
        //create
        turtles = new com.badlogic.gdx.utils.Array<Turtle>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX() / JavaGame.PPM, rect.getY() / JavaGame.PPM));
        }

        //create pole
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            new Pole(screen, object);
        }

    }
    public com.badlogic.gdx.utils.Array<Goomba> getGoombas() {
        return goombas;
    }
    public com.badlogic.gdx.utils.Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }



}
