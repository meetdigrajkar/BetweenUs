package com.mmog.players;

import java.io.IOException;

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
import com.mmog.tasks.Task;

import Misc.Vent;

public class Imposter extends Player{
	private Table table;
	private Stage stage;

	public ImageButton sabotageButton,reportButton,ventButton,lightsButton,reactorButton;

	BitmapFont labelFont = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
	Label tasksLabel;
	public boolean sabotageClicked;
	public boolean enteringVent = false;
	private Vent currVent;

	public Imposter(int playerID) {
		super(playerID);

		sabotageClicked =  false;

		stage = new Stage();
		table = new Table();
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);
		table.setFillParent(true);
		currVent = null;

		//font sizes
		labelFont.getData().setScale(0.4f);
		LabelStyle labelFontStyle = new LabelStyle(labelFont, Color.WHITE);

		//init textures
		final TextureRegionDrawable sabotage = new TextureRegionDrawable(new Texture("UI/sabotageButton.png"));
		final TextureRegionDrawable report = new TextureRegionDrawable(new Texture("UI/reportButton.png"));
		final TextureRegionDrawable vent = new TextureRegionDrawable(new Texture("UI/ventButton.png"));
		final TextureRegionDrawable lights = new TextureRegionDrawable(new Texture("UI/lightsButton.png"));
		final TextureRegionDrawable reactor = new TextureRegionDrawable(new Texture("UI/reactorButton.png"));

		//init images
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

		//add table as an actor to the stage
		stage.addActor(table);

		//use button listener
		lightsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("LIGHTS CLICKED: " + isOver());

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
						Client.sendOutVent(getX(), getY(), isFlipped, isDead, isIdle);
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
					lightsButton.setVisible(true);
					reactorButton.setVisible(true);
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
			}
		});
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

		stage.act();
		stage.draw();
	}

}
