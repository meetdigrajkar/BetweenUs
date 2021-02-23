package com.mmog.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;
import com.mmog.screens.MainScreen;

public class AdminTask extends Task  {

	final static String taskName = "Admin Task";
	Table table;
	TextField crewMemberID;
	Stage stage;

	Sprite adminCard,adminTextBar,adminbg,adminWalletFront;
	final Image cardImg, adminbgImg, adminWalletFrontImg;
	boolean setInPlace = false;
	float cardInPlaceXPos;
	boolean completed = false;
	private long startTime = 0, elapsedTime = 0;
	
	public AdminTask() {
		super(taskName);

		stage = new Stage();

		adminbg = new Sprite(new Texture("TaskUI/Swipe Card/adminTask.png"));
		adminCard = new Sprite(new Texture("TaskUI/Swipe Card/admin_Card.png"));
		adminWalletFront = new Sprite(new Texture("TaskUI/Swipe Card/admin_walletFront.png"));

		//resizing because of camera zoom
		adminbg.setSize(250, 250);
		adminCard.setSize(100, 55);
		adminWalletFront.setSize(125, 52);

		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.FIREBRICK);
		final Label textLabel = new Label("Swipe your ID card.", ls);
		
		adminbgImg = new Image(adminbg);
		cardImg = new Image(adminCard);
		adminWalletFrontImg = new Image(adminWalletFront);
	
		stage.addActor(adminbgImg);
		stage.addActor(cardImg);
		stage.addActor(adminWalletFrontImg);
		stage.addActor(textLabel);
		
		adminbgImg.setPosition(stage.getWidth()/2,stage.getHeight()/2);
		cardImg.setPosition((stage.getWidth()/2)+25,stage.getHeight()/2);
		adminWalletFrontImg.setPosition((stage.getWidth()/2) + 20,stage.getHeight()/2);
		textLabel.setPosition((stage.getWidth()/2) + 50,(stage.getHeight()/2) + 453);

		cardImg.addListener(new DragListener() {
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				if(!setInPlace) {
					cardInPlaceXPos = cardImg.getX();
					cardImg.setPosition(cardImg.getX(), cardImg.getY() + 190);
					setInPlace = true;
				}
				return true;
			}
			
			@Override
			public void touchDragged(InputEvent e, float x, float y, int pointer) {
				if(setInPlace) {
					cardImg.setPosition(cardImg.getX() + x, cardImg.getY());
		
					//reset the position of the card if it reaches the end of the scanner or if the card is not being dragged
					if((cardImg.getX() > (adminbgImg.getX() + 500)) || (cardImg.getX() < adminbgImg.getX()))  {
						cardImg.setX(cardInPlaceXPos);
					}
					
					//check the amount of time it took to drag
					if((cardImg.getX() >= adminbgImg.getX() + 400)) {
						elapsedTime = TimeUtils.timeSinceMillis((long) startTime);
					}
					
				}
			}
			
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				cardImg.setX(cardInPlaceXPos);
				startTime = TimeUtils.millis();
				System.out.println(elapsedTime);
				
				if(elapsedTime < 3000 && elapsedTime > 2000) {
					completed = true;
				}
				
				else if(elapsedTime < 2000 && elapsedTime > 300) {
					textLabel.setText("TOO FAST!");
				}
				
				else if(elapsedTime > 3000) {
					textLabel.setText("TOO SLOW!");
				}
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
