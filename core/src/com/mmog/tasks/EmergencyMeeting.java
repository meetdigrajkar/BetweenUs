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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mmog.Client;
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;
import com.mmog.players.Player;
import com.mmog.screens.GameScreen;
import com.mmog.screens.ScreenEnum;
import com.mmog.screens.ScreenManager;

public class EmergencyMeeting extends Task{

	final static String taskName = "Emergency Meeting";
	Table table, messageTable, receivedMessageTable, chatTable;
	ArrayList<Label> playerNames;
	Stage stage;

	Sprite meetingbg,playerbox,chaticon,cancelvote, confirmvote, playervoteicon, deadx, skipvote, skipped, playervoted, playericon, chatbg, messagebg, sendbutton, receivedbg;
	final Image meetingbgImage, chaticonImage, playervotedImage, deadxImage, skippedImage,skipvoteImage, chatbgImage, messagebgImage, sendbuttonImage;
	boolean getVotes = false;
	boolean isChatting = false;
	private Label timer;
	private float timerNum = 60;

	//make fonts here
	BitmapFont font = new BitmapFont(Gdx.files.internal("UI/newlabelfont.fnt"));

	private long startTime = 0, elapsedTime = 0;
	private ArrayList<Table> playerboxtables, messagesTable;
	private String votedPlayer = "";
	private boolean voted = false, receivedVotes = false, drawVotes = false, drawSkippedVotes =  false, everyoneVoted = false;
	public HashMap<String, Integer> votes;
	LabelStyle timerstyle;
	String votedOffPlayer = "";
	
	TextField messageField;
	
	private List<String> users_list;
	private ScrollPane chat_scroll;
	private ScrollPane users_scroll;
	private ScrollPane input_scroll;
	private Label chat_label;
	
	private TextButton send_button;
	private TextArea message_field;
	
	private Skin skin;
	
	public boolean meetingCompleted;
	
