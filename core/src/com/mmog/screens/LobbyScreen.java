package com.mmog.screens;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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
	//make fonts here
	BitmapFont font = new BitmapFont(Gdx.files.internal("UI/textf.fnt"));
	Label joinRoom;

	public LobbyScreen() {
		super();
	}

	@Override
	public void buildStage() {
		LabelStyle lsC = new LabelStyle(font, Color.GREEN);
		joinRoom = new Label("Join", lsC);
		joinRoom.setVisible(false);
		Gdx.input.setInputProcessor(this);
		
		//join room button listener
		joinRoom.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Starting the Game...");
				
				try {
					Client.sendStartCommand();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		addActor(joinRoom);
	}

	@Override
	public void show() {
		cameraSetup();
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
		
		if(Client.getPlayer().isHost) {
			joinRoom.setVisible(true);
			joinRoom.setPosition(Client.getPlayer().getX() + 150, Client.getPlayer().getY() + 150);
		}

		//if the player role has updated, replace the player with either crew member or imposter
		Client.replacePlayerByRole();

		r.getBatch().begin();

		Client.getPlayer().draw(r.getBatch());

		//draw all the players to the lobby
		for (Player p : Client.getPlayers())
		{
			if(p.getPlayerID() != -10) {
				p.draw(r.getBatch());
			}
		}

		r.getBatch().end();

		//allow player movement
		try {
			Client.getPlayer().render(Gdx.graphics.getDeltaTime());
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
		cam.zoom = 0.30f;
		vp = new FitViewport(1920, 1080,cam);
		this.setViewport(vp);

		map = new TmxMapLoader().load(Gdx.files.internal("MapAreas/mapfiles/lobby.tmx").toString());
		r = new OrthogonalTiledMapRenderer(map);

		Client.getPlayer().setCollisionLayer((TiledMapTileLayer) map.getLayers().get(0));
		Client.getPlayer().setPosition(12 * Client.getPlayer().getCollisionLayer().getTileWidth(), (Client.getPlayer().getCollisionLayer().getHeight() - 9) * Client.getPlayer().getCollisionLayer().getTileHeight());

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


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		//r.getBatch().dispose();
		//this.dispose();
	}
}
