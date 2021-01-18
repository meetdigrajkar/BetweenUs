package com.mmog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
	private SpriteBatch spritebatch;
	OrthographicCamera cam;
	float width,height;
	ShapeRenderer sr;
	private Texture btn;
	TextButtonStyle tbs;
	BitmapFont font;
	boolean startedTask,isOverlaping;
	private Label playerNameLabel;
	TextButton adminTask;
	private Viewport vp;

	Texture mapTexture,playerTexture;

	Sprite mapTextureSprite,playerSprite;

	Rectangle asRec;
	Rectangle playerRec;

	public GameScreen(int level) {
		super();
	}

	public GameScreen() {
		super();
		connectedPlayers = new HashMap<Integer,Player>();
		isOverlaping = false;
	}

	public static void addPlayer(int playerID) {
		connectedPlayers.put(playerID, null);
	}

	public static void updateConnectedClient(int playerID, float x, float y, boolean isFlipped, boolean isDead, boolean isIdle) {
		if(connectedPlayers.get(playerID) == null) {
			GameScreen.addPlayer(playerID);
			return;
		}
		connectedPlayers.get(playerID).setAll(x, y, isFlipped, isDead, isIdle);
	}

	private ArrayList<Player> getYBasedSortedPlayers() {
		ArrayList<Player> allPlayers = new ArrayList();
		allPlayers.add(MainScreen.player);


		for (int key: connectedPlayers.keySet())
		{
			if (connectedPlayers.get(key) == null)
			{                
				connectedPlayers.replace(key, new Player(key));
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
		//player = new Player(-1);

		//add the tasks below to the player at the begining of the game.
		MainScreen.player.getTasks().add(new AdminTask());

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(width, height);
		vp = new FitViewport(200, 200,cam);
		this.setViewport(vp);

		cam.setToOrtho(false);
		cam.position.set(MainScreen.player.getX() + (MainScreen.player.getWidth() * 2), MainScreen.player.getY() + (MainScreen.player.getHeight()), 0);
		cam.update();

		spritebatch = new SpriteBatch();
		spritebatch.setProjectionMatrix(cam.combined);
	}


	@Override
	public void buildStage() {
		Gdx.input.setInputProcessor(this);
		playerTexture = new Texture(Gdx.files.internal("idle.png"));
		playerSprite = new Sprite(playerTexture);
		sr = new ShapeRenderer();

		//ADMIN TASK
		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.WHITE);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		playerNameLabel = new Label(MainScreen.player.getPlayerName(), ls );
		addActor(playerNameLabel);

		tbs = new TextButtonStyle();
		font = new BitmapFont();
		tbs.font = font;
		adminTask = new TextButton("Start Admin Task", tbs);
		startedTask = false;
		adminTask.setPosition(100, 100, Align.center);
		addActor(adminTask);

		//admin task station
		mapTexture = new Texture(Gdx.files.internal("map.jpg"));
		mapTextureSprite = new Sprite(mapTexture);
		mapTextureSprite.setPosition(100, 100);
		mapTextureSprite.scale(2);

		//collision detection bs
		asRec = new Rectangle(mapTextureSprite.getX(),mapTextureSprite.getY(),mapTextureSprite.getWidth(),mapTextureSprite.getHeight());
	}

	public void update(float delta) {
		cam.position.set(MainScreen.player.getX() + (MainScreen.player.getWidth() /2), MainScreen.player.getY() + (MainScreen.player.getHeight()/2), 0);
		cam.unproject(new Vector3(MainScreen.player.getX(), MainScreen.player.getY(), 0));
		this.getCamera().update();
		spritebatch.setProjectionMatrix(cam.combined);    
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		playerNameLabel.setPosition(MainScreen.player.getX() + MainScreen.player.getWidth()/2 - MainScreen.player.getPlayerName().length() * 2 - 2, MainScreen.player.getY() + MainScreen.player.getHeight() + 8);

		update(delta);

		spritebatch.begin();
		mapTextureSprite.draw(spritebatch);
		try {
			MainScreen.player.render();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Player> allPlayers = getYBasedSortedPlayers();

		for (Player p : allPlayers)
		{
			p.draw(spritebatch);
		}
		
		draw();

		playerRec = playerSprite.getBoundingRectangle();
		
		isOverlaping = playerRec.overlaps(asRec);
		//if the player is overlaping with the admin station and presses space bar, start task
		if(isOverlaping) {
			System.out.println("Admin Task Started!");
			//find the task with the name admin task
			for(Task t: MainScreen.player.getTasks()) {
				if(t.getTaskName().equals("Admin Task")) {
					//set screen to the Admin Task Screen
					MainScreen.player.setCurrentTask(t);
					ScreenManager.getInstance().showScreen(ScreenEnum.ADMIN_TASK);
				}
			}
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