package com.mmog.players;

import java.util.ArrayList;
import java.util.Stack;

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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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
import com.mmog.screens.GameScreen;
import com.mmog.screens.LobbyScreen;
import com.mmog.tasks.EmergencyMeeting;
import com.mmog.tasks.Task;

import box2dLight.Light;

public class Player extends Sprite{

	private TextureAtlas walkRightAtlas, ghostRightAtlas;
	private TextureAtlas walkLeftAtlas, ghostLeftAtlas;
	private Animation<TextureRegion> walkLeft, ghostLeft;
	private Animation<TextureRegion> walkRight, ghostRight;
	public float elapsedTime = 0;
	private String playerName;
	public boolean isFlipped, isDead, isIdle, hatIsFlipped;
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
	public float speed;
	public boolean ghostSet,justKilled;
	Animation<TextureRegion> animation;
	//animated background
	TextureRegion[] frames = new TextureRegion[33];
	public boolean addedToDead;
	public boolean inVent = false, votedOff = false;
	public MapObjects inGameWalls;
	public boolean inGame = false;
	public int meetingUses = 2;
	public Stack<Task> emergencyMeetings;
	public Sprite hat;
	private int hatID, oldhatID;
	
	public Player(int playerID)
	{
		super(new Sprite (new Texture("idle.png")));
		ghostSet = false;
		speed = 4;
		setSize(32,50);
		addedToDead = false;
		//this.setColor(Color.YELLOW);
		justKilled = false;
		setHatID(-1);
		setOldhatID(hatID);
		
		//set test hat
		//hat = new Sprite (new Texture("Hats/hats0002.png"));
		
		//player collison rectangle
		
		emergencyMeetings = new Stack<Task>();
		
		isDead = false;
		createDeadAnim();
		isHost = false;
		walkRightAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
		walkLeftAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
		
		ghostRightAtlas = new TextureAtlas(Gdx.files.internal("ghostbob.atlas"));
		ghostLeftAtlas = new TextureAtlas(Gdx.files.internal("ghostbob.atlas"));

		isFlipped = false;
		hatIsFlipped = false;
		walkRight = new Animation<TextureRegion>(1/15f, walkRightAtlas.getRegions());
		walkLeft = new Animation<TextureRegion>(1/15f, walkLeftAtlas.getRegions());
		
		ghostRight = new Animation<TextureRegion>(1/48f, ghostRightAtlas.getRegions());
		ghostLeft = new Animation<TextureRegion>(1/48f, ghostLeftAtlas.getRegions());
		isIdle = false;

		this.setPlayerID(playerID);

		this.playerName = "";

		f = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
		f.getData().setScale(0.2f);


		for (float i = 0; i < 1; i += 0.01f)
		{
			TextureRegion tr = walkLeft.getKeyFrame(i,true);
			TextureRegion gl = ghostLeft.getKeyFrame(i,true);
			
			if(!tr.isFlipX())
			{
				tr.flip(true, false);
			}
			
			if(!gl.isFlipX())
			{
				gl.flip(true, false);
			}
		}
	}
	
	public void clearAll() {
		ghostSet = false;
		addedToDead = false;
		justKilled = false;
		isDead = false;
		isHost = false;
		isFlipped = false;
		isIdle = false;
		inVent = false;
		inGame = false;
		votedOff = false;
		hatIsFlipped = false;
		hat = null;
		setHatID(-1);
		setOldhatID(-1);
		this.playerName = "";
		this.playerID = -1;
	}
	
	public void setHat(Sprite hat, int hatID) {
		this.hat = hat;
		this.setHatID(hatID);
		hat.setSize(30, 30);
	}

