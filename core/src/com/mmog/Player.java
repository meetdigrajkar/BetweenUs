package com.mmog;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Player {

	private TextureAtlas walkRightAtlas;
	private TextureAtlas walkLeftAtlas;
	private Animation<TextureRegion> walkLeft;
	private Animation<TextureRegion> walkRight;
	private Texture idle;
	private float elapsedTime = 0;
	public Sprite sprite;
	private float x;
	private ArrayList<Task> tasks;
	private Task currentTask; 
	private String playerName;
	private Label playerNameLabel;

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

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	private float y;
	private boolean isFlipped;
	private boolean isDead;
	private boolean isIdle;
	private int playerID;
	private BitmapFont f;


	public Player(int playerID)
	{
		idle = new Texture(Gdx.files.internal("idle.png"));
		walkRightAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
		walkLeftAtlas = new TextureAtlas(Gdx.files.internal("Walk.atlas"));
		isFlipped = false;
		walkRight = new Animation(1/15f, walkRightAtlas.getRegions());
		walkLeft = new Animation(1/15f, walkLeftAtlas.getRegions());
		sprite = new Sprite(idle);
		isIdle = false;
		this.setPlayerID(playerID);
		this.tasks = new ArrayList<Task>();
		this.playerName = "";
		f = new BitmapFont();

		x=0;
		y=0;

		for (float i = 0; i < 1; i += 0.01f)
		{
			TextureRegion tr = walkLeft.getKeyFrame(i,true);

			if(!tr.isFlipX())
			{
				tr.flip(true, false);
			}

		}
	}

	public void draw(SpriteBatch batch)
	{
		//player name
		f.draw(batch, getPlayerName(), getX() + getWidth()/2 - getPlayerName().length() * 2 - 2, getY() + getHeight() + 20);
		
		elapsedTime += Gdx.graphics.getDeltaTime(); 
		if (isFlipped && !isIdle)
		{
			batch.draw(walkLeft.getKeyFrame(elapsedTime, true), x, y);
		}
		else if (!isFlipped && !isIdle)
		{
			batch.draw(walkRight.getKeyFrame(elapsedTime, true), x, y);
		}

		if (isIdle)
		{
			if(isFlipped && !sprite.isFlipX() || !isFlipped && sprite.isFlipX())
			{
				sprite.flip(true, false);
			}
			sprite.setX(x);
			sprite.setY(y);
			sprite.draw(batch);
		}
	}
	public void render() throws Exception
	{
		boolean playerMoved = false;
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			x -= 15;
			isFlipped = true;
			isIdle=false;
			playerMoved = true;
		}

		else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			x+=15;
			isFlipped = false;
			isIdle=false;
			playerMoved = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W))
		{
			y += 15;
			isIdle = false;
			playerMoved = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S))
		{
			y-=15;      
			isIdle = false;
			playerMoved = true;
		}

		if (!isIdle && !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.D) )
		{               	
			isIdle = true;
			playerMoved = true;
		}

		if(playerMoved) {
			Client.sendUpdate(x, y, isFlipped, isDead, isIdle, getPlayerName());
		}
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public void setAll(float x, float y, boolean isFlipped, boolean isDead, boolean isIdle, String playerName) {
		this.x = x;
		this.y = y;
		this.isFlipped = isFlipped;
		this.isDead = isDead;
		this.isIdle = isIdle;
		this.playerName = playerName;
	}

	public int getWidth() {
		return idle.getWidth();
	}

	public int getHeight() {
		return idle.getHeight();
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
