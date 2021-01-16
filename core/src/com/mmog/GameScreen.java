package com.mmog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends AbstractScreen{

	private Player player;
	private static HashMap<Integer,Player> connectedPlayers;
	private SpriteBatch spritebatch;
	
	public GameScreen(int level) {
		super();
	}
	
	public GameScreen() {
		super();
		connectedPlayers = new HashMap<Integer,Player>();
	}
	
	public static void addPlayer(Player player) {
		connectedPlayers.put(player.getPlayerID(), player);
	}
	
	public static void updateConnectedClient(int playerID, float x, float y, boolean isFlipped, boolean isDead, boolean isIdle) {
		connectedPlayers.get(playerID).setAll(x, y, isFlipped, isDead, isIdle);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void buildStage() {
		// TODO Auto-generated method stub
		spritebatch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// TODO Auto-generated method stub
		spritebatch.begin();
		try {
			player.render();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int key: connectedPlayers.keySet())
		{
			connectedPlayers.get(key).draw(spritebatch);
		}
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