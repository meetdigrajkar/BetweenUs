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

public class RoleUI extends AbstractScreen{
	Table table;

	public RoleUI() {
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
		
		TextureRegionDrawable bg = new TextureRegionDrawable(new Texture("UI/killBG.png"));

		Label imp = new Label("You're an Imposter! Sabotage and Kill.", labelFontStyle);
		Label crew = new Label("You're a Crew Member! Complete Tasks and Find the Imposters.", labelFontStyle);
		Label p = new Label("Press P to Continue..", labelFontStyle);
		
		table.background(bg);
		
		if(Client.getPlayer() instanceof CrewMember) {
			table.add(crew).center();
		}
		else if(Client.getPlayer() instanceof Imposter) {
			table.add(imp).center();
		}
		
		table.row();
		table.add(p).padTop(10);
		addActor(table);
	}
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		draw();
		
		if(Gdx.input.isKeyPressed(Keys.P)) {
			System.out.println("ENTERING GAME!");
			ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
		}
	}
	
}
