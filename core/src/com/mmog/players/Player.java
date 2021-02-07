package com.mmog.players;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mmog.Client;

import box2dLight.Light;

public class Player extends Sprite{

	private TextureAtlas walkRightAtlas;
	private TextureAtlas walkLeftAtlas;
	private Animation<TextureRegion> walkLeft;
	private Animation<TextureRegion> walkRight;
	public float elapsedTime = 0;
	private String playerName;
	public boolean isFlipped, isDead, isIdle;
	private int playerID;
	private TiledMapTileLayer collisionLayer;
	boolean collisionX = false, collisionY = false;
	float tileWidth,tileHeight;
	boolean playerMoved = false;
	private BitmapFont f;
	protected Body body;
	public boolean readyToPlay;
	public String role = "none";
	Sprite p;
	public String connectedRoomName = "";
	public boolean isHost;
	public Color playerColor;
	public int speed;
	public Rectangle playerRec;
	public boolean ghostSet,justKilled;
	Animation<TextureRegion> animation;
	//animated background
	TextureRegion[] frames = new TextureRegion[33];
	public boolean addedToDead;

	public Player(int playerID)
	{
		super(new Sprite (new Texture("idle.png")));
		ghostSet = false;
		speed = 4;
		setSize(32,50);
		addedToDead = false;
		//this.setColor(Color.YELLOW);
		justKilled = false;
		//player collison rectangle
		playerRec = new Rectangle(getX(),getY(),32,50);
		isDead = false;
		createDeadAnim();
		isHost = false;
		readyToPlay = false;
		walkRightAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
		walkLeftAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));

		isFlipped = false;
		walkRight = new Animation<TextureRegion>(1/15f, walkRightAtlas.getRegions());
		walkLeft = new Animation<TextureRegion>(1/15f, walkLeftAtlas.getRegions());
		isIdle = false;

		this.setPlayerID(playerID);

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

	public void setDead() {
		float x = getX();
		float y = getY();
		
		set(new Sprite((new Texture("Among Us - Player Base/Individual Sprites/Ghost/ghostbob0048.png"))));
		setSize(32,45);
		setPosition(x,y);
	}
	
	public void setAlive() {
		float x = getX();
		float y = getY();
		set((new Sprite (new Texture("idle.png"))));
		setSize(32,45);
		setPosition(x,y);
	}

	public boolean getIsIdle() {
		return isIdle;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Body getBody() {
		return this.body;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;

		tileWidth = collisionLayer.getTileWidth();
		tileHeight = collisionLayer.getTileHeight();
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	@Override
	public void draw(Batch batch)
	{
		update(Gdx.graphics.getDeltaTime(), batch);
	}

	public void drawDeadSprite(Batch batch) {
		float x = getX();
		float y = getY();
		
		Sprite deadSprite = new Sprite((new Texture("Among Us - Player Base/Individual Sprites/Death/Dead0033.png")));
		setSize(32,45);
		setPosition(x,y);
		
		deadSprite.draw(batch);
	}

	public void update(float delta, Batch batch) {
		//player name
		f.draw(batch, getPlayerName(), getX() + getWidth()/2 - getPlayerName().length() * 2 - 2, getY() + getHeight() + 20);
		playerRec.setPosition(getX(), getY());

		//set player color here too
		//batch.setColor(Color.YELLOW);

		elapsedTime += delta;
		if (isFlipped && !isIdle && !isDead)
		{
			batch.draw(walkLeft.getKeyFrame(elapsedTime, true), getX(), getY(),32,50);
		}
		else if (!isFlipped && !isIdle && !isDead)
		{
			batch.draw(walkRight.getKeyFrame(elapsedTime, true), getX(), getY(),32,50);
		}
		
		/*
		else if(justKilled) {
			batch.draw(animation.getKeyFrame(elapsedTime),getX(),getY(),32,50);

			if(elapsedTime > 5) {
				//setDead();
				justKilled = false;
			}
		}
		*/
		
		if(isDead) {
			ghostSet = true;
		}else if(!isDead) {
			setAlive();
		}
		
		if(ghostSet) {
			setDead();
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

	public void createDeadAnim() {
		//get all the frames
		for(int i = 0; i < 33; i++) {
			int j = i + 1;
			if(j < 10) {
				frames[i] = (new TextureRegion(new Texture("Among Us - Player Base/Individual Sprites/Death/Dead000" + j + ".png")));
			}
			else {
				frames[i] = (new TextureRegion(new Texture("Among Us - Player Base/Individual Sprites/Death/Dead00" + j + ".png")));
			}
		}

		animation = new Animation<TextureRegion>(1f/15f,frames);
	}

	public boolean collisionAtX(int x, String key) {
		//collision detection in x
		collisionX = collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
		collisionX |= collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) ((getY() + getHeight()/2) / tileHeight)).getTile().getProperties().containsKey(key);
		collisionX |= collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey(key);

		collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
		collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) ((getY() + getWidth()/2) / tileHeight)).getTile().getProperties().containsKey(key);
		collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey(key);

		return collisionX;
	}

	public boolean collisionAtY(int y, String key) {
		//collision detection in y
		collisionY = collisionLayer.getCell((int) (getX()/tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey(key);
		collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth() /2) /tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey(key);
		collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth()) /tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey(key);

		collisionY |= collisionLayer.getCell((int) (getX()/tileWidth), (int) ((getY() + y + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
		collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth() /2) /tileWidth), (int) ((getY()+ y  + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
		collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth()) /tileWidth), (int) ((getY()+ y  + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);

		return collisionY;
	}

	public void render(float delta) throws Exception
	{
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			for(int i = 0; i<speed;i++) {
				if(!collisionAtX(-1,"blocked")) {
					setX(getX() - 1);
				}
			}

			isFlipped = true;
			isIdle=false;
			playerMoved = true;
		}

		else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			for(int i = 0; i<speed;i++) {
				if(!collisionAtX(1,"blocked")) {
					setX(getX() + 1);
				}
			}

			isFlipped = false;
			isIdle=false;
			playerMoved = true;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W))
		{
			for(int i = 0; i<speed;i++) {
				if(!collisionAtY(1,"blocked")) {
					setY(getY() + 1);
				}
			}

			isIdle = false;
			playerMoved = true;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S))
		{
			for(int i = 0; i<speed;i++) {
				if(!collisionAtY(-1,"blocked")) {
					setY(getY() - 1);
				}
			}
			isIdle = false;
			playerMoved = true;
		}

		if (!isIdle && !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.D) )
		{               	
			isIdle = true;
			Client.sendUpdate(getX(), getY(), isFlipped, isDead, isIdle);
			playerMoved = false;
		}

		if(playerMoved) {
			Client.sendUpdate(getX(), getY(), isFlipped, isDead, isIdle);
		}
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public void setAll(float x, float y, boolean isFlipped, boolean isDead, boolean isIdle) {
		setX(x);
		setY(y);
		this.isFlipped = isFlipped;
		this.isDead = isDead;
		this.isIdle = isIdle;
	}

	public ArrayList<String> getAll(){
		ArrayList<String> allInfo = new ArrayList<>();

		allInfo.add(playerName);
		allInfo.add(getX() + "");
		allInfo.add(getY() + "");
		allInfo.add(isFlipped + "");
		allInfo.add(isDead + "");
		allInfo.add(isIdle + "");

		return allInfo;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
