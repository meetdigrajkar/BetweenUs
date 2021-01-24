package com.mmog.screens;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;
import com.mmog.players.Player;
import com.mmog.tasks.AdminTask;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.mmog.tasks.*;

public class GameScreen extends AbstractScreen{

	//public static Player player;
	private static HashMap<Integer,Player> connectedPlayers;
	OrthographicCamera cam;
	float width,height;
	TextButtonStyle tbs;
	BitmapFont font;
	private Viewport vp;

	Table table;

	private OrthogonalTiledMapRenderer r;
	private TiledMap map;

	Task task;

	//lights
	RayHandler rayhandler;
	World world;

	BodyDef bodydef;
	Body body;
	Light light;


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
		cam.zoom = 0.36f;
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

		//light and box stuff
		this.world = new World(new Vector2(0,0),false);

		rayhandler = new RayHandler(world);
		rayhandler.setAmbientLight(0, 0, 0, 0.1f);

		bodydef = new BodyDef();
		bodydef.position.set(MainScreen.player.getX(), MainScreen.player.getY());
		bodydef.type = BodyType.DynamicBody;
		bodydef.linearVelocity.set(1f, 1f);

		body = world.createBody(bodydef);
		body.setActive(true);
		MainScreen.player.setBody(body);
		body.setUserData(MainScreen.player);

		light = new ConeLight(rayhandler,120,new Color(1,1,1,0.7f), 350,body.getPosition().x, body.getPosition().y,360,360);
		light.setPosition(MainScreen.player.getX()+ 17,MainScreen.player.getY()+ 17);
		light.setSoftnessLength(0f);
	}


	@Override
	public void buildStage() {
		connectedPlayers = new HashMap<Integer,Player>();
	}

	public RayHandler getRayHandler() {
		return rayhandler;
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

		//updates the camera position
		update(delta);
		r.setView(cam);
		r.render();

		ArrayList<Player> allPlayers = getYBasedSortedPlayers();

		r.getBatch().begin();

		//if the player is a crew member, call setCurrentTask() on the player which sets the players current task if they have tried to start a task
		if(MainScreen.player instanceof CrewMember) {
			for (Player p : allPlayers)
			{
				if(p instanceof CrewMember) {
					//if the player is in the range of the main-player then render in on the screen

					//player's x is less than the main players x + distance
					//player's x is more than the main players x - distance
					//player's y is less than the main players y + distance
					//players' y is more than the main players y - distance
					if((p.getX() < MainScreen.player.getX() + light.getDistance()) && (p.getX() > MainScreen.player.getX() - light.getDistance()) 
							&& (p.getY() < MainScreen.player.getY() + light.getDistance()) && (p.getY() > MainScreen.player.getY() - light.getDistance())){
						((CrewMember) p).draw(r.getBatch());
					}
				}
				else if(p instanceof Imposter) {
					((Imposter) p).draw(r.getBatch());
				}
			}

			//if the player presses space, check the task they want to do and check if the task is not completed then set that task to the current task
			if(Gdx.input.isKeyPressed(Keys.SPACE)) {
				((CrewMember) MainScreen.player).setCurrentTaskIfCollided();
			}

			//if the player has a current task, render the task screen ui
			if(((CrewMember) MainScreen.player).getCurrentTask() != null) {
				//based on the task the player is doing, render the appropriate task 
				task = ((CrewMember) MainScreen.player).getCurrentTask();

				if(task instanceof AdminTask) {
					((AdminTask) task).render(r.getBatch());
				}
				else if(task instanceof ReactorTask) {
					System.out.println(task.getTaskName());
				}
				else if(task instanceof ElectricalTask) {
					System.out.println(task.getTaskName());
				}
				else if(task instanceof ComsTask) {
					System.out.println(task.getTaskName());
				}
				else if(task instanceof LabTask) {
					System.out.println(task.getTaskName());
				}
			}
		}
		else if(MainScreen.player instanceof Imposter) {

		}
		r.getBatch().end();
		light.setPosition(MainScreen.player.getX() + 17,MainScreen.player.getY() + 17);

		try {
			if(((CrewMember) MainScreen.player).getCurrentTask() == null) {
				Gdx.input.setInputProcessor(this);
				MainScreen.player.render(Gdx.graphics.getDeltaTime());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rayhandler.setCombinedMatrix(cam);
		rayhandler.updateAndRender();
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
		rayhandler.dispose();
	}

	public World getWorld() {
		return this.world;
	}

}