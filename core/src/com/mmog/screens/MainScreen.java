package com.mmog.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mmog.Client;


public class MainScreen extends AbstractScreen{
	TextButtonStyle tbs;
	boolean joined;
	Table table;
	Table table1;
	public static TextField playerName;
	SpriteBatch batch;

	public MainScreen() {
		// TODO Auto-generated constructor stub
		super();
	}

	public void show() {
		// TODO Auto-generated method stub
	}


	public void buildStage() {
		tbs = new TextButtonStyle();

		//make fonts here
		BitmapFont font = new BitmapFont(Gdx.files.internal("UI/textf.fnt"));
		BitmapFont gameTitleFont = new BitmapFont(Gdx.files.internal("UI/gameText.fnt"));
		BitmapFont labelFont = new BitmapFont(Gdx.files.internal("UI/labelFont.fnt"));

		//make label styles here
		LabelStyle ls = new LabelStyle(font, Color.MAGENTA);
		LabelStyle gameTitleFontLabel = new LabelStyle(gameTitleFont, Color.WHITE);
		LabelStyle labelFontStyle = new LabelStyle(labelFont, Color.BLACK);

		tbs.font = font;
		joined = false;

		final TextureRegionDrawable bg = new TextureRegionDrawable(new Texture("UI/background2.png"));
		final TextureRegionDrawable textbox = new TextureRegionDrawable(new Texture("UI/textbox.png"));

		table = new Table();
		table.setFillParent(true);
		table.setBackground(bg);

		//make labels here
		Label playerNameLabel = new Label("Player Name", labelFontStyle);
		Label gameLabel = new Label("Between Us", gameTitleFontLabel);

		//make text button styles here
		TextButton.TextButtonStyle textbs = new TextButton.TextButtonStyle();
		textbs.font = font;

		//make buttons here
		TextButton button1 = new TextButton("Join Room", textbs);
		TextButton button2 = new TextButton("Create Room", textbs);
		TextButton button3 = new TextButton("Settings",textbs);
		TextButton button4 = new TextButton("About",textbs);

		TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
		tfs.font = font;
		tfs.background = textbox;
		tfs.fontColor = new Color(Color.PURPLE);

		//table setting sizes
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);

		playerName = new TextField("", tfs);
		playerName.setAlignment(Align.center);

		//table items go here
		table.center().top();
		table.add(gameLabel).padLeft(40);
		table.row();
		table.add(playerNameLabel).padBottom(5).padLeft(40);
		table.row();
		table.add(playerName).width(100).height(25).padBottom(5).padLeft(40);
		table.row().center();
		table.add(button1).width(90).padLeft(40);
		table.row().center();
		table.add(button2).width(90).padLeft(40);

		table.row();
		table.add(button3).padRight(200);
		table.add(button4);

		addActor(table);

		//add button listeners here
		button1.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Heading to Join Room Screen!");
				
				if(Client.getPlayer() == null || Client.getPlayers() == null) {
					//create all the players and the local player
					Client.createPlayer(MainScreen.playerName.getText());
					Client.createPlayers();
				}

				if(!Client.getPlayer().getPlayerName().equals("")) {
					ScreenManager.getInstance().showScreen(ScreenEnum.JOIN_SCREEN);
				}
			}
		});

		//create room button listener
		button2.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Heading to Create Room Screen!");
				
				if(Client.getPlayer() == null || Client.getPlayers() == null) {
					//create all the players and the local player
					Client.createPlayer(MainScreen.playerName.getText());
					Client.createPlayers();
				}
				
				if(!Client.getPlayer().getPlayerName().equals("")) {
					ScreenManager.getInstance().showScreen(ScreenEnum.CREATE_ROOM);
				}
			}
		});

		//settings button listener
		button3.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Heading to Settings from menu");
				//ScreenManager.getInstance().showScreen(ScreenEnum.SETTINGS);
			}
		});

		//about button listener
		button4.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Heading to About from menu");
				//ScreenManager.getInstance().showScreen(ScreenEnum.SETTINGS);=
			}
		});
		Gdx.input.setInputProcessor(this);
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		draw();

	}

	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	public void pause() {
		// TODO Auto-generated method stub

	}

	public void resume() {
		// TODO Auto-generated method stub

	}


	public void hide() {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

}