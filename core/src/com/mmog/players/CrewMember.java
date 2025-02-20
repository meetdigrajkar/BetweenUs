package com.mmog.players;

import java.io.IOException;
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
import com.mmog.screens.GameScreen;
import com.mmog.tasks.AdminTask;
import com.mmog.tasks.ComsTask;
import com.mmog.tasks.EmergencyMeeting;
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

	public boolean reported = false, sentCompletedTasks;

	private int totalTasks = 0;
	
	public CrewMember(int playerID) {
		super(playerID);
		this.tasks = new ArrayList<Task>();
		f = new BitmapFont();
		currentTask = null;
		sentCompletedTasks = false;
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

		reportButton.setVisible(false);

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

		//for the use button
		for(int i = 0; i < meetingUses; i++) {
			addTask(new EmergencyMeeting());
		}

		addTask(new AdminTask());
		addTask(new ComsTask()); 

		totalTasks = tasks.size();
		 
		//use button listener
		useButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("USE CLICKED: " + isOver());
				setCurrentTaskIfCollided();
			}
		});

		//report button listener
		reportButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("REPORT CLICKED: " + isOver());

				try {
					Client.sendTriggerMeeting();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	

				reported = true;
				setCurrentTask("Emergency Meeting");
			}
		});

	}

	public void drawTasks(Batch batch) {
		Gdx.input.setInputProcessor(this.stage);
		tasksLabel.setText(tasksToString());

		if(isDead) {
			this.removeAllMeetings();
		}
		
		//check if crewmember has finished all the tasks and send a message to the server that the crew member is done tasks
		if(!sentCompletedTasks) {
			checkAndSendTasksCompleted();
		}
		
		stage.act();
		stage.draw();
	}

	public void checkAndSendTasksCompleted(){
		int completedTasks = 0;
		
		for(Task t: tasks) {
			if(t.isTaskCompleted()) {
				System.out.println(t.getTaskName() + "IS COMPLETE!");
				completedTasks += 1;
			}
		}
		
		//if all the tasks are completed, send the command to the server
		if(completedTasks == totalTasks - 2) {
			try {
				Client.sendCompletedAllTasks();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sentCompletedTasks = true;
		}	
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String taskName) {
		if(taskName == null) {
			currentTask = null;
			return;
		}

		if(taskName.equals("Emergency Meeting") && !reported) {
			currentTask = emergencyMeetings.pop();
			return;
		}
		else if(taskName.equals("Emergency Meeting") && reported) {
			currentTask = new EmergencyMeeting();
			reported = false;
			return;
		}

		for(Task task: tasks) {
			if(task.getTaskName().equals(taskName) && !task.isTaskCompleted()) {	
				System.out.println("Setting current task to: " + task.getTaskName());
				currentTask = task;
				return;
			}
		}
	}


	/*
	 * Sets the current tasks of the crew member if the player collides with any of the tasks
	 */
	public void setCurrentTaskIfCollided() {
		for(Task task: tasks) {
			if(!task.isTaskCompleted() && checkCollisionOnTask(task.getTaskName())) {	
				if(isDead && (task.getTaskName().equals("Electrical Task") || task.getTaskName().equals("Reactor Task"))) {
					currentTask = null;
					return;
				}
				else {
					if(task instanceof EmergencyMeeting && !GameScreen.reactorTaskStarted) {
						try {
							Client.sendTriggerMeeting();
							currentTask = task;
							return;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
					else {
						currentTask = task;
						return;
					}
				}
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

	public void removeTask(String task) {
		ArrayList<Task> toRemove = new ArrayList<Task>();

		for(Task t: tasks) {
			if(t instanceof EmergencyMeeting) {
				if(((EmergencyMeeting) t).meetingCompleted) {
					toRemove.add(t);
					break;
				}
			}
			else if(t.getTaskName().equals(task)) {
				toRemove.add(t);
				break;
			}
		}

		tasks.removeAll(toRemove);
	}
	
	public void removeAllMeetings() {
		ArrayList<Task> toRemove = new ArrayList<Task>();
		for(Task t: tasks) {
			if(t instanceof EmergencyMeeting) {
				toRemove.add(t);		
			}	
		}	
		tasks.removeAll(toRemove);
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
