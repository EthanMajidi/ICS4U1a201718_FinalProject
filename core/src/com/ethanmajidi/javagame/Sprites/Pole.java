package com.ethanmajidi.javagame.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ethanmajidi.javagame.Items.ItemDef;
import com.ethanmajidi.javagame.Items.Mushroom;
import com.ethanmajidi.javagame.JavaGame;
import com.ethanmajidi.javagame.Scenes.Hud;
import com.ethanmajidi.javagame.Screens.PlayScreen;

/**
 * Created by MerZh on 2018-01-27.
 */
public class Pole extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_POLE = 1024;
    private boolean isActive;

    public Pole(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("Custom_tile");
        fixture.setUserData(this);
        setCategoryFilter(JavaGame.POLE_BIT);
        isActive = true;
    }

    @Override
    public void onHeadHit(Java java) {
        Gdx.app.log("Pole", "Collision");
        getCell().setTile(tileSet.getTile(JavaGame.POLE_BIT));
        }
    }
