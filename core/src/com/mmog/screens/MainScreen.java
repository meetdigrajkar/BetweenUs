package com.mmog.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Player;
import com.mmog.tasks.*;

public class MainScreen extends AbstractScreen{
	TextButtonStyle tbs;
	TextButtonStyle style1;
	TextButtonStyle style2;
	TextButtonStyle style3;
	TextButtonStyle style4;
	BitmapFont font;
	boolean joined;
	Table table;
    Table table1;
	TextField playerName;
	public static Player player;
	private TextureAtlas buttonsAtlas;
	private Random r;

	public MainScreen() {
		// TODO Auto-generated constructor stub
		super();
	}

	public void show() {
		// TODO Auto-generated method stub
		Sprite p = new Sprite(new Texture("idle.png"));
		p.setSize(32, 50);
		
		//set the player to a crew member or an imposter based on a random number generator
		r = new Random();
		int upperBound = 100;
		int playerTypeSelector = r.nextInt(upperBound);
		
		//if the random number is less than 50, player is a crew member
		//if the random number is greater than 50, player is an imposter
		
		//if(playerTypeSelector < 50) {
		//	player = new CrewMember(p, -1);
			
			//ADD TASKS HERE
		//	((CrewMember) player).addTask(new AdminTask());
		//}else if(playerTypeSelector > 50){
		//	player = new Imposter(p, -1);
		//}
		
		player = new CrewMember(p, -1);
		((CrewMember) player).addTask(new AdminTask());
		((CrewMember) player).addTask(new ReactorTask());
		((CrewMember) player).addTask(new ComsTask());
		((CrewMember) player).addTask(new LabTask());
		((CrewMember) player).addTask(new ElectricalTask());
		
	}


	public void buildStage() {
		tbs = new TextButtonStyle();
		font = new BitmapFont();
		tbs.font = font;
		TextButton button = new TextButton("Join", tbs);
		joined = false;

		//button.setPosition(150, 50, Align.center);

		table = new Table();
		table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new Texture("Menu_blank.png")));
		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.FIREBRICK);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		buttonsAtlas = new TextureAtlas(Gdx.files.internal("Buttons.atlas"));
		Skin buttonskin = new Skin(buttonsAtlas);
/*
		ImageButton.ImageButtonStyle stylebig = new ImageButton.ImageButtonStyle();
		stylebig.up = buttonskin.getDrawable("buttonbign");
		stylebig.down = buttonskin.getDrawable("buttonbigp");
		stylebig.over = buttonskin.getDrawable("buttonbigh");
		ImageButton buttonBig = new ImageButton(stylebig);
*/
		style1 = new TextButtonStyle();
		style1.font = font;
		style1.up = new TextureRegionDrawable(new Texture("button2n.png"));
		style1.down = new TextureRegionDrawable(new Texture("button1p.png"));
		style1.over = new TextureRegionDrawable(new Texture("button1h.png"));
		TextButton button1 = new TextButton("Join", style1);

		style2 = new TextButtonStyle();
		style2.font = font;
		style2.up = new TextureRegionDrawable(new Texture("button2n.png"));
		style2.down = new TextureRegionDrawable(new Texture("button2p.png"));
		style2.over = new TextureRegionDrawable(new Texture("button2h.png"));
		TextButton button2 = new TextButton("Settings", style1);

		Button.ButtonStyle style3 = new Button.ButtonStyle();
		style3.up = buttonskin.getDrawable("button3n");
		style3.down = buttonskin.getDrawable("button3p");
		style3.over = buttonskin.getDrawable("button3h");
		Button button3 = new Button(style2);

		Button.ButtonStyle style4 = new Button.ButtonStyle();
		style4.up = buttonskin.getDrawable("button4n");
		style4.down = buttonskin.getDrawable("button4p");
		style4.over = buttonskin.getDrawable("button4h");
		Button button4 = new Button(style2);

        Table table1 = new Table();
		Label playerNameLabel = new Label("Player Name:", ls );
		playerName = new TextField("", uiSkin);
		table.center().top();
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);

		//table.add(playerNameLabel);
		//table.add(playerName).width(150);
		//table.debugAll();
		table.row().padTop(100);
		table.add(button1).width(100).height(40);
		table.add(button2).width(100).height(40).padLeft(10);
		table.row().padTop(10);
		table.add(button3).width(100).height(40);
		table.add(button4).width(100).height(40).padLeft(10);
		table.row().padTop(10);
		table.add(playerNameLabel);
		table.add(playerName).width(100).height(20);


		//System.out.println(GameScreen.player.getPlayerID());
		addActor(table);
		//ddActor(button);
		
		button1.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!joined) {
					System.out.println("Attempting to Connect to The Server...");
					Client.connectClientToServer();
					player.setPlayerName(playerName.getText());
					ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
				}
				joined = true;
			}
		});

		button2.addListener( new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!joined) {
					System.out.println("Heading to Settings from menu");
					//ScreenManager.getInstance().showScreen(ScreenEnum.SETTINGS);
				}
				joined = true;
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
