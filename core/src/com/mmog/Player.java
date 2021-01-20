package com.mmog;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Player extends Sprite{

	private TextureAtlas walkRightAtlas;
	private TextureAtlas walkLeftAtlas;
	private Animation<TextureRegion> walkLeft;
	private Animation<TextureRegion> walkRight;
	private float elapsedTime = 0;
	private ArrayList<Task> tasks;
	private String playerName;
	private Label playerNameLabel;
	private boolean isFlipped;
	private boolean isDead;
	private boolean isIdle;
	private int playerID;
	private BitmapFont f;
	private TiledMapTileLayer collisionLayer;
	boolean collisionX = false, collisionY = false;
	float tileWidth,tileHeight;

	public Player(Sprite sprite, int playerID)
	{
		super(sprite);

		walkRightAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));

		walkLeftAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));

		isFlipped = false;
		walkRight = new Animation<TextureRegion>(1/15f, walkRightAtlas.getRegions());
		walkLeft = new Animation<TextureRegion>(1/15f, walkLeftAtlas.getRegions());
		isIdle = false;

		this.setPlayerID(playerID);
		this.tasks = new ArrayList<Task>();
		this.playerName = "";

		f = new BitmapFont();

		for (float i = 0; i < 1; i += 0.01f)
		{
			TextureRegion tr = walkLeft.getKeyFrame(i,true);

			if(!tr.isFlipX())
			{
				tr.flip(true, false);
			}
		}
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}


	public boolean isTaskCompleted(String task) {
		for(Task t: tasks) {
			if(t.getTaskName().equals(task) && t.isTaskCompleted()) {
				return true;
			}
		}
		return false;
	}

	public void setTaskCompleted(String task) {
		for(Task t: tasks) {
			if(t.getTaskName().equals(task)) {
				t.setTaskCompleted();
				return;
			}
		}
	}

	public boolean hasTask(String task) {
		for(Task t: tasks) {
			if(t.getTaskName().equals(task)) {
				return true;
			}
		}
		return false;
	}

	public String tasksToString() {
		String tasksString = "-Crew Member Tasks-\n";

		for(Task t: tasks) {
			tasksString += t.getTaskName();

			if(t.isTaskCompleted())
				tasksString += ": COMPLETED\n";
			else
				tasksString += ": INCOMPLETE\n";
		}

		return tasksString;
	}

	@Override
	public void draw(Batch batch)
	{
		update(Gdx.graphics.getDeltaTime(), batch);
	}

	public void update(float delta, Batch batch) {
		//player name
		f.draw(batch, getPlayerName(), getX() + getWidth()/2 - getPlayerName().length() * 2 - 2, getY() + getHeight() + 20);

		elapsedTime += delta;

		if (isFlipped && !isIdle)
		{
			batch.draw(walkLeft.getKeyFrame(elapsedTime, true), getX(), getY(),32,50);
		}
		else if (!isFlipped && !isIdle)
		{
			batch.draw(walkRight.getKeyFrame(elapsedTime, true), getX(), getY(),32,50);
		}

		if (isIdle)
		{
			if(isFlipped && !isFlipX() || !isFlipped && isFlipX())
			{
				flip(true, false);
			}
			super.draw(batch);
		}
	}

	public boolean collisionAtX(int x) {
		//collision detection in x
		collisionX = collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
		collisionX |= collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) ((getY() + getHeight()/2) / tileHeight)).getTile().getProperties().containsKey("blocked");
		collisionX |= collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

		collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
		collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) ((getY() + getWidth()/2) / tileHeight)).getTile().getProperties().containsKey("blocked");
		collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

		return collisionX;
	}

	public boolean collisionAtY(int y) {
		//collision detection in y
		collisionY = collisionLayer.getCell((int) (getX()/tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey("blocked");
		collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth() /2) /tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey("blocked");
		collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth()) /tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey("blocked");

		collisionY |= collisionLayer.getCell((int) (getX()/tileWidth), (int) ((getY() + y + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
		collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth() /2) /tileWidth), (int) ((getY()+ y  + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
		collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth()) /tileWidth), (int) ((getY()+ y  + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
		return collisionY;
	}

	public void render(float delta) throws Exception
	{
		boolean playerMoved = false;
		
		tileWidth = collisionLayer.getTileWidth();
	    tileHeight = collisionLayer.getTileHeight();
	    
	    
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			for(int i = 0; i<8;i++) {
				if(!collisionAtX(-1)) {
					setX(getX() - 1);
				}
			}
			
			isFlipped = true;
			isIdle=false;
			playerMoved = true;
		}

		else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			for(int i = 0; i<8;i++) {
				if(!collisionAtX(1)) {
					setX(getX() + 1);
				}
			}
			
			isFlipped = false;
			isIdle=false;
			playerMoved = true;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W))
		{
			for(int i = 0; i<8;i++) {
				if(!collisionAtY(1)) {
					setY(getY() + 1);
				}
			}

			isIdle = false;
			playerMoved = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S))
		{
			for(int i = 0; i<8;i++) {
				if(!collisionAtY(-1)) {
					setY(getY() - 1);
				}
			}
			isIdle = false;
			playerMoved = true;
		}

		if (!isIdle && !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.D) )
		{               	
			isIdle = true;
			playerMoved = true;
		}

		if(playerMoved) {
			Client.sendUpdate(getX(), getY(), isFlipped, isDead, isIdle, getPlayerName());
		}
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public void setAll(float x, float y, boolean isFlipped, boolean isDead, boolean isIdle, String playerName) {
		setX(x);
		setY(y);
		this.isFlipped = isFlipped;
		this.isDead = isDead;
		this.isIdle = isIdle;
		this.playerName = playerName;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
