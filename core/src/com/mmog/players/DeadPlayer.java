package com.mmog.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class DeadPlayer extends Sprite{
	String name;
	private Rectangle deadPlayerRec;
	
	public DeadPlayer(int x, int y) {
		super(new Sprite(new Texture("Among Us - Player Base/Individual Sprites/Death/Dead0033.png")));
		setSize(20,25);
		setPosition(x,y);
		deadPlayerRec = new Rectangle(getX(),getY(),32,50);
	}
	
	public Rectangle getDeadPlayerRec() {
		return deadPlayerRec;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void draw(Batch batch)
	{
		super.draw(batch);
	}
}
