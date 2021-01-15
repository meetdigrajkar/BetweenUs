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
    
    
    
    public Player()
    {
		idle = new Texture(Gdx.files.internal("idle.png"));
        walkRightAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
        walkLeftAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
        isFlipped = false;
        walkRight = new Animation(1/15f, walkRightAtlas.getRegions());
        walkLeft = new Animation(1/15f, walkLeftAtlas.getRegions());
        sprite = new Sprite(idle);
        
        
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
    
    
    public void render(SpriteBatch batch)
    {
        elapsedTime += Gdx.graphics.getDeltaTime();
        
        
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
        	x -= 5;
        	batch.draw(walkLeft.getKeyFrame(elapsedTime, true), x, y);
        	isFlipped = true;
        }
        
        else if(Gdx.input.isKeyPressed(Input.Keys.D)){
        	x+=5;
        	batch.draw(walkRight.getKeyFrame(elapsedTime, true), x, y);
        	isFlipped = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
        	y += 5;
        	if (isFlipped)
        	{
        		batch.draw(walkLeft.getKeyFrame(elapsedTime, true), x, y);
        	}
        	else
        	{
        		batch.draw(walkRight.getKeyFrame(elapsedTime, true), x, y);
        	}
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
        	y-=5;
        	if (isFlipped)
        	{
        		batch.draw(walkLeft.getKeyFrame(elapsedTime, true), x, y);
        	}
        	else
        	{
        		batch.draw(walkRight.getKeyFrame(elapsedTime, true), x, y);
        	}
        }
        
        if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.D) )
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
}
