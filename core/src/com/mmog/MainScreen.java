package com.mmog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MainScreen extends AbstractScreen{
	private Texture btn;
	TextButtonStyle tbs;
	BitmapFont font;
	boolean joined;
	Table table;
	TextField playerName;
	public static Player player;
	
	public MainScreen() {
		// TODO Auto-generated constructor stub
		super();
	}


	public void show() {
		// TODO Auto-generated method stub
		Sprite p = new Sprite(new Texture("idle.png"));
		p.setSize(32, 50);
		player = new Player(p, -1);
		
		//ADD TASKS HERE
		player.getTasks().add(new AdminTask());
	}


	public void buildStage() {
		tbs = new TextButtonStyle();
		font = new BitmapFont();
		tbs.font = font;
		TextButton button = new TextButton("Join", tbs);
		joined = false;

		button.setPosition(150, 50, Align.center);
		addActor(button);
		
		table = new Table();
		table.setFillParent(true);

		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.FIREBRICK);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		Label playerNameLabel = new Label("Player Name:", ls );
		playerName = new TextField("", uiSkin);
		table.add(playerNameLabel);
		table.add(playerName).width(150);
		//System.out.println(GameScreen.player.getPlayerID());
		addActor(table);
		
		button.addListener( new ClickListener() {              
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!joined) {
					System.out.println("Attempting to Connect to The Server...");
					Client.connectClientToServer();
					player.setPlayerName(playerName.getText());
					ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
				}
				joined = true;
			};
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
