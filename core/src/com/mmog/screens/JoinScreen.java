package com.mmog.screens;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter.DigitsOnlyFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mmog.Client;

public class JoinScreen extends AbstractScreen {
	Table table;
	SpriteBatch batch;

	//textures go here
	final TextureRegionDrawable bg = new TextureRegionDrawable(new Texture("UI/background2.png"));
	final TextureRegionDrawable textbox = new TextureRegionDrawable(new Texture("UI/textbox.png"));

	//make fonts here
	BitmapFont font = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
	BitmapFont gameTitleFont = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
	BitmapFont labelFont = new BitmapFont(Gdx.files.internal("UI/labelFont.fnt"));

	static SelectBox<String> rooms;
	Skin skin =new Skin(Gdx.files.internal("skins/uiskin.json"));
	
	String selectedRoom = "";
	
	Animation<TextureRegion> animation;
	float elapsedTime;
	
	public JoinScreen() {
		super();
		rooms = new SelectBox<String>(skin);
		//sprite batch
		batch = new SpriteBatch();
	}

	@Override
	public void buildStage() {
		//resizing fonts
		font.getData().setScale(0.1f);
		gameTitleFont.getData().setScale(0.2f);
		
		//make label styles here
		LabelStyle lsC = new LabelStyle(font, Color.GREEN);
		LabelStyle lsI = new LabelStyle(font, Color.RED);
		LabelStyle gameTitleFontLabel = new LabelStyle(gameTitleFont, Color.WHITE);
		LabelStyle labelFontStyle = new LabelStyle(labelFont, Color.BLACK);

		//init table and setup background
		table = new Table();
		table.setFillParent(true);

		//table setting sizes
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);

		//make labels here
		Label availableRoomsLabel = new Label("Available Rooms", lsC);
		Label roomLabel = new Label("Room Name", labelFontStyle);
		Label joinRoomLabel = new Label("Join A Room", gameTitleFontLabel);
		Label joinRoom = new Label("Join", lsC);
		Label backToMain = new Label("To Main", lsI);
		Label refresh = new Label("Refresh", lsI);

		//add to the table
		//table items go here
		table.center().top();
		table.add(joinRoomLabel).padLeft(30).padBottom(30);
		table.row();
		table.add(availableRoomsLabel).padLeft(30).padBottom(10);
		table.row();
		
		table.add(rooms).width(150).height(25).padLeft(30);
		
		table.row();
		table.add(joinRoom).padLeft(30).padTop(55).padBottom(60);
		table.row();
		table.add(backToMain).left();
		table.add(refresh).right();	
		
		//room change listener
		rooms.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				System.out.println("Changing Selected Room...");
				selectedRoom = (String) ((SelectBox)actor).getSelected();
			}
		});	
		
		//join room button listener
		joinRoom.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Joining A Room...");
				
				if(!selectedRoom.equals("")) {					
					Client.getPlayer().connectedRoomName = selectedRoom;
					
					try {
						Client.connectClientToServer();
						ScreenManager.getInstance().showScreen(ScreenEnum.LOBBY_SCREEN);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		//refresh available rooms list
		refresh.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Refreshed!");
				try {
					Client.sendRefreshCommand();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		
		Gdx.input.setInputProcessor(this);
		animation = MainScreen.createBackgroundAnimation(this.animation);
		
		//addActor(rooms);
		addActor(table);
	}

	public static void addRoom(String roomToAdd) {
		rooms.getItems().add(roomToAdd);
		rooms.setItems(rooms.getItems());
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
		this.act();
	}

	public void resize(int width, int height) {
	}

	public void dispose() {
	}

}