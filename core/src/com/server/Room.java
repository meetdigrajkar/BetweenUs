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
	public HashMap<Integer,InetAddress> connectedPlayers;
	public HashMap<Integer, String> connectedPlayersNames;
	private static Random r;
	public ArrayList<String> rolelist;
	public  boolean startGame = false;
	public float numCrew, numImp;
	ArrayList<Boolean> reactorTaskCompleted;
	
	public Room(String hostName, String roomName, InetAddress hostAddres, float numCrew, float numImp) {
		this.setHostName(hostName);
		this.setRoomName(roomName);
		this.setHostAddress(hostAddress);
		this.numCrew = numCrew;
		this.numImp = numImp;
		
		//reactor task is incomplete by default
		reactorTaskCompleted = new ArrayList<Boolean>();
		
		connectedPlayers = new HashMap<Integer,InetAddress>();
		connectedPlayersNames = new HashMap<Integer,String>();
		rolelist = new ArrayList<>();
		
		setR(new Random());
	}
	
	//checks if the lobby is full so the game can start
	public boolean isRoomFull() {
		if(connectedPlayers.size() == (numCrew + numImp)) {
			return true;
		}
		return false;
	}
	
	public boolean isRoomEmpty() {
		if(connectedPlayers.size() == 0) {
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
	
	public void removePlayer(String name) {
		for(Entry<Integer,String> entry: connectedPlayersNames.entrySet()) {
			if(entry.getValue().equals(name)) {
				connectedPlayersNames.remove(entry.getKey());
				connectedPlayers.remove(entry.getKey());
				break;
			}
		}
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