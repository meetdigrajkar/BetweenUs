package com.mmog.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.mmog.Client;
import com.mmog.players.CrewMember;

public class ComsTask extends Task {
	final static String taskName = "Coms Task";
	
	Stage stage;
	Sprite wifiLever, wifiPanel, sideLid, hinge;
	final Image wifiLeverImg, wifiPanelImg, sideLidImg, hingeImg;  
	
	
	public ComsTask() {
		super(taskName);
		
		//skip nodes for now - since i need multiple copies of it and have to design pseudo code to iterate seamlessly
		
		stage = new Stage();
		
		wifiLever = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi-lever.png"));
		wifiPanel = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi_bg.png"));
		sideLid = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi_lidside.png"));
		hinge = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi_closingthing.png"));
		
		
		
		//Resizing because of camera zoom
		//wifiLever.setSize(0, 0);
		//wifiPanel.setSize(0, 0);
		//sideLid.setSize(0, 0);
		
		
		wifiLeverImg = new Image(wifiLever);
		wifiPanelImg = new Image(wifiPanel);
		sideLidImg = new Image(sideLid);
		hingeImg = new Image(hinge);
		
		stage.addActor(wifiPanelImg);
		stage.addActor(wifiLeverImg);
		stage.addActor(sideLidImg);
		stage.addActor(hingeImg);
		
		wifiPanelImg.setPosition(stage.getWidth() /2 - 200, stage.getHeight() / 2 - 250);	
		wifiLeverImg.setPosition(stage.getWidth() /2 + 75, stage.getHeight() / 2 + 135);
		sideLidImg.setPosition(stage.getWidth() /2 - 390, stage.getHeight() / 2 - 290);
		hingeImg.setPosition(stage.getWidth() /2 - 260, stage.getHeight() / 2 - 290);
		
		
		
		//Below is logic for lever 
		
		wifiLeverImg.addListener(new DragListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchDragged(InputEvent e, float x, float y, int pointer) {
				wifiLeverImg.setPosition(wifiLeverImg.getX(), wifiLeverImg.getY() - y);
				
			}
			
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
			
		});
	}
	
	
	public void render(Batch batch) {
		((CrewMember) Client.getPlayer()).draw(batch);
		Gdx.input.setInputProcessor(stage);
		stage.draw();
		
		/*
		if(completed) {
			System.out.println("SUCCESS!");
			((CrewMember) Client.getPlayer()).setCurrentTask(null);
			((CrewMember) Client.getPlayer()).setTaskCompleted(taskName);
		}
		
		*/
	}

}
