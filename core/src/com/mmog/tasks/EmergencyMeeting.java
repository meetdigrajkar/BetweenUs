package com.mmog.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Player;
import com.mmog.screens.GameScreen;
import com.mmog.screens.ScreenEnum;
import com.mmog.screens.ScreenManager;

public class EmergencyMeeting extends Task{

	final static String taskName = "Emergency Meeting";
	Table table;
	ArrayList<Label> playerNames;
	Stage stage;

	Sprite meetingbg,playerbox,chaticon,cancelvote, confirmvote, playervoteicon, deadx, skipvote, skipped, playervoted, playericon;
	final Image meetingbgImage, chaticonImage, playervotedImage, deadxImage, skippedImage,skipvoteImage;
	boolean completed = false;
	private Label timer;
	private float timerNum = 60;

	//make fonts here
	BitmapFont font = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));

	private long startTime = 0, elapsedTime = 0;
	private ArrayList<Table> playerboxtables;
	private String votedPlayer = "";
	private boolean voted = false, end = false, drawVotes = false, drawSkippedVotes =  false;
	public static HashMap<String, Integer> votes;
	
	public EmergencyMeeting() {
		super(taskName);

		stage = new Stage();
		table = new Table();
		votes = new HashMap<String,Integer>();
		
		//resizing fonts
		font.getData().setScale(0.6f);

		playerNames = new ArrayList<Label>();
		playerboxtables = new ArrayList<Table>();

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
		chaticonImage = new Image(chaticon);
		deadxImage = new Image(deadx);
		skipvoteImage = new Image(skipvote);
		skippedImage = new Image(skipped);
		playervotedImage = new Image(playervoted);

		LabelStyle style = new LabelStyle(font, Color.BLACK);
		timer = new Label(timerNum + "", style);
		
		//set position
		meetingbgImage.setPosition(stage.getWidth()/3,stage.getHeight()/3);
		chaticonImage.setPosition((stage.getWidth()/2) + 380, (stage.getHeight()/2) + 300);
		skippedImage.setPosition((stage.getWidth()/3) + 60,(stage.getHeight()/3) + 80);
		skipvoteImage.setPosition((stage.getWidth()/3) + 60,(stage.getHeight()/3) + 150);
		timer.setPosition((stage.getWidth()/2) + 300,(stage.getHeight()/3) + 80);

		stage.addActor(meetingbgImage);
		stage.addActor(chaticonImage);

		//table for the playerbox
		table.setFillParent(true);
		table.setPosition(70, 290);
	
		int count = 0, i = 0;

		for(final Player p: GameScreen.getYBasedSortedPlayers()) {
			System.out.println("ADDED: @playerName: " + p.getPlayerName());

			Table playerboxtable = new Table();
			playerboxtable.background(new TextureRegionDrawable(playerbox));
			
			Label playerlabel = new Label (p.getPlayerName(), style);
			Image playericonImage = new Image(playericon);
			final Image cancelvoteImage = new Image(cancelvote);
			final Image confirmvoteImage = new Image(confirmvote);
			
			cancelvoteImage.setVisible(false);
			confirmvoteImage.setVisible(false);
			
			playerboxtable.add(playericonImage).padRight(30);
			playerboxtable.add(playerlabel).padRight(15);
			playerboxtable.add(cancelvoteImage).padRight(5);
			playerboxtable.add(confirmvoteImage);
			playerboxtable.row();

			playerboxtables.add(playerboxtable);
			table.add(playerboxtables.get(i)).padRight(10);
			
			//add listener
			
			playerboxtables.get(i).getChild(0).addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(!voted) {
						System.out.println("CLICKED: @playerName: " + p.getPlayerName());
						
						cancelvoteImage.setVisible(true);
						confirmvoteImage.setVisible(true);
					}	
				}
			});
			
			playerboxtables.get(i).getChild(2).addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					System.out.println("CANCEL CLICK ON: @playerName: " + p.getPlayerName());
					
					cancelvoteImage.setVisible(false);
					confirmvoteImage.setVisible(false);
				}
			});
			
			playerboxtables.get(i).getChild(3).addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					System.out.println("CONFIRM VOTE ON: @playerName: " + p.getPlayerName());
					
					votedPlayer = p.getPlayerName();
					voted = true;
					
					//send vote to the server
					try {
						Client.sendVote(voted, p.getPlayerName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					cancelvoteImage.setVisible(false);
					confirmvoteImage.setVisible(false);
				}
			});
	
			count++;
			i++;
			if (count == 2) {
				table.row();
				count = 0;
			}
		}
		
		stage.addActor(table);
		stage.addActor(skipvoteImage);
		stage.addActor(timer);
		
		skipvoteImage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!voted) {
					System.out.println("SKIPPED VOTE!");
					
					voted = true;
					
					//send vote to the server
					try {
						Client.sendVote(false, "");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		
		stage.addActor(skippedImage);
	}
	
	public static void addVote(String playerName, Integer numOfVotes) {
		votes.put(playerName, numOfVotes);
	}

	public void render(Batch batch) {
		(Client.getPlayer()).draw(batch);
		Gdx.input.setInputProcessor(stage);
		
		if(timerNum > 50) {
			timerNum = (((timerNum * 1000) - (Gdx.graphics.getDeltaTime() * 1000)) /1000);
			timer.setText(((int) timerNum) + "");
		}
		else {
			completed = true;
		}
		
		if(voted) {
			skipvoteImage.setVisible(false);
		}
		
		for(int i = 0; i < playerboxtables.size(); i++) {
			if(voted) {
				 playerboxtables.get(i).getChild(2).setVisible(false);
				 playerboxtables.get(i).getChild(3).setVisible(false);
			}
			if(end && !drawVotes) {
				for(Entry<String, Integer> e: votes.entrySet()) {
					String playerName = e.getKey();
					Integer numOfVotes = e.getValue();
					
					if(((Label) playerboxtables.get(i).getChild(1)).getText().toString().equals(playerName)) {
						for(int k = 0; k < numOfVotes; k ++) {
							Image playervoteiconImage = new Image(playervoteicon);
							
							playerboxtables.get(i).add(playervoteiconImage);
						}
					}
				}
				drawVotes = true;
			}
		}
		
		if(end && !drawSkippedVotes) {
			for(Entry<String, Integer> e: votes.entrySet()) {
				String playerName = e.getKey();
				Integer numOfVotes = e.getValue();
				
				if(playerName.equals("skipped")) {
					int newx = 0;
					for(int o = 0; o < numOfVotes; o++) {
						Image playervoteiconImage = new Image(playervoteicon);
						playervoteiconImage.setPosition((stage.getWidth()/3) + 60 + newx,(stage.getHeight()/3) + 50);
						stage.addActor(playervoteiconImage);
						
						newx += 10;
					}
				}
			}
			drawSkippedVotes = true;
		}
		
		
		if(completed) {
			if(!end) {
				try {
					Client.sendGetVotes();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				end = true;
			}
			
			if(timerNum > 0) {
				timerNum = (((timerNum * 1000) - (Gdx.graphics.getDeltaTime() * 1000)) /1000);
				timer.setText(((int) timerNum) + "");
			}
			else {
				((CrewMember) Client.getPlayer()).setCurrentTask(null);
				((CrewMember) Client.getPlayer()).setTaskCompleted(taskName);
			}
		}
		
		
		stage.act();
		stage.draw();
	}
}
