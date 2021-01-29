package com.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class Room {
	
	private String hostName, roomName;
	private InetAddress hostAddress;
	private int hostID;
	
	//room info
	public HashMap<Integer,InetAddress> connectedPlayers;
	public HashMap<Integer, String> connectedPlayersNames;
	private static Random r;
	public ArrayList<String> rolelist;
	public  boolean startGame = false;
	public float numCrew, numImp;
	
	public Room(String hostName, String roomName, InetAddress hostAddres, float numCrew, float numImp) {
		this.setHostName(hostName);
		this.setRoomName(roomName);
		this.setHostAddress(hostAddress);
		this.numCrew = numCrew;
		this.numImp = numImp;
		
		connectedPlayers = new HashMap<Integer,InetAddress>();
		connectedPlayersNames = new HashMap<Integer,String>();
		rolelist = new ArrayList<>();
		
		setR(new Random());
	}
	
	public String getAllPlayersNames() {
		String names = "";
		for(Entry<Integer, String> e: connectedPlayersNames.entrySet()) {
			names += e.getValue() + ",";
		}
		
		return names;
	}
	
	public boolean isRoomEmpty() {
		if(connectedPlayers.size() == 0) {
			return true;
		}
		return false;
	}
	
	//checks if the lobby is full so the game can start
	public boolean isRoomFull() {
		if(connectedPlayers.size() == (numCrew + numImp)) {
			return true;
		}
		return false;
	}
	
	public void addPlayer(String playerName, InetAddress hostAddress) {		
		if(!connectedPlayers.containsValue(hostAddress)) {
			connectedPlayers.put(connectedPlayers.size(), hostAddress);
		}
		
		if(!connectedPlayersNames.containsValue(playerName)) {
			connectedPlayersNames.put(connectedPlayersNames.size(), playerName);
		}
	}
	
	public boolean removePlayer(String playerName) {
		String name = "";
		for(Entry<Integer, String> e: connectedPlayersNames.entrySet()) {
			name = e.getValue();
			
			if(name.equals(playerName)) {
				connectedPlayersNames.remove(e.getKey(), playerName);
				connectedPlayers.remove(e.getKey());
				return true;
			}
		}
		return false;
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
