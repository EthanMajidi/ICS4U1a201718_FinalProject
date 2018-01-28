package com.ethanmajidi.javagame.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ethanmajidi.javagame.JavaGame;
import com.ethanmajidi.javagame.Screens.PlayScreen;

/**
 * Created by EthanMajidi on 2017-12-20.
 */

public class Java extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, WON};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion playerStand;
    private Animation <TextureRegion> playerRun;
    private TextureRegion playerJump;
    private TextureRegion playerDead;
    private TextureRegion playerWon;
    private TextureRegion bigPlayerStand;
    private TextureRegion bigPlayerJump;
    private Animation <TextureRegion> bigPlayerRun;
    private Animation <TextureRegion> growPlayer;

    private float stateTimer;
    private boolean runningRight;
    public boolean playerIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigPlayer;
    private boolean timeToRedefinePlayer;
    private boolean playerIsDead;
    private boolean playerHasWon;

    public Java(PlayScreen screen){
        //initialize default values
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        playerRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        bigPlayerRun = new Animation<TextureRegion>(0.1f, frames);
        //clears frames for next animation sequence
        frames.clear();

        //get animation for big player grow
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32));
        growPlayer = new Animation<TextureRegion>(0.2f,frames);

        playerJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80,0,16,16);
        bigPlayerJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80,0,16,32);

        playerStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigPlayerStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32);

        //create dead player texture region
        playerDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        //create player won texture region
        playerWon = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        defineMario();
        setBounds(0, 0, 16 / JavaGame.PPM, 16 / JavaGame.PPM);
        setRegion(playerStand);

    }


    public void update(float dt){
        if(playerIsBig)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 -6 /JavaGame.PPM);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        if(timeToDefineBigPlayer)
            defineBigPlayer();
        if(timeToRedefinePlayer)
            redefinePlayer();
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case DEAD:
                region = playerDead;
                break;
            case WON:
                region = playerWon;
                break;
            case GROWING:
                region = growPlayer.getKeyFrame(stateTimer);
                if(growPlayer.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = playerIsBig ? bigPlayerJump : playerJump;
                break;
            case RUNNING:
                region = playerIsBig ? bigPlayerRun.getKeyFrame(stateTimer, true) : playerRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerIsBig ? bigPlayerStand : playerStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;

    }

    public State getState(){
        if(playerIsDead)
            return State.DEAD;
        if(playerHasWon)
            return State.WON;
        else if(runGrowAnimation)
            return State.GROWING;
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }
    public void grow(){
        runGrowAnimation = true;
        playerIsBig = true;
        timeToDefineBigPlayer = true;
        setBounds(getX(), getY(), getWidth(), getHeight() *2);

    }

    public boolean isDead() {
        return playerIsDead;
    }

    public boolean hasWon() {
        return playerHasWon;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public boolean isBig(){
        return playerIsBig;
    }

    public void hit(Enemy enemy) {
        if(enemy instanceof Turtle && ((Turtle)enemy).getCurrentState() == Turtle.State.STANDING_SHELL){
            ((Turtle)enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
        }
        else {
            if (playerIsBig) {
                playerIsBig = false;
                timeToRedefinePlayer = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            } else {
                playerIsDead = true;
                Filter filter = new Filter();
                filter.maskBits = JavaGame.NOTHING_BIT;
                for (Fixture fixture : b2body.getFixtureList())
                    fixture.setFilterData(filter);
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            }
        }



    }


    public void redefinePlayer(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ JavaGame.PPM);
        fdef.filter.categoryBits = JavaGame.PLAYER_BIT;
        fdef.filter.maskBits = JavaGame.GROUND_BIT | JavaGame.COIN_BIT | JavaGame.BRICK_BIT | JavaGame.ENEMY_BIT | JavaGame.OBJECT_BIT | JavaGame.ENEMY_HEAD_BIT | JavaGame.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/JavaGame.PPM, 6/JavaGame.PPM), new Vector2(2/JavaGame.PPM, 6/JavaGame.PPM));
        fdef.filter.categoryBits = JavaGame.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToRedefinePlayer = false;

    }

    public void defineBigPlayer(){
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0,10/JavaGame.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ JavaGame.PPM);
        fdef.filter.categoryBits = JavaGame.PLAYER_BIT;
        fdef.filter.maskBits = JavaGame.GROUND_BIT | JavaGame.COIN_BIT | JavaGame.BRICK_BIT | JavaGame.ENEMY_BIT | JavaGame.OBJECT_BIT | JavaGame.ENEMY_HEAD_BIT | JavaGame.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0,-14/JavaGame.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/JavaGame.PPM, 6/JavaGame.PPM), new Vector2(2/JavaGame.PPM, 6/JavaGame.PPM));
        fdef.filter.categoryBits = JavaGame.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigPlayer = false;
    }


    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 /JavaGame.PPM, 32/JavaGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ JavaGame.PPM);
        fdef.filter.categoryBits = JavaGame.PLAYER_BIT;
        fdef.filter.maskBits = JavaGame.GROUND_BIT | JavaGame.COIN_BIT | JavaGame.BRICK_BIT | JavaGame.ENEMY_BIT | JavaGame.OBJECT_BIT | JavaGame.ENEMY_HEAD_BIT | JavaGame.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/JavaGame.PPM, 6/JavaGame.PPM), new Vector2(2/JavaGame.PPM, 6/JavaGame.PPM));
        fdef.filter.categoryBits = JavaGame.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

}