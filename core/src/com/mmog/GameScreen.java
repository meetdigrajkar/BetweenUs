package com.mmog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends AbstractScreen{

	//public static Player player;
	private static HashMap<Integer,Player> connectedPlayers;
	OrthographicCamera cam;
	float width,height;
	TextButtonStyle tbs;
	BitmapFont font;
	private Label tasksLabel,tasksTitleLabel;
	private Viewport vp;

	Table table;

	private OrthogonalTiledMapRenderer r;
	private TiledMap map;

	public GameScreen() {
		super();
	}

	public static void addPlayer(int playerID) {
		connectedPlayers.put(playerID, null);
	}

	public static void updateConnectedClient(int playerID, float x, float y, boolean isFlipped, boolean isDead, boolean isIdle, String playerName) {
		if(connectedPlayers.get(playerID) == null) {
			GameScreen.addPlayer(playerID);
			return;
		}
		connectedPlayers.get(playerID).setAll(x, y, isFlipped, isDead, isIdle, playerName);
	}

	private ArrayList<Player> getYBasedSortedPlayers() {
		ArrayList<Player> allPlayers = new ArrayList();
		allPlayers.add(MainScreen.player);


		for (int key: connectedPlayers.keySet())
		{
			if (connectedPlayers.get(key) == null)
			{                
				connectedPlayers.replace(key, new Player(new Sprite(new Texture("idle.png")),key));
			}

			allPlayers.add(connectedPlayers.get(key));
		}

		//Render Based On Y-Axis to avoid poor sprite overlap.


		Collections.sort(allPlayers, new Comparator<Player>() {
			@Override
			public int compare(Player arg0, Player arg1) {
				// TODO Auto-generated method stub
				return Float.compare(arg1.getY(), arg0.getY());

			}
		});
		return allPlayers;
	}

	@Override
	public void show() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(width, height);
		cam.zoom = 0.46f;
		vp = new FitViewport(1920, 1080,cam);
		this.setViewport(vp);

		map = new TmxMapLoader().load("MapAreas/mapfiles/map.tmx");
		r = new OrthogonalTiledMapRenderer(map);
		
		MainScreen.player.setCollisionLayer((TiledMapTileLayer) map.getLayers().get(0));
		MainScreen.player.setPosition(35 * MainScreen.player.getCollisionLayer().getTileWidth(), (MainScreen.player.getCollisionLayer().getHeight() - 10) * MainScreen.player.getCollisionLayer().getTileHeight());
		
		cam.setToOrtho(false);
		cam.position.set(MainScreen.player.getX() + (MainScreen.player.getWidth() * 2), MainScreen.player.getY() + (MainScreen.player.getHeight()), 0);
		cam.update();

		r.getBatch().setProjectionMatrix(cam.combined);
	}


	@Override
	public void buildStage() {
		connectedPlayers = new HashMap<Integer,Player>();

		Gdx.input.setInputProcessor(this);

		table = new Table();
		table.setFillParent(true);

		//label skin
		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.WHITE);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		//player's tasks label
		LabelStyle tasksStyle = new LabelStyle(new BitmapFont(),Color.GOLD);
		tasksLabel = new Label(MainScreen.player.tasksToString(), tasksStyle);

		//table.add(tasksTitleLabel);
		table.add(tasksLabel);
		addActor(table);

	}

	public void update(float delta) {
		cam.position.set(MainScreen.player.getX() + (MainScreen.player.getWidth() /2), MainScreen.player.getY() + (MainScreen.player.getHeight()/2), 0);
		cam.unproject(new Vector3(MainScreen.player.getX(), MainScreen.player.getY(), 0));
		this.getCamera().update();
		r.getBatch().setProjectionMatrix(cam.combined);    
	}

	@Override
	public void render(float delta) {
		//clear the previous screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tasksLabel.setPosition(MainScreen.player.getX() - Gdx.graphics.getWidth()/2 +50, MainScreen.player.getY() + Gdx.graphics.getHeight()/3 +150);

		//updates the camera position
		update(delta);
		r.setView(cam);
		r.render();

		r.getBatch().begin();

		ArrayList<Player> allPlayers = getYBasedSortedPlayers();
		try {
			MainScreen.player.render(Gdx.graphics.getDeltaTime());
			//MainScreen.player.draw(r.getBatch());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Player p : allPlayers)
		{
			p.draw(r.getBatch());
		}
		
		r.getBatch().end();
		
		draw();

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
		r.getBatch().dispose();
	}

}