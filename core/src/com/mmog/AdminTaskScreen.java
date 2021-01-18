package com.mmog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

public class AdminTaskScreen extends AbstractScreen{

	Table table;
	TextField crewMemberID;

	public AdminTaskScreen() {
		super();
	}

	@Override
	public void buildStage() {
		table = new Table();
		table.setFillParent(true);

		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.FIREBRICK);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		Label crewMemberIDLabel = new Label("Crew Member Name:", ls );
		crewMemberID = new TextField("", uiSkin);
		table.add(crewMemberIDLabel);
		table.add(crewMemberID).width(50);
		addActor(table);
		table.setPosition(50,50);
	}

	@Override
	public void render(float delta) {
		draw();

		if(AdminTask.validateCrewMemberID(crewMemberID.getText())) {
			System.out.println("TASK COMPLETED!");
			
			MainScreen.player.setTaskCompleted("Admin Task");
			ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
		}

	}

}
