package com.mmog.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class RoleUI {
	private Stage stage;
	BitmapFont font = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
	Table table;
	LabelStyle labelFontStyle = new LabelStyle(font, Color.WHITE);
	
	//bg sprite
	Sprite bg = new Sprite(new Texture("UI/killBG.png"));
	Image bgImg = new Image(bg);
	
	public RoleUI() {
		stage = new Stage();
		font.getData().setScale(0.1f);
		Gdx.input.setInputProcessor(stage);
		
		//table setting sizes
		table = new Table();
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);
		
		table.add(bgImg);
	}
	
	public void drawCrewBanner() {
		Label playerNameLabel = new Label("YOU'RE A CREW MEMBER! COMPLETE TASKS TO WIN", labelFontStyle);
		
		table.add(playerNameLabel);
		
		stage.addActor(table);
		stage.act();
		stage.draw();
		
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
		}
	}

	public void drawImposterBanner() {
		Label playerNameLabel = new Label("YOU'RE AN IMPOSTER! SABOTAGE AND KILL TO WIN", labelFontStyle);
		
		table.add(playerNameLabel);
		
		stage.addActor(table);
		stage.act();
		stage.draw();
		
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
		}
	}
}
