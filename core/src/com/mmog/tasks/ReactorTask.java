package com.mmog.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;
import com.mmog.screens.GameScreen;
import com.mmog.screens.MainScreen;
import com.mmog.screens.ScreenEnum;
import com.mmog.screens.ScreenManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ReactorTask extends Task {
	final static String taskName = "Reactor Task";
	Table table, a;
	TextField crewMemberID;
	Stage stage;
	//Sprite nbutton, hbutton, pbutton;
	public static boolean completed = false;
	TextButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
	TextButton.TextButtonStyle textButtonStyleN;
	Integer[] array;
	private static Integer current;

	public ReactorTask() {
		super(taskName);

		stage = new Stage();
		setGame();

	}

	public void setGame(){
		table = new Table();
		table.setFillParent(true);
		float MAX_WIDTH = Gdx.graphics.getWidth();
		float MAX_HEIGTH = Gdx.graphics.getHeight();
		table.setSize(MAX_WIDTH, MAX_HEIGTH);
		table.center();
		
		GameScreen.timerNum = GameScreen.TIMER_START_VALUE;
		
		textButtonStyleN = new TextButton.TextButtonStyle();
		textButtonStyleN.up = new TextureRegionDrawable(new Texture("GreyButton.png"));
		textButtonStyleN.down = new TextureRegionDrawable(new Texture("GreenButton.png"));
		textButtonStyleN.over = new TextureRegionDrawable(new Texture("LightGreyButton.png"));
		textButtonStyleN.disabled = new TextureRegionDrawable(new Texture("GreenButton.png"));
		BitmapFont font = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));
		font.getData().setScale(0.4f);
		textButtonStyleN.font = font;
		textButtonStyleN.fontColor = Color.BLACK;
		textButtonStyleN.downFontColor = Color.DARK_GRAY;

		array = new Integer[10];
		for (Integer i = 0; i<array.length; i++){
			array[i] = i+1;
		}
		randomizeArray(array);
		System.out.println("Hello Tareq");
		for (int i = 0; i < array.length; i++)
		{
			System.out.print(array[i] + " \n");
		}
		current = 0;
		b1 = new TextButton(array[0].toString(), textButtonStyleN);
		b2 = new TextButton(array[1].toString(), textButtonStyleN);
		b3 = new TextButton(array[2].toString(), textButtonStyleN);
		b4 = new TextButton(array[3].toString(), textButtonStyleN);
		b5 = new TextButton(array[4].toString(), textButtonStyleN);
		b6 = new TextButton(array[5].toString(), textButtonStyleN);
		b7 = new TextButton(array[6].toString(), textButtonStyleN);
		b8 = new TextButton(array[7].toString(), textButtonStyleN);
		b9 = new TextButton(array[8].toString(), textButtonStyleN);
		b10 = new TextButton(array[9].toString(), textButtonStyleN);
		table.row().colspan(5);
		table.add(b1,b2,b3,b4,b5);
		table.row().colspan(5);
		table.add(b6,b7,b8,b9,b10);
		stage.addActor(table);

		b1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 1 Clicked");
				String str = b1.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b1.setTouchable(Touchable.disabled);
					b1.setDisabled(true);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});
		b2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 2 Clicked");
				String str = b2.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b2.setDisabled(true);
					b2.setTouchable(Touchable.disabled);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});

		b3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 3 Clicked");
				String str = b3.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b3.setDisabled(true);
					b3.setTouchable(Touchable.disabled);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});

		b4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 4 Clicked");
				String str = b4.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b4.setDisabled(true);
					b4.setTouchable(Touchable.disabled);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});
		b5.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 5 Clicked");
				String str = b5.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b5.setTouchable(Touchable.disabled);
					b5.setDisabled(true);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});

		b6.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 6 Clicked");
				String str = b6.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b6.setTouchable(Touchable.disabled);
					b6.setDisabled(true);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});

		b7.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 7 Clicked");
				String str = b7.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b7.setTouchable(Touchable.disabled);
					b7.setDisabled(true);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});

		b8.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 8 Clicked");
				String str = b8.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b8.setTouchable(Touchable.disabled);
					b8.setDisabled(true);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});

		b9.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 9 Clicked");
				String str = b9.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b9.setTouchable(Touchable.disabled);
					b9.setDisabled(true);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});

		b10.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button 10 Clicked");
				String str = b10.toString();
				String num = str.substring(str.lastIndexOf(" ")+1);
				Integer a = Integer.parseInt(num);
				if (buttonPressed(a)) {
					//Button becomes green and disabled
					System.out.println("Good Button");
					b10.setTouchable(Touchable.disabled);
					b10.setDisabled(true);
					if(lastNumber()){
						completed = true;
					}
				}
				else {
					System.out.println("Bad Button");
					enableAll();
					current = 0;
				}
			}
		});
	}

	public static void randomizeArray(Integer[] a) {
		int n = a.length;
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < n; i++){
			int change = i + random.nextInt(n - i);
			Integer helper = a[i];
			a[i] = a[change];
			a[change] = helper;
		}
	}

	public static Boolean lastNumber(){
		if(current == 10){
			System.out.println("SUCCESS!");

			try {
				Client.sendReactorTaskCompleted();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public static Boolean buttonPressed(Integer num){
		if(num == current + 1){
			current += 1;
			return true;
		}
		return false;
	}

	public static void setCompletedTask(Boolean isCompleted) {
		if(isCompleted) {
			GameScreen.reactorTaskStarted = false;
			GameScreen.timer.setVisible(false);
			
			if(Client.getPlayer() instanceof CrewMember) {
				if(((CrewMember) Client.getPlayer()).getCurrentTask() != null) {
					if(((CrewMember) Client.getPlayer()).getCurrentTask().getTaskName().equals(taskName)) {
						((CrewMember) Client.getPlayer()).setCurrentTask(null);
					}
				}
				((CrewMember) Client.getPlayer()).removeTask(taskName);
			}
			else{
				if(((Imposter) Client.getPlayer()).getCurrentTask() != null) {
					if(((Imposter) Client.getPlayer()).getCurrentTask().getTaskName().equals(taskName)) {
						((Imposter) Client.getPlayer()).setCurrentTask(null);
					}
				}
				((Imposter) Client.getPlayer()).removeTask(taskName);
			}
			
			GameScreen.reactorTaskStarted = false;
		}
	}

	public void render(Batch batch){
		(Client.getPlayer()).draw(batch);
		Gdx.input.setInputProcessor(stage);

		stage.draw();
	}

	public void enableAll(){
		b1.setDisabled(false);
		b1.setTouchable(Touchable.enabled);
		b2.setDisabled(false);
		b2.setTouchable(Touchable.enabled);
		b3.setDisabled(false);
		b3.setTouchable(Touchable.enabled);
		b4.setDisabled(false);
		b4.setTouchable(Touchable.enabled);
		b5.setDisabled(false);
		b5.setTouchable(Touchable.enabled);
		b6.setDisabled(false);
		b6.setTouchable(Touchable.enabled);
		b7.setDisabled(false);
		b7.setTouchable(Touchable.enabled);
		b8.setDisabled(false);
		b8.setTouchable(Touchable.enabled);
		b9.setDisabled(false);
		b9.setTouchable(Touchable.enabled);
		b10.setDisabled(false);
		b10.setTouchable(Touchable.enabled);
	}


}
