package com.mmog.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mmog.players.CrewMember;
import com.mmog.screens.MainScreen;

public class AdminTask extends Task  {

	final static String taskName = "Admin Task";
	Table table;
	TextField crewMemberID;
	Stage stage;

	Sprite adminCard,adminTextBar,adminbg,adminWalletFront;
	final Image img;

	public AdminTask() {
		super(taskName);
		
		stage = new Stage();
		
		adminbg = new Sprite(new Texture("TaskUI/Swipe Card/adminTask.png"));
		adminCard = new Sprite(new Texture("TaskUI/Swipe Card/admin_Card.png"));
		adminTextBar = new Sprite(new Texture("TaskUI/Swipe Card/admin_textBar.png"));
		adminWalletFront = new Sprite(new Texture("TaskUI/Swipe Card/admin_walletFront.png"));

		//resizing because of camera zoom
		adminbg.setSize(250, 250);
		adminCard.setSize(100, 55);
		adminWalletFront.setSize(125, 52);
		
		LabelStyle ls = new LabelStyle(new BitmapFont(),Color.FIREBRICK);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		
		img = new Image(adminCard);
		stage.addActor(img);
		
		DragAndDrop dragAndDrop = new DragAndDrop();
		
		dragAndDrop.addSource(new Source(img) {

			@Override
			public Payload dragStart(InputEvent event, float x, float y, int pointer) {
				Payload payload = new Payload();
				payload.setObject("Some payload!");

				payload.setDragActor(getActor());

				return payload;
			}

		});
		
		dragAndDrop.addTarget(new Target(img) {

			@Override
			public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
				 getActor().setPosition(x, y);
		         System.out.println("touchdragged" + x + ", " + y);
				return false;
			}

			@Override
			public void drop(Source source, Payload payload, float x, float y, int pointer) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

	public void render(Batch batch) {
		((CrewMember) MainScreen.player).draw(batch);
		Gdx.input.setInputProcessor(stage);
		
		adminbg.setPosition(MainScreen.player.getX() - 40,MainScreen.player.getY() - 50);
		//img.setOrigin(MainScreen.player.getX() - 25,MainScreen.player.getY() - 35);
		adminWalletFront.setPosition(MainScreen.player.getX() - 30,MainScreen.player.getY() - 50);
		
		adminbg.draw(batch);
		adminWalletFront.draw(batch);
		
		stage.draw();
	}
}
