package com.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class Room {

	private String hostName, roomName;
	private InetAddress hostAddress;
	private int hostID;

	//room info
	public ArrayList<ServerPlayer> allPlayers;

	//public HashMap<Integer,InetAddress> connectedPlayers;
	//public HashMap<Integer, String> connectedPlayersNames;
	private static Random r;
	public ArrayList<String> rolelist;
	public boolean startGame = false;
	public int numCrew, numImp;
	ArrayList<Boolean> reactorTaskCompleted;
	public HashMap<String, Integer> votes;
	private ArrayList<String> completedCrewMembers;
	public boolean sentCrewWinCommand;
	public boolean sentImposterWinCommand, isSabotagedIncomplete, sentWinCommand;

	public Room(String hostName, String roomName, InetAddress hostAddres, int numCrew, int numImp) {
		this.setHostName(hostName);
		this.setRoomName(roomName);
		this.setHostAddress(hostAddress);
		this.numCrew = numCrew;
		this.numImp = numImp;
		rolelist = new ArrayList<>();
		votes = new HashMap<String, Integer>();
		completedCrewMembers = new ArrayList<String>();
		sentCrewWinCommand = false;
		sentImposterWinCommand = false;
		isSabotagedIncomplete = false;
		sentWinCommand = false;
		//populate rolelist

		for(int i = 0; i < numCrew; i++) {
			rolelist.add("CrewMember");
		}

		for(int i = 0; i < numImp; i++) {
			rolelist.add("Imposter");
		}
		
		Collections.shuffle(rolelist);
		
		//reactor task is incomplete by default
		reactorTaskCompleted = new ArrayList<Boolean>();

		allPlayers = new ArrayList<ServerPlayer>();

		setR(new Random());
	}

	//checks if the lobby is full so the game can start
	public boolean isRoomFull() {
		if(allPlayers.size() == (numCrew + numImp)) {
			return true;
		}
		return false;
	}
	
	public String getPlayerNameAndNumVotes() {
		String toReturn = "";
		
		for(Entry<String,Integer> e: votes.entrySet()) {
			String playerName = e.getKey();
			Integer numOfVotes = e.getValue();
			
			toReturn += playerName + "," + numOfVotes + ",";
		}
		
		return toReturn;
	}
	
	public void addVote(String playerVoted) {
		if(votes.containsKey(playerVoted)){
			votes.replace(playerVoted, votes.get(playerVoted) + 1);
		}
		else
			votes.put(playerVoted, 1);
	}
	
	public boolean hostLeft() {
		//loop through the list of players and check if the host is still in the list
		//if the host is not in the list return true
		for(ServerPlayer p: allPlayers) {
			if(p.getAddress().equals(hostAddress)) {
				return false;
			}
		}

		return true;
	}

	public void transferHost(ServerPlayer p) {
		//loop through all players
		//check if the next player exist
		for(int i = 1; i < allPlayers.size(); i++) {
			//if the next player exists, transfer host
			if(allPlayers.get(p.getPlayerID() + i) != null) {
				this.hostID = p.getPlayerID() + i;
				this.hostAddress = allPlayers.get(p.getPlayerID() + i).getAddress();
				this.hostName = allPlayers.get(p.getPlayerID() + i).getPlayerName();
			}
		}
	}

	public InetAddress getAddressOfPlayer(String playerName) {
		InetAddress address = null;
		for(ServerPlayer p: allPlayers) {
			if(p.getPlayerName().equals(playerName)) {
				address =  p.getAddress();
			}
		}
		return address;
	}

	public String assignRole() {
		String role = "";
		int upperBound = rolelist.size() - 1;
		
		if(rolelist.size() == 1) {
			role = rolelist.get(0);
			rolelist.remove(0);
		}else {
			int playerTypeSelector = r.nextInt(upperBound);

			role = rolelist.get(playerTypeSelector);
			rolelist.remove(playerTypeSelector);
		}

		return role;
	}

	public boolean isRoomEmpty() {
		if(allPlayers.size() == 0) {
			return true;
		}
		return false;
	}

	public ArrayList<ServerPlayer> getCrewMembers(){
		ArrayList<ServerPlayer> crew = new ArrayList<ServerPlayer>();

		for(ServerPlayer p: allPlayers) {
			if(p.getRole().equals("CrewMember")) {
				crew.add(p);
				System.out.println(p.getPlayerName() + "IS A CREW MEMBER-------------------------------------------------------------------------");
			}
		}
		return crew;
	}
	
	public ArrayList<ServerPlayer> getImposters(){
		ArrayList<ServerPlayer> imposters = new ArrayList<ServerPlayer>();

		for(ServerPlayer p: allPlayers) {
			if(p.getRole().equals("Imposter")) {
				imposters.add(p);
			}
		}
		return imposters;
	}

	public void addPlayer(String playerName, InetAddress hostAddress) {
		boolean isIn = false;

		if(allPlayers.isEmpty()) {
			ServerPlayer player = new ServerPlayer(playerName, hostAddress);
			allPlayers.add(player);
			player.setPlayerID(1);
			return;
		}else {
			//loop through all players and check if the player exists in the list
			//if the player in list, isIn is true
			for(int i = 0; i < allPlayers.size();i++) {
				ServerPlayer p = allPlayers.get(i);	

				if(p.getPlayerName().equals(playerName)) {
					isIn = true;
				}
			}
		}

		if(!isIn) {
			ServerPlayer player = new ServerPlayer(playerName, hostAddress);
			allPlayers.add(player);
			player.setPlayerID(allPlayers.size());
			return;
		}
	}

	public String printRoleList() {
		String roles = "Role List: ";
		for(String role: rolelist) {
			roles += role + ",";
		}
		return roles;
	}

	public ServerPlayer removePlayer(String name) {
		ArrayList<ServerPlayer> playersToRemove = new ArrayList<ServerPlayer>();

		for(ServerPlayer player: allPlayers) {
			if(player.getPlayerName().equals(name)) {
				playersToRemove.add(player);
			}
		}
		ServerPlayer playerRemoved = playersToRemove.get(0);
		allPlayers.removeAll(playersToRemove);

		return playerRemoved;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public InetAddress getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(InetAddress hostAddress) {
		this.hostAddress = hostAddress;
	}

	public int getHostID() {
		return hostID;
	}

	public void setHostID(int hostID) {
		this.hostID = hostID;
	}

	public Random getR() {
		return r;
	}

	public static void setR(Random r) {
		Room.r = r;
	}

	public void addCompletedCrew(String crewName) {
		completedCrewMembers.add(crewName);
	}

	public boolean isAllCrewMembersTasksCompleted() {
		//check the size of the completed crew members and compare with all the crew members in the game
		if(completedCrewMembers.size() == getCrewMembers().size()) {
			return true;
		}
		return false;
	}
	
	public int getNumOfAliveCrew() {
		int count = 0;
		for(ServerPlayer p: getCrewMembers()) {
			if(!p.isDead()) {
				count ++;
			}
		}
		return count;
	}
	
	public int getNumOfAliveImposters() {
		int count = 0;
		System.out.println("BEFORE GETTING IMPOSTERS");
		
		for(ServerPlayer p: getImposters()) {
			System.out.println("@playername:" + p.getPlayerName() + " : " + p.isDead());
			if(!p.isDead()) {
				count ++;
			}
		}
		return count;
	}
	
	public boolean isImposterRatio1to1() {
		//check if the size of all the alive imposters is equal to the alive crew members
		return getNumOfAliveCrew() == getNumOfAliveImposters();
	}

	public void updatePlayer(String name, boolean isDead) {
		for(ServerPlayer p: allPlayers){
			if(p.getPlayerName().equals(name)) {
				p.setDead(isDead);
			}
		}
	}

	public boolean isAllImposterDead() {
		return getNumOfAliveImposters() == 0;
	}
}