	public EmergencyMeeting() {
		super(taskName);
		meetingCompleted = false;
		stage = new Stage();
		table = new Table();
		votes = new HashMap<String,Integer>();
		drawVotes = false;
		messageTable = new Table();
		receivedMessageTable = new Table();
		
		messagesTable = new ArrayList<Table>();

		
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
		chatbg = new Sprite(new Texture("TaskUI/meeting/chatbg.png"));
		messagebg = new Sprite(new Texture("TaskUI/meeting/messagebg.png"));
		sendbutton = new Sprite(new Texture("TaskUI/meeting/sendbutton.png"));
		receivedbg = new Sprite(new Texture("TaskUI/meeting/receivedbg.png"));

		meetingbgImage = new Image(meetingbg);
		chaticonImage = new Image(chaticon);
		deadxImage = new Image(deadx);
		skipvoteImage = new Image(skipvote);
		skippedImage = new Image(skipped);
		playervotedImage = new Image(playervoted);
		chatbgImage = new Image(chatbg);
		messagebgImage = new Image(messagebg);
		sendbuttonImage = new Image(sendbutton);
		
		

		LabelStyle style = new LabelStyle(font, Color.BLACK);
		timerstyle = new LabelStyle(font, Color.WHITE);
		
		timer = new Label(timerNum + "", timerstyle);
		
		TextField.TextFieldStyle msgstyle = new TextField.TextFieldStyle();
		msgstyle.font = font;
		
		
		//msgstyle.background = (Drawable) messagebgImage;
		msgstyle.fontColor = new Color(Color.BLACK);
		
		
		messageField = new TextField("adadadad", msgstyle);
		//messageField.setWidth(30);
		messageField.setMaxLength(30);
		messageField.setAlignment(Align.left);

		
		//set position
		meetingbgImage.setPosition(stage.getWidth()/3,stage.getHeight()/3);
		chatbgImage.setPosition(stage.getWidth()/3 + 50,stage.getHeight()/3 + 50);
		chaticonImage.setPosition((stage.getWidth()/2) + 465, (stage.getHeight()/2) + 300);
		skippedImage.setPosition((stage.getWidth()/3) + 60,(stage.getHeight()/3) + 80);
		skipvoteImage.setPosition((stage.getWidth()/3) + 60,(stage.getHeight()/3) + 150);
		timer.setPosition((stage.getWidth()/2) + 465,(stage.getHeight()/3) + 80);
		messagebgImage.setPosition(stage.getWidth()/3 + 63,stage.getHeight()/3 + 55);
		sendbuttonImage.setPosition(stage.getWidth()/3 + 615,stage.getHeight()/3 + 61);
		
		
		messageField.setPosition(stage.getWidth()/3 + 70,stage.getHeight()/3 + 55);
		messageField.setVisible(false);
		messagebgImage.setVisible(false);
		sendbuttonImage.setVisible(false);

		stage.addActor(meetingbgImage);
		stage.addActor(chatbgImage);
		stage.addActor(chaticonImage);
		stage.addActor(messagebgImage);
		stage.addActor(sendbuttonImage);
		stage.addActor(messageField);
		

		//table for the playerbox
		table.setFillParent(true);
		table.setPosition(70, 290);
		
		messageTable.setFillParent(true);
		messageTable.setPosition(70,290);
	
		int count = 0, i = 0;

		for(final Player p: GameScreen.getYBasedSortedPlayers()) {
			//System.out.println("ADDED: @playerName: " + p.getPlayerName());

			Table playerboxtable = new Table();
			playerboxtable.background(new TextureRegionDrawable(playerbox));
			
			Label playerlabel = new Label (p.getPlayerName(), style);
			Image deadxImage = new Image(deadx);
			final Image cancelvoteImage = new Image(cancelvote);
			final Image confirmvoteImage = new Image(confirmvote);
			final Image playericonImage = new Image(playericon);
			
			cancelvoteImage.setVisible(false);
			confirmvoteImage.setVisible(false);
			
			chatbgImage.setVisible(false);
			
			playerboxtable.add(playericonImage).padRight(30);
			playerboxtable.add(playerlabel).padRight(15);
			playerboxtable.add(cancelvoteImage).padRight(5);
			playerboxtable.add(confirmvoteImage);
			
			//if the player is dead add an x to the player box table
			if(p.isDead) {
				playerboxtable.add(deadxImage);
			}
			
			playerboxtable.row();
			playerboxtables.add(playerboxtable);
			table.add(playerboxtables.get(i)).padRight(10);
			
			//add listener
			
			playerboxtables.get(i).getChild(0).addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(!voted) {
						//System.out.println("CLICKED: @playerName: " + p.getPlayerName());
						
						//if the local player is NOT dead and the player being voted for is NOT dead, then allow them to be VOTED
						if(!Client.getPlayer().isDead && !p.isDead) {
							cancelvoteImage.setVisible(true);
							confirmvoteImage.setVisible(true);
						}
					}	
				}
			});
			
			playerboxtables.get(i).getChild(2).addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					//System.out.println("CANCEL CLICK ON: @playerName: " + p.getPlayerName());
					
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
				if(!voted && !Client.getPlayer().isDead) {
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
		
		
		
		
		chaticonImage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isChatting == false) {
					System.out.println("Showing Chatbox");
					isChatting = true;
					
					chatTable.setVisible(true);

					skipvoteImage.setVisible(false);
					skippedImage.setVisible(false);
					
					for(int i = 0; i < playerboxtables.size(); i++) {	
							 playerboxtables.get(i).setVisible(false);
							 playerboxtables.get(i).setVisible(false);
					}
					
					for(int m = 0; m < messagesTable.size(); m++) {	
						messagesTable.get(m).setVisible(false);
						messagesTable.get(m).setVisible(false);
					}
				}
				
				else if (isChatting) {
					System.out.println("Hiding Chatbox");
					isChatting = false;
					chatTable.setVisible(false);

					skipvoteImage.setVisible(true);
					skippedImage.setVisible(true);
					
					for(int i = 0; i < playerboxtables.size(); i++) {	
						 playerboxtables.get(i).setVisible(true);
						 playerboxtables.get(i).setVisible(true);
				}
					}
				}
			});
		
		chatTable = buildChatRoomTable();
		//table.add(chatTable);
		
		chatTable.setVisible(false);
		stage.addActor(chatTable);
		stage.addActor(skippedImage);
				
		chatTable.setPosition(stage.getWidth()/3 - 600,stage.getHeight()/3 - 225);
	}
	
	public void addVote(String playerName, Integer numOfVotes) {
		votes.put(playerName, numOfVotes);
	}
	
	
	public void disableReportButton() {
		if(Client.getPlayer() instanceof CrewMember) {
			((CrewMember) Client.getPlayer()).reportButton.setVisible(false);
		}
		else if(Client.getPlayer() instanceof Imposter) {
			((Imposter) Client.getPlayer()).reportButton.setVisible(false);
		}
	}
	
	private Table buildChatRoomTable(){
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		final TextureRegionDrawable textbox = new TextureRegionDrawable(new Texture("UI/textbox.png"));

		TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
		tfs.font = font;
		tfs.background = textbox;
		tfs.fontColor = new Color(Color.PURPLE);
		
		TextButton.TextButtonStyle textbs2 = new TextButton.TextButtonStyle();
		textbs2.font = font;
		textbs2.fontColor = Color.WHITE;

		
		LabelStyle v2 = new LabelStyle(font, Color.BLACK);
		Table table = new Table();
		table.setFillParent(true);

		chat_label = new Label("", v2);
		chat_label.setWrap(true);
		chat_label.setAlignment(Align.topLeft);
		chat_label.setFontScale(0.25f, 0.25f);
		
		
		chat_scroll = new ScrollPane(chat_label, skin);
		chat_scroll.setFadeScrollBars(false);

		table.add(chat_scroll).width(300f).height(400f).colspan(2);

		users_list = new List<String>(skin);
		
		Array<String> users = new Array<String>();
		users.add("Player Names");
		for(final Player p: GameScreen.getYBasedSortedPlayers()) {
			if (!p.isDead) {
				users.add(p.getPlayerName());
			}
		}
		
		users_list.setItems(users);
	
		users_scroll = new ScrollPane(users_list, skin);
		users_scroll.setFadeScrollBars(false);

		table.add(users_scroll).width(150f).height(400f).colspan(3);

		message_field = new TextArea("", skin);
		message_field.setPrefRows(2);

		input_scroll = new ScrollPane(message_field, skin);
		input_scroll.setFadeScrollBars(false);

		table.row();
		table.add(input_scroll).width(300f).colspan(2);

		send_button = new TextButton("Send", textbs2);
		table.add(send_button).colspan(2);

		table.setVisible(false);
		
		send_button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				System.out.println("Sending Message");
				
				//Use Client method to send message to server
				if (!Client.getPlayer().isDead) {
					if (!message_field.getText().isEmpty()) {
						try {
							Client.sendMessageCommand(message_field.getText());
							message_field.setText("");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}});

		return table;
	}
	
	public void updateMessage(String name, String message) {
		chat_label.setText(chat_label.getText() + " " + name + " : " + message + "\n");	
	}
	
	public void render(Batch batch) {
		(Client.getPlayer()).draw(batch);
		Gdx.input.setInputProcessor(stage);

		//chat_label.setText("Render test");
		chat_label.act(Gdx.graphics.getDeltaTime());
		disableReportButton();
		
		if(timerNum >= 10) {
			timerNum = (((timerNum * 1000) - (Gdx.graphics.getDeltaTime() * 1000)) /1000);
			timer.setText(((int) timerNum) + "");
		}
		else {
			getVotes = true;
		}
		
		if(voted) {
			skipvoteImage.setVisible(false);
		}
		
		if(getVotes && !receivedVotes) {
			try {
				Client.sendGetVotes();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			receivedVotes = true;
		}
		
		/*
		if(votes.size() == GameScreen.getNumOfAlivePlayers()) {
			timerNum = 10;
			getVotes = true;
		}
		*/
		
		if(receivedVotes) {		
			for(int i = 0; i < playerboxtables.size(); i++) {	
				if(voted) {
					 playerboxtables.get(i).getChild(2).setVisible(false);
					 playerboxtables.get(i).getChild(3).setVisible(false);
				}
				
				if(!drawVotes) {
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
					
					if(votes.size() != 0) {
						drawVotes = true;
					}
				}
			}
			
			if(!drawSkippedVotes) {
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
		
			
			if(timerNum > 0) {
				if((((int) timerNum) % 2) == 0) {
					timerstyle.fontColor = Color.RED;
				}
				else
					timerstyle.fontColor = Color.WHITE;
				
				
				timerNum = (((timerNum * 1000) - (Gdx.graphics.getDeltaTime() * 1000)) /1000);
				timer.setText(((int) timerNum) + "");
			
				timer.act(Gdx.graphics.getDeltaTime());
			}
			else {
				int currMaxVotes = 0, skippedVotes = 0;
				for(Entry<String, Integer> es: votes.entrySet()) {
					String playerName = es.getKey();
					Integer numOfVotes = es.getValue();
					
					if(!playerName.equals("skipped")) {
						//find new max vote
						if(currMaxVotes < numOfVotes) {
							currMaxVotes = numOfVotes;
							votedOffPlayer = playerName;
						}
						else if(currMaxVotes == numOfVotes) {
							votedOffPlayer = "";
						}
					}
					
					if(playerName.equals("skipped")) {
						skippedVotes = numOfVotes;
					}
				}
				
				if(skippedVotes >= currMaxVotes) {
					votedOffPlayer = "";
				}
				
				
				for(Player p: GameScreen.getYBasedSortedPlayers()) {
					if(p.getPlayerName().equals(votedOffPlayer)) {
						p.isDead = true;
						p.votedOff = true;
					}
				}
				
				meetingCompleted = true;
				
				if(Client.getPlayer() instanceof CrewMember) {
					((CrewMember) Client.getPlayer()).setCurrentTask(null);
					((CrewMember) Client.getPlayer()).removeTask(taskName);
				}
				else{
					((Imposter) Client.getPlayer()).setCurrentTask(null);
					((Imposter) Client.getPlayer()).removeTask(taskName);
				}
			}
		}
		
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
}
