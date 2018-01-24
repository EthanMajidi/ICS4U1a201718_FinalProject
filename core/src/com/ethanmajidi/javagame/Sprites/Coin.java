package com.ethanmajidi.javagame.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ethanmajidi.javagame.JavaGame;
import com.ethanmajidi.javagame.Scenes.Hud;
import com.ethanmajidi.javagame.Screens.PlayScreen;

/**
 * Created by EthanMajidi on 2018-01-21.
 */

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 2;
    private boolean isActive;
    public Coin(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("Custom_tile");
        fixture.setUserData(this);
        setCategoryFilter(JavaGame.COIN_BIT);
        isActive = true;
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        if (isActive) {
            Hud.addScore(100);
            isActive = false;

        }

        /**
        if(getCell().getTile().getId() == BLANK_COIN)
        {
            JavaGame.manager.get("audio/sounds/BumpMusic.wav", Sound.class).play();
        }
        else
        {
            JavaGame.manager.get("audio/sounds/CoinMusic.wav", Sound.class).play();
        */
    }
}