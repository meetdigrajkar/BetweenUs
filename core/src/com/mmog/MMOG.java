package com.mmog;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class MMOG extends Game {
	private SpriteBatch batch;
	private Player player;
	
	GameScreen gameScreen;
	Stage stage;
	TextButton button;
	TextButtonStyle textButtonStyle;
	BitmapFont font;
	
	
	@Override
	public void create () {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		font = new BitmapFont();
		textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.RED;
        textButtonStyle.font.getData().setScale(10);
        textButtonStyle.downFontColor = Color.BLUE;
        button = new TextButton("Join", textButtonStyle);
        button.pad(100);
        stage.addActor(button);
        
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Attempting to Join...");
                Client.connectClientToServer();
            }
        });
        
		batch = new SpriteBatch();
		player = new Player();
		
		//uncomment when refactored code in this class into seperate scenes
		//ScreenManager.getInstance().initialize(this);
		//ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		player.render(batch);
		stage.draw();
		batch.end();
	}


	@Override
	public void dispose () {
		gameScreen.dispose();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}

	@Override
	public void pause() {

	}

	@Override 
	public void resume() {

	}
}
