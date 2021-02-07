package com.server;

import java.net.InetAddress;
import java.util.ArrayList;
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
	public  boolean startGame = false;
	public int numCrew, numImp;
	ArrayList<Boolean> reactorTaskCompleted;

	public Room(String hostName, String roomName, InetAddress hostAddres, int numCrew, int numImp) {
		this.setHostName(hostName);
		this.setRoomName(roomName);
		this.setHostAddress(hostAddress);
		this.numCrew = numCrew;
		this.numImp = numImp;
		rolelist = new ArrayList<>();
		//populate rolelist

		for(int i = 0; i < numCrew; i++) {
			rolelist.add("CrewMember");
		}

		for(int i = 0; i < numImp; i++) {
			rolelist.add("Imposter");
		}
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
		if(rolelist.size() > 1) {
			int upperBound = rolelist.size();
			int playerTypeSelector = r.nextInt(upperBound - 1) + 1;
			
			role = rolelist.get(playerTypeSelector - 1);
			rolelist.remove(playerTypeSelector - 1);
		}
		else {
			role = rolelist.get(0);
			rolelist.remove(0);
		}
	
		return role;
	}
	
	public boolean isRoomEmpty() {
		if(allPlayers.size() == 0) {
			return true;
		}
		return false;
	}

	public void addPlayer(String playerName, InetAddress hostAddress) {
		ServerPlayer player = new ServerPlayer(playerName, hostAddress);
		allPlayers.add(player);
		player.setPlayerID(allPlayers.size());
	}

	public String printRoleList() {
		String roles = "Role List: ";
		for(String role: rolelist) {
			roles += role + ",";
		}
		return roles;
	}

	public void removePlayer(String name) {
		ArrayList<ServerPlayer> playersToRemove = new ArrayList<ServerPlayer>();

		for(ServerPlayer player: allPlayers) {
			if(player.getPlayerName().equals(name)) {
				playersToRemove.add(player);
			}
		}
		allPlayers.removeAll(playersToRemove);
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
}