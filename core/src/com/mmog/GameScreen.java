package com.mmog;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends AbstractScreen{

	private Player player;
	private SpriteBatch spritebatch;
	
	public GameScreen(int level) {
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void buildStage() {
		// TODO Auto-generated method stub
		spritebatch = new SpriteBatch();
		player = new Player();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		spritebatch.begin();
		player.render(spritebatch);
		spritebatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		spritebatch.dispose();
	}
	
}