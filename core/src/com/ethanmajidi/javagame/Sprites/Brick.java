package com.ethanmajidi.javagame.Sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.ethanmajidi.javagame.JavaGame;

/**
 * Created by EthanMajidi on 2018-01-21.
 */

public class Brick extends InteractiveTileObject {
    public Brick(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(JavaGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");
        setCategoryFilter(JavaGame.DESTRYOYED_BIT);
        getCell().setTile(null);
    }
}
