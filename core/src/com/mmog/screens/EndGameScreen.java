package com.mmog.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;

public class EndGameScreen extends AbstractScreen{
	Table table;

	public EndGameScreen() {
		super();
	}

	@Override
	public void buildStage() {
		Gdx.input.setInputProcessor(this);

		//table setting sizes
		table = new Table();
		table.setFillParent(true);
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		
		table.setSize(MAX_WIDTH, MAX_HEIGTH);
		
		BitmapFont font = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
		LabelStyle labelFontStyle = new LabelStyle(font, Color.BLACK);
		font.getData().setScale(0.08f);
		
		TextureRegionDrawable crewbg = new TextureRegionDrawable(new Texture("UI/crew_win.jpg"));
		TextureRegionDrawable impbg = new TextureRegionDrawable(new Texture("UI/imposters_win.jpg"));

		Label p = new Label("Press ESCAPE to Exit..", labelFontStyle);
		
		table.center();
		if(GameScreen.crewWin) {
			table.background(crewbg);
			
			BitmapFont f = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
			f.getData().setScale(0.2f);
			LabelStyle lsf = new LabelStyle(f, Color.GREEN);
			Label c = new Label("Crew Mate Win!", lsf);
			
			table.add(c);
		}
		else if(GameScreen.imposterWin) {
			table.background(impbg);
			
			BitmapFont fl = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
			fl.getData().setScale(0.2f);
			LabelStyle lsfl = new LabelStyle(fl, Color.RED);
			Label i = new Label("Imposter Win!", lsfl);
			
			table.add(i);
		}
		
		table.row();
		table.add(p).padTop(50);
		addActor(table);
	}
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		draw();
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) && Client.getPlayer() != null) {
			System.out.println("EXITING GAME!");
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		}
	}
	
}