	public void setDead() {
		float x = getX();
		float y = getY();

		set(new Sprite((new Texture("Among Us - Player Base/Individual Sprites/Ghost/ghostbob0048.png"))));
		setSize(32,50);
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
		//set player color here too
		//batch.setColor(Color.YELLOW);
		
		elapsedTime += delta;
		if (isFlipped && !isIdle)
		{
			if(hatID != -1 && hat != null && hatIsFlipped && !hat.isFlipX()) {
				hat.flip(true, false);
			}
			
			if(!isDead) {
				batch.draw(walkLeft.getKeyFrame(elapsedTime, true), getX(), getY(),32,50);
			}
			else
				batch.draw(ghostLeft.getKeyFrame(elapsedTime, true), getX(), getY(),32,50);
			
		}
		else if (!isFlipped && !isIdle)
		{
			if(hatID != -1 && hat != null && !hatIsFlipped && hat.isFlipX()) {
				hat.flip(true, false);
			}
			
			if(!isDead) {
				batch.draw(walkRight.getKeyFrame(elapsedTime, true), getX(), getY(),32,50);
			}
			else
				batch.draw(ghostRight.getKeyFrame(elapsedTime, true), getX(), getY(),32,50);
		}

		if(isDead) {
			ghostSet = true;
		}

		if (isIdle)
		{
			if(isFlipped && !isFlipX() || !isFlipped && isFlipX())
			{
				flip(true, false);
			}
			
			super.draw(batch);
			
			if(isDead) {
				setDead();
			}
		}
		
		//draw hat if the player has one
		if(hatID != -1) {
			hat = LobbyScreen.hats.get(hatID);
			hat.setPosition(getX(), getY() + (getHeight()/2) + 11);
			hat.draw(batch,0.9f);
		}
		//player name
		f.draw(batch, getPlayerName(), getX() + getWidth()/2 - getPlayerName().length() * 2 - 5, getY() + getHeight() + 20);
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
		if(collisionLayer != null){
			//collisionX = collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
			//collisionX |= collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) ((getY() + getHeight()/2) / tileHeight)).getTile().getProperties().containsKey(key);
			collisionX = collisionLayer.getCell((int)((getX()+ x) /tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey(key);

			//collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
			//collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) ((getY() + getWidth()/2) / tileHeight)).getTile().getProperties().containsKey(key);
			collisionX |= collisionLayer.getCell((int)((getX()+ x + getWidth()) /tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey(key);	
		}

		return collisionX;
	}

	public boolean collisionAtY(int y, String key) {
		if(collisionLayer != null){
			//collision detection in y
			collisionY = collisionLayer.getCell((int) (getX()/tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey(key);
			collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth() /2) /tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey(key);
			collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth()) /tileWidth), (int) ((getY()+ y)  / tileHeight)).getTile().getProperties().containsKey(key);

			//collisionY |= collisionLayer.getCell((int) (getX()/tileWidth), (int) ((getY() + y + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
			//collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth() /2) /tileWidth), (int) ((getY()+ y  + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
			//collisionY |= collisionLayer.getCell((int) ((getX()+ getWidth()) /tileWidth), (int) ((getY()+ y  + getHeight()) / tileHeight)).getTile().getProperties().containsKey(key);
		}
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
			hatIsFlipped = true;
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
			hatIsFlipped = false;
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
			Client.sendUpdate(getX(), getY(), isFlipped, isDead, isIdle, getHatID(), hatIsFlipped);
			playerMoved = false;
		}

		if(playerMoved || hatChanged()) {
			Client.sendUpdate(getX(), getY(), isFlipped, isDead, isIdle, getHatID(), hatIsFlipped);
		}
	}
	
	public boolean hatChanged() {
		if(oldhatID != hatID) {
			oldhatID = hatID;
			return true;
		}
		return false;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public void setAll(float x, float y, boolean isFlipped, boolean isDead, boolean isIdle, int hatID, boolean hatIsFlipped) {
		setX(x);
		setY(y);
		this.isFlipped = isFlipped;
		this.isDead = isDead;
		this.isIdle = isIdle;
		this.setHatID(hatID);
		this.hatIsFlipped = hatIsFlipped;
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

	public int getHatID() {
		return hatID;
	}

	public void setHatID(int hatID) {
		this.hatID = hatID;
	}

	public int getOldhatID() {
		return oldhatID;
	}

	public void setOldhatID(int oldhatID) {
		this.oldhatID = oldhatID;
	}
}
