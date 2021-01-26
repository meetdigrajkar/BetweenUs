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

	boolean everyoneReady;
	Table table;
	BitmapFont font;

	public LobbyScreen() {
		super();
	}

	//create all the players in the connected players array in the client
	public void createPlayers() {
		for (Entry<Integer, Player> e : Client.getConnectedPlayers().entrySet()) {
			if(e.getValue() == null) {
				Client.getConnectedPlayers().put(e.getKey(), new Player(e.getKey()));
				e.getValue().setPlayerName(Client.getConnectedPlayersNames().get(e.getKey()));
			}
		}
	}


	@Override
	public void buildStage() {
		table = new Table();
		table.setFillParent(true);

		font = new BitmapFont();
		TextButtonStyle tbs = new TextButtonStyle();
		//font.getData().scale(2);
		font.setColor(Color.RED);
		tbs.font = font;

		button = new TextButton("Join", tbs);
		table.add(button);

		//button listener for the ready button to be ready to join game and be assigned a role
		button.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!Client.getPlayer().readyToPlay) {
					System.out.println("READY!");
					Client.getPlayer().readyToPlay = true;

					try {
						
							readyToStart();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(Client.getPlayer().readyToPlay) {
					System.out.println("NOT READY!");
					Client.getPlayer().readyToPlay = false;
				}

			}
		});
		
		addActor(table);
	}

	public void readyToStart() throws IOException {
		Client.sendPlayerIsReady();
	}


	public void sendAssignRoleAndStart() {
		Client.sendPlayerRoleAssign();
		System.out.println("NOW ENTERING GAME!");
		
		ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
	}


	@Override
	public void show() {
		createPlayers();
		cameraSetup();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//create all the players
		createPlayers();
		update(delta);

		//map renderer
		r.setView(cam);
		r.render();

		everyoneReady = false;


		for(Entry<Integer, Player> e: Client.getConnectedPlayers().entrySet()) {
			everyoneReady &= e.getValue().readyToPlay;
		}

		if(everyoneReady) { 
			sendAssignRoleAndStart(); 
		}

		r.getBatch().begin();
		//draw all the players to the lobby
		for (Entry<Integer, Player> e : Client.getConnectedPlayers().entrySet())
		{
			Player p = e.getValue();
			if(p != null) {
				p.draw(r.getBatch());
			}
		}
		r.getBatch().end();

		button.setPosition(Client.getPlayer().getX() + 275,Client.getPlayer().getY() + 185);

		//allow player movement
		try {
			Gdx.input.setInputProcessor(this);
			Client.getPlayer().render(Gdx.graphics.getDeltaTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(Client.getPlayer().getIsIdle())
		{
			font.setColor(Color.GREEN);
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

		map = new TmxMapLoader().load("MapAreas/mapfiles/lobby.tmx");
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
		r.getBatch().dispose();
	}
}
