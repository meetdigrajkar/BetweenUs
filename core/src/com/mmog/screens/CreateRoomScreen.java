package com.mmog.screens;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter.DigitsOnlyFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mmog.Client;

public class CreateRoomScreen extends AbstractScreen {
	Table table;
	TextField roomName,crewMembers,imposters;
	int numCrew,numImp;
	SpriteBatch batch;
	
	Animation<TextureRegion> animation;
	float elapsedTime;
	

	public CreateRoomScreen() {
		super();
		//sprite batch
		batch = new SpriteBatch();
	}

	@Override
	public void buildStage() {
		Gdx.input.setInputProcessor(this);

		//make fonts here
		BitmapFont font = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
		BitmapFont gameTitleFont = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
		BitmapFont labelFont = new BitmapFont(Gdx.files.internal("UI/labelFont.fnt"));

		//resizing fonts
		font.getData().setScale(0.1f);
		gameTitleFont.getData().setScale(0.2f);
		
		//make label styles here
		LabelStyle lsC = new LabelStyle(font, Color.GREEN);
		LabelStyle lsI = new LabelStyle(font, Color.RED);
		LabelStyle gameTitleFontLabel = new LabelStyle(gameTitleFont, Color.WHITE);
		LabelStyle labelFontStyle = new LabelStyle(font, Color.WHITE);

		//textures go here
		final TextureRegionDrawable bg = new TextureRegionDrawable(new Texture("UI/background2.png"));
		final TextureRegionDrawable textbox = new TextureRegionDrawable(new Texture("UI/textbox.png"));

		//init table and setup background
		table = new Table();
		table.setFillParent(true);

		//table setting sizes
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);

		//make labels here
		Label numOfCrewMembersLabel = new Label("# Crew Members", lsC);
		Label numOfImpostersLabel = new Label("# Imposters", lsI);
		Label roomLabel = new Label("Room Name", labelFontStyle);
		Label createRoomLabel = new Label("Creating A Room", gameTitleFontLabel);
		Label createRoom = new Label("Create", lsC);
		Label backToMain = new Label("To Main", lsI);

		//text field for room name
		TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
		tfs.font = font;
		tfs.background = textbox;
		tfs.fontColor = new Color(Color.PURPLE);

		//room name
		roomName = new TextField("", tfs);
		roomName.setAlignment(Align.center);

		//number fields
		crewMembers = new TextField("",tfs);
		crewMembers.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());

		imposters = new TextField("",tfs);
		imposters.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());

		crewMembers.setAlignment(Align.center);
		imposters.setAlignment(Align.center);

		//add to the table
		//table items go here
		table.center().top();
		table.add(createRoomLabel).padRight(20).padBottom(20);
		table.row();
		table.add(numOfCrewMembersLabel);
		table.add(crewMembers).width(50).height(25);
		table.row();
		table.add(numOfImpostersLabel);
		table.add(imposters).width(50).height(25);
		table.row();
		table.add(roomLabel);
		table.add(roomName).width(100).height(25);
		table.row().padBottom(60);
		table.add(createRoom).center().padLeft(150).padTop(50).padBottom(40);
		table.row();
		table.add(backToMain).left();

		addActor(table);

		//create room button listener
		createRoom.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if((!roomName.getText().contains(",")) && !roomName.getText().isEmpty()) {
					System.out.println("Created A Room");
					numCrew = Integer.parseInt(crewMembers.getText());
					numImp = Integer.parseInt(imposters.getText());

					if((numCrew >= 0 && numCrew <= 10) && (numImp >= 0 && numImp <= 2)) {
						try {
							Client.getPlayer().connectedRoomName = roomName.getText();
							System.out.println("Created a Room with The Room Name: " + roomName.getText() + " c: " + numCrew  + " i: " + numImp);
							
							//make the local player a host.
							Client.getPlayer().isHost = true;
							
							Client.sendCreateRoomCommand(roomName.getText(),numCrew,numImp);
							ScreenManager.getInstance().showScreen(ScreenEnum.LOBBY_SCREEN);	
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});

		//back to main listener

		backToMain.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Back To Main Screen!");
				ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
			}
		});
		
		animation = MainScreen.createBackgroundAnimation(this.animation);
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		elapsedTime += Gdx.graphics.getDeltaTime();
		//System.out.println(elapsedTime);
		
		if(elapsedTime > 3f) {
			elapsedTime = 0f;
		}
		
		batch.begin();
		batch.draw(animation.getKeyFrame(elapsedTime),0,0);
		batch.end();
		
		
		
		draw();
	}

	public void resize(int width, int height) {
	}

	public void dispose() {
	}

}