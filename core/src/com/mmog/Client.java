package com.mmog;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;
import com.mmog.players.Player;
import com.mmog.screens.GameScreen;
import com.mmog.screens.JoinScreen;
import com.mmog.screens.LobbyScreen;
import com.mmog.screens.MainScreen;
import com.mmog.screens.ScreenEnum;
import com.mmog.screens.ScreenManager;

import java.io.*;
public class Client
{
	static int port = 7077;
	static DatagramSocket socket;
	static InetAddress address;
	private static Player player;
	private static ArrayList<Player> players;

	public Client() throws IOException {
		players = new ArrayList<Player>();
		initSockets();
	}

	public static void initSockets() {
		port = 7077;
		socket = null;
		// Create a Datagram Socket...
		try
		{ 
			address = InetAddress.getByName("127.0.0.1");

			socket = new DatagramSocket(8000);
		}
		catch (IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}

		UDPEchoReader reader = new UDPEchoReader(socket);
		reader.setDaemon(true);
		reader.start();
	}

	public static ArrayList<Player> getPlayers(){
		return players;
	}

	public static void replacePlayerByRole() {
		if(!player.role.equals("none")) {
			System.out.println("NOW ENTERING GAME!");
			String name = getPlayer().getPlayerName();
			String roomName = getPlayer().connectedRoomName;
			
			if(player.role.equals("CrewMember")) {
				player = new CrewMember(getPlayer().getPlayerID());
				player.setPlayerName(name);
				player.connectedRoomName = roomName;
				//start the game, roles are assigned.
				
				ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
			}
			else if(Client.getPlayer().role.equals("Imposter")) {
				player = new Imposter(getPlayer().getPlayerID());
				player.setPlayerName(name);
				player.connectedRoomName = roomName;
				//start the game, roles are assigned.
				
				ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
			}

			player.setPlayerName(player.getPlayerName());
		}
	}

	public static class UDPEchoReader extends Thread
	{
		public UDPEchoReader(DatagramSocket socket)
		{
			this.datagramSocket = socket;
			active = true;
		}
		public void run()
		{
			byte[] buffer = new byte[1024];
			DatagramPacket incoming = new DatagramPacket(buffer,
					buffer.length);
			while(active)
			{
				try
				{
					// listen for incoming datagram packet
					datagramSocket.receive(incoming);
					// print out received string
					String received = new String(incoming.getData(),incoming.getOffset(),incoming.getLength());
					parseCommand(received);
				}
				catch(IOException e)
				{
					System.out.println(e);
					active = false;
				}
			}
		}
		public boolean active;
		public DatagramSocket datagramSocket;
	}


	public static void connectClientToServer() throws IOException {

		//command int for connection request.
		String input = "0" + ",";
		input += player.getPlayerName() + ",";
		input += player.connectedRoomName;

		// send datagram packet to the server
		DatagramPacket datagramPacket = new DatagramPacket
				(input.getBytes(), input.getBytes().length, address, port);
		socket.send(datagramPacket);
	}

	public static void sendUpdate(float x, float y, boolean isFlipped, boolean isDead, boolean isIdle) throws Exception
	{
		String toSend = "";
		toSend += 1 + ",";
		toSend += player.getPlayerName() + ",";
		toSend += player.connectedRoomName + ",";
		toSend +=  x +","; 
		toSend +=  y +",";
		toSend +=  isFlipped + ","; 
		toSend +=  isDead + ",";  
		toSend +=  isIdle;

		DatagramPacket datagramPacket = new DatagramPacket(toSend.getBytes(), toSend.getBytes().length, address, 7077);	     
		socket.send(datagramPacket);	      	      
	}

	public static void sendCreateRoomCommand(String roomName, float numCrew, float numImp) throws IOException {
		String toSend = "";
		toSend += 5 + ",";
		toSend += roomName +",";
		toSend += player.getPlayerName() + ",";
		toSend +=  numCrew +",";
		toSend +=  numImp;

		DatagramPacket datagramPacket = new DatagramPacket(toSend.getBytes(), toSend.getBytes().length, address, 7077);	     
		socket.send(datagramPacket);
	}

	public static void sendRefreshCommand() throws IOException {
		String toSend = "";
		toSend += 6 + ",";
		toSend += player.getPlayerName();

		DatagramPacket datagramPacket = new DatagramPacket(toSend.getBytes(), toSend.getBytes().length, address, 7077);	     
		socket.send(datagramPacket);
	}

