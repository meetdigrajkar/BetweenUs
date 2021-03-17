package com.server;

import java.net.InetAddress;

public class ServerPlayer {

	private int playerID;
	private InetAddress address;
	private String role;
	private String playerName;
	private boolean isHost;
	private boolean isDead;
	
	public ServerPlayer(String playerName, InetAddress address) {
		this.setPlayerName(playerName);
		this.setAddress(address);
		role = "";
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return this.role;
	}
	
	public void setPlayerID(int pid) {
		this.playerID = pid;
	}
	
	public int getPlayerID() {
		return playerID;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
