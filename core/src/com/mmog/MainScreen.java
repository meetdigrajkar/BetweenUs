package com.mmog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreen extends AbstractScreen{
	private Player player;
	TextButton button;
	TextButtonStyle textButtonStyle;
	BitmapFont font;
	SpriteBatch batch;
	
	public MainScreen() {
		// TODO Auto-generated constructor stub
		super();
	}


	public void show() {
		// TODO Auto-generated method stub
	}

	
	public void buildStage() {
		font = new BitmapFont();
		textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        button = new TextButton("Join", textButtonStyle);
        addActor(button);
        
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Attempting to Join...");
                Client.connectClientToServer();
            }
        });
        
		batch = new SpriteBatch();
		player = new Player();
	}
	
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		player.render(batch);
		draw();
		batch.end();
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
		batch.dispose();
	}
	
}
