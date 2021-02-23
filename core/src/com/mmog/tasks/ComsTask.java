package com.mmog.tasks;

import java.util.concurrent.TimeUnit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.mmog.Client;
import com.mmog.players.CrewMember;

public class ComsTask extends Task {
	final static String taskName = "Coms Task";
	float threshold = 0;
	float oldY[] = new float[0];
	int timeLeft = 10;
	boolean completed = false;
	Stage stage;
	Sprite wifiLever, wifiPanel, sideLid, hinge;
	final Image wifiLeverImg, wifiPanelImg, sideLidImg, hingeImg;  
	private long startTime = 0, elapsedTime = 0;
	
	
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
		//stage.addActor(wifiLeverImg);
		stage.addActor(sideLidImg);
		stage.addActor(hingeImg);
		
		wifiPanelImg.setPosition(stage.getWidth() /2 - 200, stage.getHeight() / 2 - 250);	
		//wifiLeverImg.setPosition(stage.getWidth() /2 + 75, stage.getHeight() / 2 + 135);
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
		final Label modemLabel = new Label("Please Turn On Your Modem", ls);
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
					while (timeLeft > 0) {
						modemLabel.setText("Please wait " + timeLeft + " seconds.");
						System.out.println("Please wait " + timeLeft + " seconds.");
						System.out.println(" ");
						timeLeft = timeLeft -1;
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
				}
				completed = true;
			}
		});
		
	}
	
	
	public void render(Batch batch) {
		(Client.getPlayer()).draw(batch);
		Gdx.input.setInputProcessor(stage);
	
		if(completed) {
			if(Client.getPlayer() instanceof CrewMember) {
				((CrewMember) Client.getPlayer()).setCurrentTask(null);
				((CrewMember) Client.getPlayer()).setTaskCompleted(taskName);
			}
		}
		else
			stage.draw();
		
	
	}

}
