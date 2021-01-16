package com.mmog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends AbstractScreen{

	private Player player;
	private SpriteBatch spritebatch;
	
	public GameScreen(int level) {
		super();
	}
	
	public GameScreen() {
		super();
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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