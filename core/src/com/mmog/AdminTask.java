package com.mmog;

public class AdminTask extends Task{

	final static String taskName = "Admin Task";
	
	public AdminTask() {
		super(taskName);
	}
	
	public static boolean validateCrewMemberID(String playerName) {
		if(MainScreen.player.getPlayerName().equals(playerName)) {
			return true;
		}
		return false;
	}
	
}