	public static void createPlayer(String name) {
		player = new Player(-1);
		player.setPlayerName(name);
	}

	//create all the players in the connected players array in the client
	public static void createPlayers() {
		for(int i= 0; i < 8; i ++) {
			players.add(new Player(-1));
		}
	}

	public static Player getPlayer() {
		return player;
	}

	public static void updateConnectedClient(int playerID, float x, float y, boolean isFlipped, boolean isDead, boolean isIdle) {
		for(Player p: players) {
			if(p.getPlayerID() == playerID) {
				p.setAll(x, y, isFlipped, isDead, isIdle);
				return;
			}
		}
	}

	public static boolean removeClient() {
		String toSend = "";
		toSend += "4" + ",";
		toSend += player.connectedRoomName + ",";
		toSend += getPlayer().getPlayerName();

		System.out.println(toSend);
		
		ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		
		DatagramPacket datagramPacket = new DatagramPacket(toSend.getBytes(), toSend.getBytes().length, address, 7077);	     
		try {
			socket.send(datagramPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	/*
	 * This parseCommand method is called when the Client receives a datagram packet from the server (byte array)
	 */
	public static void parseCommand(String received) throws IOException
	{
		System.out.println(received);

		String[] dataArray = parseData(received);

		int size = dataArray.length;
		Integer command = Integer.parseInt(dataArray[size-1]);

		if (command == 0)//connect command
		{
			Stack<Integer> playerIDs = new Stack<Integer>();
			Stack<String> playerNames = new Stack<String>();

			for(int i = 0; i < size-1;i+=2) {
				int playerID = Integer.parseInt(dataArray[i]);
				String playerName = dataArray[i+1];

				playerIDs.push(playerID);
				playerNames.push(playerName);

				System.out.println("Connected with @ClientID: " + playerID + " @Name:" + playerName);

			}

			for(Player p: players) 
			{
				if (playerIDs.isEmpty())
				{
					break;
				}

				if(p.getPlayerID() == -1) {
					p.setPlayerID(playerIDs.pop());
					p.setPlayerName(playerNames.pop());
				}
			}
		}
		else if (command == 1)//update command
		{
			//System.out.println(receivedData);
			float x = Float.valueOf(dataArray[0]);
			float y = Float.valueOf(dataArray[1]);
			boolean isFlipped = Boolean.parseBoolean(dataArray[2]);
			boolean isDead = Boolean.parseBoolean(dataArray[3]);
			boolean isIdle = Boolean.parseBoolean(dataArray[4]);
			int playerID = Integer.parseInt(dataArray[5].trim());

			updateConnectedClient(playerID, x, y, isFlipped, isDead, isIdle);
		}
		else if(command == 3) {
			String role = dataArray[0];
			addRoleToPlayer(role);
		}
		else if(command == 4) {
			String name = dataArray[0];
			int playerID = -1;

			for(Player p: players) {
				if(p.getPlayerName().equals(name)) {
					playerID = p.getPlayerID();
				}
			}
			removePlayerWithID(playerID);
		}
		else if(command == 5) {
			//server has returned the created room's host name and the room name
			String roomName = dataArray[0];
			String hostName = dataArray[1];
			
			System.out.println("Server added @Room:" + roomName + " @host:" + hostName);
		}
		else if(command == 6) {
			if(dataArray.length == 1) {
				System.out.println("Reply from Server: No Rooms Available");
			}
			else {
				for(int i = 0; i < size - 1; i ++) {
					JoinScreen.addRoom(dataArray[i]);
				}
			}
		}
	}

	public static Player getPlayerWithID(int playerID) {
		Player player = null;
		for(Player p: players) {
			if(p.getPlayerID() == playerID) {
				player = p;
			}
		}
		return player;
	}

	public static void addPlayerWithID(int playerID) {
		for(Player p: players) {
			if(p.getPlayerID() == -1) {
				p.setPlayerID(playerID);
			}
		}
	}

	public static void addRoleToPlayer(String role) {
		player.role = role;
	}

	public static void removePlayerWithID(int playerID) {
		getPlayerWithID(playerID).setPlayerID(-1);
	}

	public static String[] parseData(String receivedData) throws IOException {
		String dataArray[] = receivedData.split(",");
		return dataArray;
	}
} 