package com.mmog.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;
import com.mmog.players.Player;

public class LobbyScreen extends AbstractScreen{

	//tiled map
	private TiledMap map;
	private OrthogonalTiledMapRenderer r;

	//Camera and viewport
	private Viewport vp;
	OrthographicCamera cam;
	float width,height;
	TextButton button;

	Table table;
	BitmapFont font;
	public static ArrayList<Sprite> hats;
	private int hatID;
	private int totalHats = 50;

	public LobbyScreen() {
		super();
	}

	@Override
	public void buildStage() {
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	@Override
	public void show() {
		cameraSetup();
		Client.getPlayer().speed = 2f;
		hats = new ArrayList<Sprite>();
		hatID = -1;
		initHats();
	}

	public void initHats() {
		for(int i = 2; i <= totalHats; i++) {
			Sprite hat = null;

			if(i < 10) {
				hat = new Sprite(new Texture("Hats/hats000" + i + ".png" ));
			}
			else {
				hat = new Sprite(new Texture("Hats/hats00" + i + ".png" ));
			}

			if(hat != null) {
				hats.add(hat);
			}
		}
	}

	public void detectingKeyPresses() {
		//starting the game.
		if(Gdx.input.isKeyPressed(Keys.ENTER) && Client.getPlayer().isHost) {
			try {
				Client.sendStartCommand();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//change hat command
		else if(Gdx.input.isKeyJustPressed(Keys.L)) {
			hatID += 1;
			if(hatID == totalHats - 1) {
				hatID = 0;
			}
			//toggle through a list of hats everytime the hat command is triggered
			Client.getPlayer().setHat(hats.get(hatID), hatID);
		}
		else if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			System.out.println("Back To Main Screen!");
			Client.removeClient();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//create all the players
		update(delta);

		//map renderer
		r.setView(cam);
		r.render();

		detectingKeyPresses();

		r.getBatch().begin();
		//if the player role has updated, replace the player with either crew member or imposter
		Client.replacePlayerByRole(r.getBatch());
		r.getBatch().end();

		r.getBatch().begin();

		Client.getPlayer().draw(r.getBatch());

		//draw all the players to the lobby
		for (Player p : Client.getPlayers())
		{
			if(p.getPlayerID() != -1) {
				p.draw(r.getBatch());
			}
		}
		r.getBatch().setColor(Color.WHITE);
		r.getBatch().end();

		//allow player movement
		try {
			Gdx.input.setInputProcessor(this);
			if(Client.getPlayer().role.equals("none")) {
				Client.getPlayer().render(Gdx.graphics.getDeltaTime());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		draw();
	}

	public void cameraSetup() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(width, height);
		cam.zoom = 0.5f;
		vp = new FitViewport(1920, 1080,cam);
		this.setViewport(vp);

		map = new TmxMapLoader().load("map/lobby.tmx");
		r = new OrthogonalTiledMapRenderer(map);

		Client.getPlayer().setCollisionLayer((TiledMapTileLayer) map.getLayers().get(0));
		Client.getPlayer().setPosition(38 * Client.getPlayer().getCollisionLayer().getTileWidth(), (Client.getPlayer().getCollisionLayer().getHeight() - 30) * Client.getPlayer().getCollisionLayer().getTileHeight());

		cam.setToOrtho(false);
		cam.position.set(Client.getPlayer().getX() + (Client.getPlayer().getWidth() * 2), Client.getPlayer().getY() + (Client.getPlayer().getHeight()), 0);
		cam.update();

		r.getBatch().setProjectionMatrix(cam.combined);
	}

	public void update(float delta) {
		cam.position.set(Client.getPlayer().getX() + (Client.getPlayer().getWidth() * 2), Client.getPlayer().getY() + (Client.getPlayer().getHeight()), 0);
		cam.unproject(new Vector3(Client.getPlayer().getX(), Client.getPlayer().getY(), 0));
		this.getCamera().update();
		r.getBatch().setProjectionMatrix(cam.combined);
	}

	public void hide() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		//r.getBatch().dispose();
		//this.dispose();
	}
}
