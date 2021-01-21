package com.mmog.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Imposter extends Player{

	public Imposter(Sprite sprite, int playerID) {
		super(sprite, playerID);
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}
}
