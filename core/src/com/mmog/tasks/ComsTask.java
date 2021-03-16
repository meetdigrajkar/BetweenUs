package com.mmog.tasks;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mmog.Client;
import com.mmog.players.CrewMember;

public class ComsTask extends Task {
	final static String taskName = "Coms Task";
	float threshold = 0;
	float oldY[] = new float[0];
	int timeLeft = 10;
	boolean completed = false;
	boolean updatingLabel = false;
	Stage stage;
	Sprite wifiLever, wifiPanel, sideLid, hinge;
	final Image wifiLeverImg, wifiPanelImg, sideLidImg, hingeImg;  
	Label modemLabel;
	
	
	public ComsTask() {
		super(taskName);
		
		stage = new Stage();
		
		wifiLever = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi-lever.png"));
		wifiPanel = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi_bg.png"));
		sideLid = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi_lidside.png"));
		hinge = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi_closingthing.png"));
		
		
		wifiLeverImg = new Image(wifiLever);
		wifiPanelImg = new Image(wifiPanel);
		sideLidImg = new Image(sideLid);
		hingeImg = new Image(hinge);
		
		stage.addActor(wifiPanelImg);
		stage.addActor(sideLidImg);
		stage.addActor(hingeImg);
		
		wifiPanelImg.setPosition(stage.getWidth() /2 - 200, stage.getHeight() / 2 - 250);	
		sideLidImg.setPosition(stage.getWidth() /2 - 390, stage.getHeight() / 2 - 290);
		hingeImg.setPosition(stage.getWidth() /2 - 260, stage.getHeight() / 2 - 290);
		
		
		Texture leverBackground = new Texture("TaskUI/Reset Modem/sliderBackground.PNG");
		Texture leverKnob = new Texture ("TaskUI/Reset Modem/panel_wifi-lever.png");
		
		Slider.SliderStyle ss = new Slider.SliderStyle();
		ss.background = new TextureRegionDrawable(new TextureRegion(leverBackground));
		ss.knob = new TextureRegionDrawable(new TextureRegion(leverKnob));
		
		final Slider lever = new Slider(0f, 100f, 1f, true,ss);	
		
		Table t = new Table();
		t.add(lever).width(80).height(363);
		t.setPosition(stage.getWidth() /2 + 65, stage.getHeight() / 2 - 190);
		t.pack();
		
		
		stage.addActor(t);
		
		LabelStyle ls = new LabelStyle(new BitmapFont(), Color.WHITE);
		modemLabel = new Label("Please Turn On Your Modem", ls);
		stage.addActor(modemLabel);
		modemLabel.setPosition(stage.getWidth() /2 - 145, stage.getHeight() / 2 + 165);
		
		
		
		
		lever.addListener(new DragListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				return true;
			}
			
			@Override
			public void touchDragged(InputEvent e, float x, float y, int pointer) {
				//System.out.println(lever.getPercent());					
			}
			
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (lever.getPercent() == 1.0) {
					lever.setDisabled(true);
					long timeCompleted = System.currentTimeMillis()/ 1000;
					modemLabel.setText("     Please wait 5 seconds");
					while ((System.currentTimeMillis()/1000)  - timeCompleted  < 5) {
						
					}	
					completed = true;
				}
				
			}
		});
	}
	
	public void render(Batch batch) {
		((CrewMember) Client.getPlayer()).draw(batch);
		Gdx.input.setInputProcessor(stage);
		stage.draw();
		
	
		if(completed) {
			System.out.println("SUCCESS!");
			((CrewMember) Client.getPlayer()).setCurrentTask(null);
			((CrewMember) Client.getPlayer()).setTaskCompleted(taskName);
		}
		
	
	}

}