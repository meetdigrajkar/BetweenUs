package com.mmog.tasks;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;
import com.mmog.screens.GameScreen;
import com.mmog.screens.MainScreen;

public class ElectricalTask extends Task {

	final static String taskName = "Electrical Task";
	Table table;

	//Declaration for background
	Stage stage;
	Sprite elec_back, buttonR, buttonRM, buttonY, buttonYM, buttonB, buttonG, buttonGM;
	final Image elecbackImg, buttonRedLeft, buttonRedRight, buttonRedMid, buttonYellowLeft, buttonYellowRight, buttonYellowMid, buttonBlueLeft, buttonBlueRight, buttonBlueMid, buttonGreenLeft, buttonGreenRight, buttonGreenMid;

	boolean done = false;

	boolean RedWire = false;
	boolean BlueWire = false;
	boolean GreenWire = false;
	boolean YellowWire = false;
	boolean sentFixed = false;

	private String test;

	public ElectricalTask() {
		super(taskName);

		stage = new Stage();

		elec_back = new Sprite(new Texture("TaskUI/Electrical/Electrical Background.png"));
		elecbackImg = new Image(elec_back);
		elecbackImg.setPosition(Gdx.graphics.getWidth()/2 - elecbackImg.getWidth()/2, Gdx.graphics.getHeight()/2 - elecbackImg.getHeight()/2);

		buttonR = new Sprite(new Texture("TaskUI/Electrical/red.png"));
		buttonRedLeft = new Image(buttonR);
		buttonRedLeft.setHeight(45);
		buttonRedLeft.setPosition(514, 790);

		buttonRedRight = new Image(buttonR);
		buttonRedRight.setHeight(45);
		buttonRedRight.setPosition(1286, 236);

		buttonRM = new Sprite(new Texture("TaskUI/Electrical/red_middle.png"));
		buttonRedMid = new Image(buttonRM);
		buttonRedMid.setPosition(634, 236);

		buttonY = new Sprite(new Texture("TaskUI/Electrical/yellow.png"));
		buttonYellowLeft = new Image(buttonY);
		buttonYellowLeft.setHeight(45);
		buttonYellowLeft.setPosition(514, 606);

		buttonYellowRight = new Image(buttonY);
		buttonYellowRight.setHeight(45);
		buttonYellowRight.setPosition(1286, 790);

		buttonYM = new Sprite(new Texture("TaskUI/Electrical/yellow_middle.png"));
		buttonYellowMid = new Image(buttonYM);
		buttonYellowMid.setPosition(634, 606);

		buttonB = new Sprite(new Texture("TaskUI/Electrical/blue.png"));
		buttonBlueLeft = new Image(buttonB);
		buttonBlueLeft.setHeight(45);
		buttonBlueLeft.setPosition(514, 416);

		buttonBlueRight = new Image(buttonB);
		buttonBlueRight.setHeight(45);
		buttonBlueRight.setPosition(1286, 416);

		buttonBlueMid = new Image(buttonB);
		buttonBlueMid.setHeight(45);
		buttonBlueMid.setWidth(652);
		buttonBlueMid.setPosition(634, 416);

		buttonG = new Sprite(new Texture("TaskUI/Electrical/green.png"));
		buttonGreenLeft = new Image(buttonG);
		buttonGreenLeft.setHeight(45);
		buttonGreenLeft.setPosition(514, 236);

		buttonGreenRight = new Image(buttonG);
		buttonGreenRight.setHeight(45);
		buttonGreenRight.setPosition(1286, 606);

		buttonGM = new Sprite(new Texture("TaskUI/Electrical/green_middle.png"));
		buttonGreenMid = new Image(buttonGM);
		buttonGreenMid.setPosition(634, 236);

		stage.addActor(elecbackImg);
		stage.addActor(buttonRedLeft);
		stage.addActor(buttonYellowLeft);
		stage.addActor(buttonBlueLeft);
		stage.addActor(buttonGreenLeft);
		stage.addActor(buttonRedRight);
		stage.addActor(buttonYellowRight);
		stage.addActor(buttonBlueRight);
		stage.addActor(buttonGreenRight);

		buttonRedLeft.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				test = "red";
				System.out.println(test);
				System.out.println();
			}
		});

		buttonYellowLeft.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				test = "yellow";
				System.out.println(test);				
			}
		});

		buttonBlueLeft.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				test = "blue";
				System.out.println(test);

			}
		});

		buttonGreenLeft.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				test = "green";
				System.out.println(test);

			}
		});

		buttonRedRight.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if(test == "red") {

					test = "-";

					RedWire = true;

					stage.addActor(buttonRedMid);

					System.out.println("Correct");

					buttonRedRight.setTouchable(Touchable.disabled);
					buttonRedLeft.setTouchable(Touchable.disabled);	

					if(RedWire && BlueWire && GreenWire && YellowWire) {

						done = true;
					}
				}
				else {
					test = "-";
					System.out.println("Wrong");				
				}
			}
		});

		buttonYellowRight.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if(test == "yellow") {

					test = "-";

					YellowWire = true;

					stage.addActor(buttonYellowMid);

					System.out.println("Correct");

					buttonYellowRight.setTouchable(Touchable.disabled);
					buttonYellowLeft.setTouchable(Touchable.disabled);	

					if(RedWire && BlueWire && GreenWire && YellowWire) {

						done = true;
					}
				}
				else {
					test = "-";
					System.out.println("Wrong");				
				}

			}
		});

		buttonBlueRight.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if(test == "blue") {

					test = "-";

					BlueWire = true;

					stage.addActor(buttonBlueMid);

					System.out.println("Correct");

					buttonBlueRight.setTouchable(Touchable.disabled);
					buttonBlueLeft.setTouchable(Touchable.disabled);	

					if(RedWire && BlueWire && GreenWire && YellowWire) {

						done = true;
					}
				}
				else {
					test = "-";
					System.out.println("Wrong");				
				}

			}
		});

		buttonGreenRight.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if(test == "green") {

					test = "-";

					GreenWire = true;

					stage.addActor(buttonGreenMid);

					System.out.println("Correct");

					buttonGreenRight.setTouchable(Touchable.disabled);
					buttonGreenLeft.setTouchable(Touchable.disabled);	

					if(RedWire && BlueWire && GreenWire && YellowWire) {

						done = true;
					}
				}
				else {
					test = "-";
					System.out.println("Wrong");				
				}

			}
		});
	}

	public void render(Batch batch) {
		(Client.getPlayer()).draw(batch);
		
		Gdx.input.setInputProcessor(stage);
		stage.draw();

		if(done) {
			System.out.println("SUCCESS!");
			
			GameScreen.light.setDistance(180);

			if(!sentFixed) {
				try {
					Client.sendLightsFixed();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sentFixed = true;
			}

			if(Client.getPlayer() instanceof CrewMember) {
				((CrewMember) Client.getPlayer()).setCurrentTask(null);
			}
			else{
				((Imposter) Client.getPlayer()).setCurrentTask(null);
			}
		}
	}
}
