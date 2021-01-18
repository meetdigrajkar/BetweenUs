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
	private SpriteBatch spritebatch;
	OrthographicCamera cam;
	float width,height;
	ShapeRenderer sr;
	private Texture btn;
	TextButtonStyle tbs;
	BitmapFont font;
	boolean startedTask,isOverlapingATS;
	private Label playerNameLabel,tasksLabel,tasksTitleLabel;
	private Viewport vp;

	Texture mapTexture,adminStationTexture;

	Sprite mapTextureSprite,adminStationTextureSprite;

	Rectangle asRec;
	Rectangle playerRec;

	Table table;

	public GameScreen() {
		super();
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
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(width, height);
		vp = new FitViewport(1920, 1080,cam);
		this.setViewport(vp);

		cam.setToOrtho(false);
		cam.position.set(MainScreen.player.getX() + (MainScreen.player.getWidth() * 2), MainScreen.player.getY() + (MainScreen.player.getHeight()), 0);
		cam.update();

		spritebatch = new SpriteBatch();
		spritebatch.setProjectionMatrix(cam.combined);
	}


	@Override
	public void buildStage() {
		connectedPlayers = new HashMap<Integer,Player>();

		Gdx.input.setInputProcessor(this);
		
		table = new Table();
		table.setFillParent(true);

		//player name label
		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.WHITE);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		playerNameLabel = new Label(MainScreen.player.getPlayerName(), ls );
		addActor(playerNameLabel);

		//player's tasks label
		LabelStyle tasksStyle = new LabelStyle(new BitmapFont(),Color.GOLD);
		tasksLabel = new Label(MainScreen.player.tasksToString(), tasksStyle);
		
		//table.add(tasksTitleLabel);
		table.add(tasksLabel);
		addActor(table);
		
		//map sprite
		mapTexture = new Texture(Gdx.files.internal("map.jpg"));
		mapTextureSprite = new Sprite(mapTexture);
		mapTextureSprite.setPosition(100, 100);
		mapTextureSprite.scale(2);

		//admin task station sprite
		adminStationTexture = new Texture(Gdx.files.internal("TaskStations/adminTaskStation.png"));
		adminStationTextureSprite = new Sprite(adminStationTexture);
		adminStationTextureSprite.setPosition(1430, 195);

		//COLLISION DETECTION FOR ALL THE STATIONS GO HERE, IN THE FORM OF A RECTANGLE
		asRec = new Rectangle(adminStationTextureSprite.getX(),adminStationTextureSprite.getY(),adminStationTextureSprite.getWidth(),adminStationTextureSprite.getHeight());
	}

	public void update(float delta) {
		cam.position.set(MainScreen.player.getX() + (MainScreen.player.getWidth() /2), MainScreen.player.getY() + (MainScreen.player.getHeight()/2), 0);
		cam.unproject(new Vector3(MainScreen.player.getX(), MainScreen.player.getY(), 0));
		this.getCamera().update();
		spritebatch.setProjectionMatrix(cam.combined);    
	}

	@Override
	public void render(float delta) {
		//clear the previous screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//set position of the players name label to follow player
		playerNameLabel.setPosition(MainScreen.player.getX() + MainScreen.player.getWidth()/2 - MainScreen.player.getPlayerName().length() * 2 - 2, MainScreen.player.getY() + MainScreen.player.getHeight() + 8);
		
		tasksLabel.setPosition(MainScreen.player.getX() - Gdx.graphics.getWidth()/3, MainScreen.player.getY() + Gdx.graphics.getHeight()/3);
		
		//updates the camera position
		update(delta);

		spritebatch.begin();
		mapTextureSprite.draw(spritebatch);
		adminStationTextureSprite.draw(spritebatch);
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
		spritebatch.end();

		draw();
		
		playerRec = MainScreen.player.sprite.getBoundingRectangle();
		isOverlapingATS = playerRec.overlaps(asRec);

		//if the player is overlaping with the admin station and presses space bar and has an admin task, then start the task
		if(isOverlapingATS && Gdx.input.isKeyPressed(Keys.SPACE) && MainScreen.player.hasTask("Admin Task") && !(MainScreen.player.isTaskCompleted("Admin Task"))) {
			System.out.println("Admin Task Started!");
			System.out.println("Player Name: " + MainScreen.player.getPlayerName());

			ScreenManager.getInstance().showScreen(ScreenEnum.ADMIN_TASK);
		}
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