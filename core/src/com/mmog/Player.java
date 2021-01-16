package com.mmog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    
    private TextureAtlas walkRightAtlas;
    private TextureAtlas walkLeftAtlas;
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;
    private Texture idle;
    private float elapsedTime = 0;
    private Sprite sprite;
    private float x;
    private float y;
    private boolean isFlipped;
    private boolean isDead;
    private boolean isIdle;
    private int playerID;
    
    
    public Player(int playerID)
    {
		idle = new Texture(Gdx.files.internal("idle.png"));
        walkRightAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
        walkLeftAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
        isFlipped = false;
        walkRight = new Animation(1/15f, walkRightAtlas.getRegions());
        walkLeft = new Animation(1/15f, walkLeftAtlas.getRegions());
        sprite = new Sprite(idle);
        isIdle = false;
        this.setPlayerID(playerID);
        
        x=0;
        y=0;
        
        for (float i = 0; i < 1; i += 0.01f)
        {
        	TextureRegion tr = walkLeft.getKeyFrame(i,true);
        	
        	if(!tr.isFlipX())
        	{
        	 tr.flip(true, false);
        	}
        	
        }
    }
    
    public void draw(SpriteBatch batch)
    {
    	elapsedTime += Gdx.graphics.getDeltaTime(); 
    	if (isFlipped && !isIdle)
    	{
    		batch.draw(walkLeft.getKeyFrame(elapsedTime, true), x, y);
    	}
    	else if (!isFlipped && !isIdle)
    	{
        	batch.draw(walkRight.getKeyFrame(elapsedTime, true), x, y);
    	}
    	
    	if (isIdle)
    	{
    		if(isFlipped && !sprite.isFlipX() || !isFlipped && sprite.isFlipX())
        	{
        		sprite.flip(true, false);
        	}
    		sprite.setX(x);
        	sprite.setY(y);
        	sprite.draw(batch);
    	}
    }
    public void render() throws Exception
    {
    	boolean playerMoved = false;
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
        	x -= 5;
        	isFlipped = true;
        	isIdle=false;
        	playerMoved = true;
        }
        
        else if(Gdx.input.isKeyPressed(Input.Keys.D)){
        	x+=5;
        	isFlipped = false;
        	isIdle=false;
        	playerMoved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
        	y += 5;
        	isIdle = false;
        	playerMoved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
        	y-=5;      
        	isIdle = false;
        	playerMoved = true;
        }
        
        if (!isIdle && !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.D) )
        {               	
        	isIdle = true;
        	playerMoved = true;
        }
        
        if(playerMoved) {
        	Client.sendUpdate(x, y, isFlipped, isDead, isIdle);
        }
    }

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public void setAll(float x, float y, boolean isFlipped, boolean isDead, boolean isIdle) {
		this.x = x;
		this.y = y;
		this.isFlipped = isFlipped;
		this.isDead = isDead;
		this.isIdle = isIdle;
	}
}
