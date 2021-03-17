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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mmog.Client;
import com.mmog.screens.GameScreen;
import com.mmog.tasks.ElectricalTask;
import com.mmog.tasks.EmergencyMeeting;
import com.mmog.tasks.ReactorTask;
import com.mmog.tasks.Task;

import Misc.Vent;

public class Imposter extends Player{
	private Table table;
	private Stage stage;

	public ImageButton useButton, sabotageButton,reportButton,ventButton,lightsButton,reactorButton;

	BitmapFont labelFont = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
	Label tasksLabel;
	public boolean sabotageClicked;
	public ArrayList<Task> tasks;
	private Task currentTask;
	private int lightsCDTimer = 3000, reactorCDTimer = 80;
	private boolean lightsOnCD = false, reactorOnCD = false, reported = false;

	public Imposter(int playerID) {
		super(playerID);

		sabotageClicked =  false;
		tasks = new ArrayList<Task>();
		stage = new Stage();
		table = new Table();
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);
		table.setFillParent(true);
		setCurrentTask(null);
		//font sizes
		labelFont.getData().setScale(0.4f);
		LabelStyle labelFontStyle = new LabelStyle(labelFont, Color.WHITE);

		//init textures
		final TextureRegionDrawable sabotage = new TextureRegionDrawable(new Texture("UI/sabotageButton.png"));
		final TextureRegionDrawable report = new TextureRegionDrawable(new Texture("UI/reportButton.png"));
		final TextureRegionDrawable vent = new TextureRegionDrawable(new Texture("UI/ventButton.png"));
		final TextureRegionDrawable lights = new TextureRegionDrawable(new Texture("UI/lightsButton.png"));
		final TextureRegionDrawable reactor = new TextureRegionDrawable(new Texture("UI/reactorButton.png"));
		final TextureRegionDrawable use = new TextureRegionDrawable(new Texture("UI/useButton.png"));


		//init images
		useButton = new ImageButton(use);
		sabotageButton = new ImageButton(sabotage);
		reportButton = new ImageButton(report);
		ventButton = new ImageButton(vent);
		lightsButton = new ImageButton(lights);
		reactorButton = new ImageButton(reactor);

		reportButton.setVisible(false);
		ventButton.setVisible(false);
		lightsButton.setVisible(false);
		reactorButton.setVisible(false);

		//labels
		tasksLabel = new Label(tasksToString(), labelFontStyle);

		//add items to the table
		table.left().top();
		table.add(tasksLabel);
		table.row().padTop((Gdx.graphics.getHeight()/2) + 100);

		table.add(lightsButton);
		table.add(reactorButton);

		table.row().padTop(40);

		table.add(ventButton);
		table.add(reportButton);
		table.add(sabotageButton);
		table.add(useButton);

		//add table as an actor to the stage
		stage.addActor(table);

		//for the use button
		for(int i = 0; i < meetingUses; i++) {
			addTask(new EmergencyMeeting());
		}
		
		//use button listener
		lightsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("LIGHTS CLICKED: " + isOver());
				lightsOnCD = true;

				//when they trigger lights, add the electrical task
				if(!hasTask("Electrical Task")) {
					tasks.add(new ElectricalTask());
				}

				//should add a timer here, so imposters can't spam this command
				try {
					Client.sendLightsCommand();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		//use button listener
		reactorButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("REACTOR CLICKED: " + isOver());
				reactorOnCD = true;

				if(!hasTask("Reactor Task")) {
					tasks.add(new ReactorTask());
				}

				//should add a timer here, so imposters can't spam this command
				try {
					Client.sendReactorCommand();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		//use button listener
		ventButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!inVent) {
					inVent = true;

					System.out.println("ENTERING VENT!");
					addImposterToVent();
				}
				else {
					inVent = false;

					//sends message to all other players that the player has vented OUT
					try {
						Client.sendOutVent(getX(), getY(), isFlipped, isDead, isIdle, getHatID(), hatIsFlipped);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		//use button listener
		sabotageButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("SABOTAGE CLICKED: " + isOver());
				if(!sabotageClicked) {
					if(!lightsOnCD) {
						lightsButton.setVisible(true);
					}
					if(!reactorOnCD) {
						reactorButton.setVisible(true);
					}
					sabotageClicked = true;
				}
				else {
					lightsButton.setVisible(false);
					reactorButton.setVisible(false);
					sabotageClicked = false;
				}
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

		//use button listener
		useButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("USE CLICKED: " + isOver());
				setCurrentTaskIfCollided();
			}
		});
	}

	public Task setCurrentTaskIfCollided() {
		for(Task task: tasks) {
			if(!task.isTaskCompleted() && checkCollisionOnTask(task.getTaskName())) {	
				if(isDead && (task.getTaskName().equals("Electrical Task") || task.getTaskName().equals("Reactor Task"))) {
					currentTask = null;
					return currentTask;
				}
				else {
					if(task instanceof EmergencyMeeting) {
						try {
							Client.sendTriggerMeeting();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
					
					currentTask = task;
					return currentTask;
				}
			}
		}
		return currentTask;
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

	public void removeAllMeetings() {
		ArrayList<Task> toRemove = new ArrayList<Task>();
		for(Task t: tasks) {
			if(t instanceof EmergencyMeeting) {
				toRemove.add(t);		
			}	
		}	
		tasks.removeAll(toRemove);
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
		}

		tasks.removeAll(toRemove);
	}

	public boolean hasTask(String task) {
		for(Task t: tasks) {
			if(t.getTaskName().equals(task)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkCollisionOnVent() {
		//loop through all the vents
		for(Vent v: GameScreen.vents) {
			if(v.getRec().overlaps(Client.getPlayer().getBoundingRectangle())) {
				System.out.println("OVERLAPS!");
				return true;
			}
		}
		return false;
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void addImposterToVent() {
		for(Vent v: GameScreen.vents) {
			if(v.getRec().overlaps(Client.getPlayer().getBoundingRectangle())) {
				v.addImposter((Imposter) Client.getPlayer());

				//send invent to all the other playerss
				try {
					Client.sendInVent();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public String tasksToString() {
		String tasksString = "-Fake Tasks-\n";

		tasksString += "Admin Task\nReactor Task\nComs Task\nElectrical Task";
		return tasksString;
	}

	public void drawUI(Batch batch) {
		Gdx.input.setInputProcessor(this.stage);

		ventButton.setVisible(checkCollisionOnVent());

		if(isDead) {
			this.removeAllMeetings();
			ventButton.setVisible(false);
		}
		
		//if lights sabotage on cool down, disable button
		if(lightsOnCD) {
			lightsButton.setVisible(false);

			if(lightsCDTimer > 0) {
				lightsCDTimer -= Gdx.graphics.getDeltaTime();
			}
			else {
				lightsCDTimer = 30;
				lightsOnCD = false;
				lightsButton.setVisible(true);
			}

		}
		//if reactor sabotage on cool down, disable button
		if(reactorOnCD) {
			reactorButton.setVisible(false);

			if(reactorCDTimer > 0) {
				reactorCDTimer -= Gdx.graphics.getDeltaTime();
			}
			else {
				reactorCDTimer = 80;
				reactorOnCD = false;
				reactorButton.setVisible(true);
			}
		}

		stage.act();
		stage.draw();
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
				currentTask = task;		
				return;
			}
		}
	}

}
