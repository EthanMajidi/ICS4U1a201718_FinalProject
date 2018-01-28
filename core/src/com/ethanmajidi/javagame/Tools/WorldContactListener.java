package com.ethanmajidi.javagame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ethanmajidi.javagame.Items.Item;
import com.ethanmajidi.javagame.JavaGame;
import com.ethanmajidi.javagame.Sprites.Enemy;
import com.ethanmajidi.javagame.Sprites.InteractiveTileObject;
import com.ethanmajidi.javagame.Sprites.Java;
import com.ethanmajidi.javagame.Sprites.Pole;

import java.util.jar.JarEntry;

/**
 * Created by EthanMajidi on 2018-01-23.
 */

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch (cDef){
            case JavaGame.PLAYER_HEAD_BIT | JavaGame.BRICK_BIT:
            case JavaGame.PLAYER_HEAD_BIT | JavaGame.COIN_BIT:
                if(fixA.getFilterData().categoryBits == JavaGame.PLAYER_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Java) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Java) fixB.getUserData());
                break;
            case JavaGame.ENEMY_HEAD_BIT | JavaGame.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == JavaGame.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((Java) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Java) fixA.getUserData());
                break;
            case JavaGame.ENEMY_BIT | JavaGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == JavaGame.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case JavaGame.PLAYER_BIT | JavaGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == JavaGame.PLAYER_BIT)
                    ((Java) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Java) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case JavaGame.ENEMY_BIT | JavaGame.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).onEnemyHit((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).onEnemyHit((Enemy)fixA.getUserData());
                break;
            case JavaGame.ITEM_BIT | JavaGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == JavaGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case JavaGame.ITEM_BIT | JavaGame.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == JavaGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Java) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Java) fixA.getUserData());
                break;

        }
    }
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case JavaGame.PLAYER_BIT | JavaGame.POLE_BIT:
                if (fixA.getFilterData().categoryBits == JavaGame.PLAYER_BIT)
                    ((Pole) fixA.getUserData()).onHeadHit((Java) fixB.getUserData());
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
