package com.mmog.players;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mmog.Client;
import com.mmog.tasks.Task;

public class CrewMember extends Player {

	public boolean isDoingTask = false;
	private ArrayList<Task> tasks;
	private BitmapFont f;
	private Task currentTask;
	private Table table;
	private Stage stage;

	public ImageButton useButton,reportButton;

	BitmapFont labelFont = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
	Label tasksLabel;

	public CrewMember(int playerID) {
		super(playerID);
		this.tasks = new ArrayList<Task>();
		f = new BitmapFont();
		currentTask = null;

		stage = new Stage();

		table = new Table();
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);
		table.setFillParent(true);

		//font sizes
		labelFont.getData().setScale(0.4f);
		LabelStyle labelFontStyle = new LabelStyle(labelFont, Color.WHITE);

		//init textures
		final TextureRegionDrawable use = new TextureRegionDrawable(new Texture("UI/useButton.png"));
		final TextureRegionDrawable report = new TextureRegionDrawable(new Texture("UI/reportButton.png"));

		//init images
		useButton = new ImageButton(use);
		reportButton = new ImageButton(report);
		
		//reportButton.setVisible(false);

		//labels
		tasksLabel = new Label(tasksToString(), labelFontStyle);

		//add items to the table
		table.left().top();
		table.add(tasksLabel);
		table.row().padTop((Gdx.graphics.getHeight()/2) + 100);
		table.add(reportButton);
		table.add(useButton);

		//add table as an actor to the stage
		stage.addActor(table);

		//use button listener
		useButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("USE CLICKED: " + isOver());
				((CrewMember) Client.getPlayer()).setCurrentTaskIfCollided();
			}
		});

		//report button listener
		reportButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("REPORT CLICKED: " + isOver());
			}
		});

	}

	public void drawTasks(Batch batch) {
		Gdx.input.setInputProcessor(this.stage);
		tasksLabel.setText(tasksToString());

		stage.act();
		stage.draw();
	}

	public void drawLabel(Batch batch) {

	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task task) {
		this.currentTask = task;
	}

	/*
	 * Sets the current tasks of the crew member if the player collides with any of the tasks
	 */
	public void setCurrentTaskIfCollided() {
		for(Task task: tasks) {
			if(!task.isTaskCompleted() && checkCollisionOnTask(task.getTaskName())) {	
				currentTask = task;
				return;
			}
		}
	}

	public boolean checkCollisionOnTask(String taskName) {
		if((collisionAtX(1,taskName) || collisionAtY(1,taskName))){
			return true;
		}
		return false;
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


	public Task getTask(String taskName) {
		Task task = null;

		for(Task t: tasks) {
			if(t.getTaskName().equals(taskName)) {
				task = t;
			}
		}

		return task;
	}


	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

}
