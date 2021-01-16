package com.mmog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MainScreen extends AbstractScreen{
	private Texture btn;
	TextButtonStyle tbs;
	BitmapFont font;
	
	public MainScreen() {
		// TODO Auto-generated constructor stub
		super();
	}


	public void show() {
		// TODO Auto-generated method stub
	}

	
	public void buildStage() {
		tbs = new TextButtonStyle();
		font = new BitmapFont();
		tbs.font = font;
		TextButton button = new TextButton("Join", tbs);
      
		button.setPosition(100, 100, Align.center);
        addActor(button);
        
        button.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
               System.out.println("Attempting to Connect to The Server...");
               Client.connectClientToServer();
               ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
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
