package com.mmog.tasks;

import java.util.ArrayList;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Player;

public class EmergencyMeeting extends Task{

	final static String taskName = "Emergency Meeting";
	Table table;
	ArrayList<Label> playerNames;
	Stage stage;

	Sprite meetingbg,playerbox,chaticon,cancelvote, confirmvote, playervoteicon, deadx, skipvote, skipped, playervoted, playericon;
	final Image meetingbgImage, playericonImage, playerboxImage, chaticonImage, cancelvoteImage, confirmvoteImage, playervoteiconImage, playervotedImage, deadxImage, skippedImage,skipvoteImage;
	boolean completed = false;
	private long startTime = 0, elapsedTime = 0;
	
	public EmergencyMeeting() {
		super(taskName);

		stage = new Stage();
		table = new Table();
	
		playerNames = new ArrayList<Label>();
		
		meetingbg = new Sprite(new Texture("TaskUI/meeting/meetingbg.png"));
		playerbox = new Sprite(new Texture("TaskUI/meeting/playerbox.png"));
		playericon = new Sprite(new Texture("TaskUI/meeting/playericon.png"));
		cancelvote = new Sprite(new Texture("TaskUI/meeting/cancelvote.png"));
		chaticon = new Sprite(new Texture("TaskUI/meeting/chaticon.png"));
		confirmvote = new Sprite(new Texture("TaskUI/meeting/confirmvote.png"));
		playervoteicon = new Sprite(new Texture("TaskUI/meeting/playervoteicon.png"));
		deadx = new Sprite(new Texture("TaskUI/meeting/x.png"));
		skipvote = new Sprite(new Texture("TaskUI/meeting/skip.png"));
		skipped = new Sprite(new Texture("TaskUI/meeting/skipped.png"));
		playervoted = new Sprite(new Texture("TaskUI/meeting/playervoted.png"));
		
		meetingbgImage = new Image(meetingbg);
		playerboxImage = new Image(playerbox);
		chaticonImage = new Image(chaticon);
		cancelvoteImage = new Image(cancelvote);
		confirmvoteImage = new Image(confirmvote);
		playervoteiconImage = new Image(playervoteicon);
		deadxImage = new Image(deadx);
		skipvoteImage = new Image(skipvote);
		skippedImage = new Image(skipped);
		playervotedImage = new Image(playervoted);
		playericonImage = new Image(playericon);
		
		//set position
		meetingbgImage.setPosition(stage.getWidth()/3,stage.getHeight()/3);
		chaticonImage.setPosition((stage.getWidth()/2) + 380, (stage.getHeight()/2) + 300);
		skippedImage.setPosition((stage.getWidth()/3) + 60,(stage.getHeight()/3) + 80);
		skipvoteImage.setPosition((stage.getWidth()/3) + 60,(stage.getHeight()/3) + 150);
		
		stage.addActor(meetingbgImage);
		stage.addActor(chaticonImage);
		
		//table for the playerbox
		table.setFillParent(true);
		float MAX_WIDTH = meetingbgImage.getWidth();
		float MAX_HEIGTH = meetingbgImage.getHeight();
		
		table.setPosition(-1000, 325);
		//table.setSize(MAX_WIDTH, MAX_HEIGTH);
		
		int count = 0;
		for(Player p: Client.getPlayers()) {
			table.add(playerboxImage).padRight(50);
			count++;
			if (count == 2) {
				table.row();
				count = 0;
			}
		}
		
		//stage.addActor(cancelvoteImage);
		//stage.addActor(confirmvoteImage);
		stage.addActor(table);
		stage.addActor(skipvoteImage);
		stage.addActor(skippedImage);
		
		
	}

	public void render(Batch batch) {
		(Client.getPlayer()).draw(batch);
		Gdx.input.setInputProcessor(stage);
		stage.draw();
		
		if(completed) {
			System.out.println("SUCCESS!");
			((CrewMember) Client.getPlayer()).setCurrentTask(null);
			((CrewMember) Client.getPlayer()).setTaskCompleted(taskName);
		}
	}
}
