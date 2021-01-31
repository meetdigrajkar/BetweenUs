package com.mmog.tasks;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.mmog.Client;
import com.mmog.players.CrewMember;

public class ComsTask extends Task {
	final static String taskName = "Coms Task";
	float oldY;
	float totalTime;
	
	boolean turnedOff;
	boolean turningOn;
	boolean turningOff = true;
	Stage stage;
	Sprite wifiLever, wifiPanel, sideLid, hinge;
	final Image wifiLeverImg, wifiPanelImg, sideLidImg, hingeImg;  
	
	private long startTime = 0, elapsedTime = 0;
	
	
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
		//ss.background.setMinHeight(443);
		//ss.background.setMinWidth(80);
		ss.knob = new TextureRegionDrawable(new TextureRegion(leverKnob));
		
		Slider lever = new Slider(0f, 100f, 1f, true,ss);
	
		
		
		Table t = new Table();
		t.add(lever).width(80).height(363);
		t.setPosition(stage.getWidth() /2 + 65, stage.getHeight() / 2 - 190);
		t.pack();
		
		
		stage.addActor(t);
		
		
		
		
		
		/*
		Table table = new Table();
        //crew member slider 
		final TextureRegionDrawable knob = new TextureRegionDrawable(new Texture("TaskUI/Reset Modem/panel_wifi-lever.png"));
		final TextureRegionDrawable background = new TextureRegionDrawable(new Texture("TaskUI/Reset Modem/sliderBackground.png"));
		background.setMinHeight(443);
		background.setMinWidth(80);
        //slider style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        Slider cSlider = new Slider(1f,6f,1f,true,sliderStyle);
        sliderStyle.knob = knob;
        sliderStyle.background = background;
     
        stage.addActor(table);
        table.setPosition(stage.getWidth() /2 + 100, stage.getHeight() / 2 + 135);
        
        */

		//Below is logic for lever 
		/*
		wifiLeverImg.addListener(new DragListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				oldY = wifiLeverImg.getY();
				System.out.println("touchDown");
				return true;
			}
			
			@Override
			public void touchDragged(InputEvent e, float x, float y, int pointer) {
				if (turningOff && (wifiLeverImg.getY() >= (stage.getHeight()/2 -190))) {
					System.out.println("Turning Off");
					wifiLeverImg.setPosition(wifiLeverImg.getX(), wifiLeverImg.getY() - y);
					
				}
				
				else if (turningOn && (wifiLeverImg.getY() <= (stage.getHeight()/2 + 135))) {
					System.out.println("Turning On");
					wifiLeverImg.setPosition(wifiLeverImg.getX(), wifiLeverImg.getY() + y);
				}
				
			}
			
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (wifiLeverImg.getY() == (stage.getHeight()/2 -190)) {
					turnedOff = true;
					//Start timer
					startTime = TimeUtils.millis();
					
					
					
					//
					
					turningOn = true;
				}
				else {
					wifiLeverImg.setY(oldY);
				}
				
			}
			
		});
		*/
	}
	
	
	public void render(Batch batch) {
		((CrewMember) Client.getPlayer()).draw(batch);
		Gdx.input.setInputProcessor(stage);
		stage.draw();
		
		/*
		System.out.println(totalTime);
		
		if (turnedOff && totalTime <= 10) {
			totalTime += Gdx.graphics.getDeltaTime();
		
		}
		
		if (turnedOff && totalTime == 10) {
			turningOn = true;
			turnedOff = false;
		}
		
		
		if(completed) {
			System.out.println("SUCCESS!");
			((CrewMember) Client.getPlayer()).setCurrentTask(null);
			((CrewMember) Client.getPlayer()).setTaskCompleted(taskName);
		}
		
		*/
	}

}
