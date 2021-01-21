package com.mmog.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mmog.players.CrewMember;
import com.mmog.screens.MainScreen;

public class AdminTask extends Task{

	final static String taskName = "Admin Task";
	Table table;
	TextField crewMemberID;
	Stage stage;
	
	public AdminTask() {
		super(taskName);
		
		table = new Table();
		stage = new Stage();
		table.setFillParent(true);
		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.FIREBRICK);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		Label crewMemberIDLabel = new Label("Crew Member Name:", ls );
		crewMemberID = new TextField("", uiSkin);
		
		table.add(crewMemberIDLabel);
		table.add(crewMemberID).width(150);
		
		stage.addActor(table);
		table.setPosition(50,50);
	}
	
	public boolean validateCrewMemberID(String playerName) {
		if(MainScreen.player.getPlayerName().equals(playerName)) {
			return true;
		}
		return false;
	}
	
	public void render(Batch batch) {
		((CrewMember) MainScreen.player).draw(batch);
		stage.draw();
		
		Gdx.input.setInputProcessor(stage);
		
		if(validateCrewMemberID(crewMemberID.getText())) {
			System.out.println("TASK COMPLETED!");
			((CrewMember) MainScreen.player).setTaskCompleted("Admin Task");
			((CrewMember) MainScreen.player).setCurrentTask(null);
		}
	}
}
