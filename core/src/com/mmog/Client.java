package com.mmog;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mmog.players.CrewMember;
import com.mmog.players.Imposter;
import com.mmog.players.Player;
import com.mmog.screens.GameScreen;
import com.mmog.screens.LobbyScreen;
import com.mmog.screens.MainScreen;

import java.io.*;
public class Client
{
	int port = 7077;
	static DatagramSocket socket;
	static InetAddress address;
	private static String name = "";

	//hash map of connected players
	private static HashMap<Integer,Player> connectedPlayers;
	private static HashMap<Integer,String> connectedPlayersNames;
	private static HashMap<Integer, String> connectedPlayersRoles;

	public Client() throws IOException {
		connectedPlayers = new HashMap<Integer,Player>();
		connectedPlayersNames = new HashMap<Integer,String>();
		connectedPlayersRoles = new HashMap<Integer,String>();
	}

	public static HashMap<Integer,Player> getConnectedPlayers(){
		return connectedPlayers;
	}

	public static HashMap<Integer,String> getConnectedPlayersRoles(){
		return connectedPlayersRoles;
	}
	
	public static HashMap<Integer,String> getConnectedPlayersNames(){
		return connectedPlayersNames;
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
					parseCommand(buffer);
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

	public static void addPlayer(int playerID) {
		connectedPlayers.put(playerID, null);
	}

	public static boolean connectClientToServer(String playerName) {
		int port = 7077;
		socket = null;
		BufferedReader keyboardReader = null;
		// Create a Datagram Socket...
		try
		{ 
			address = InetAddress.getByName("127.0.0.1");

			socket = new DatagramSocket(8000);
			keyboardReader = new BufferedReader(new
					InputStreamReader(System.in));
		}
		catch (IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}

		UDPEchoReader reader = new UDPEchoReader(socket);
		reader.setDaemon(true);
		reader.start();

		try
		{	
			name = playerName;
			//command int for connection request.
			String input = "0" + ",";
			input += name;

			System.out.println(name);

			// send datagram packet to the server
			DatagramPacket datagramPacket = new DatagramPacket
					(input.getBytes(), input.length(), address, port);
			socket.send(datagramPacket);

		}
		catch(IOException e)
		{
			System.out.println(e);
		}	
		return false;
	}

	public static void sendUpdate(float x, float y, boolean isFlipped, boolean isDead, boolean isIdle, int playerID) throws Exception
	{
		String toSend = "";
		toSend += 1 + ",";
		toSend +=  x +","; 
		toSend +=  y +",";
		toSend +=  isFlipped + ","; 
		toSend +=  isDead + ",";  
		toSend +=  isIdle + ","; 
		toSend += playerID + ",";

		DatagramPacket datagramPacket = new DatagramPacket(toSend.getBytes(), toSend.length(), address, 7077);	     
		socket.send(datagramPacket);	      	      
	}

	public static Player getPlayer() {
		Player p = null;
		for (Entry<Integer, Player> e : getConnectedPlayers().entrySet()) {
			p = e.getValue();

			if(p != null && p.getPlayerName().equals(name)) {
				p = e.getValue();
			}
		}
		return p;
	}

	//tell the other clients you are ready
	public static void sendPlayerIsReady() throws IOException {
		String toSend = "";
		toSend += 2 + ",";
		toSend += getPlayer().getPlayerID() + ",";

		DatagramPacket datagramPacket = new DatagramPacket(toSend.getBytes(), toSend.length(), address, 7077);	     
		socket.send(datagramPacket);
	}

	public static void sendPlayerRoleAssign(){
		String toSend = "";
		toSend += 3 + ",";
		toSend += getPlayer().getPlayerID() + ",";

		DatagramPacket datagramPacket = new DatagramPacket(toSend.getBytes(), toSend.length(), address, 7077);	     
		try {
			socket.send(datagramPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateConnectedClient(int playerID, float x, float y, boolean isFlipped, boolean isDead, boolean isIdle) {
		connectedPlayers.get(playerID).setAll(x, y, isFlipped, isDead, isIdle);
	}

	public static boolean removeClient() {
		String toSend = "";
		toSend += "4" + ",";
		toSend += getPlayer().getPlayerID() + ",";

		DatagramPacket datagramPacket = new DatagramPacket(toSend.getBytes(), toSend.length(), address, 7077);	     
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
	public static void parseCommand(byte[] data) throws IOException
	{
		String receivedData = new String(data);
		String[] dataArray = parseData(receivedData);

		int size = dataArray.length;
		Integer command = Integer.parseInt(dataArray[size-1].trim());

		if (command == 0)//connect command
		{
			for(int i = 0; i < size-1;i ++) {
				int playerID = Integer.parseInt(dataArray[i]);
				String playerName = dataArray[i+1];

				//for all the other connect command requests coming from the server from other clients that are not connected already
				if(!connectedPlayersNames.containsKey(playerID)) {
					connectedPlayersNames.put(playerID, playerName);
					connectedPlayers.put(playerID, null);
					System.out.println("Connected with @ClientID: " + playerID + " @Name:" + playerName);
				}
				i++;
			}
		}
		else if (command == 1)//update command
		{
			System.out.println(receivedData);
			float x = Float.valueOf(dataArray[0]);
			float y = Float.valueOf(dataArray[1]);
			boolean isFlipped = Boolean.parseBoolean(dataArray[2]);
			boolean isDead = Boolean.parseBoolean(dataArray[3]);
			boolean isIdle = Boolean.parseBoolean(dataArray[4]);
			int playerID = Integer.parseInt(dataArray[5].trim());

			updateConnectedClient(playerID, x, y, isFlipped, isDead, isIdle);
		}
		else if(command == 2) //is ready command
		{
			//System.out.println(receivedData);
			int readyPlayerID = Integer.parseInt(dataArray[0]);
			connectedPlayers.get(readyPlayerID).readyToPlay = true;
			receivedData = "";
		}
		else if(command == 3) {
			String role = dataArray[1];
			int playerID = Integer.parseInt(dataArray[0]);

			connectedPlayers.remove(playerID);

			connectedPlayersRoles.put(playerID, role);
			connectedPlayers.put(playerID, null);
		}
		else if(command == 4) {
			connectedPlayers.remove(Integer.parseInt(dataArray[0]));
			connectedPlayersNames.remove(Integer.parseInt(dataArray[0]));
		}

	}

	public static String[] parseData(String receivedData) throws IOException {
		String dataArray[] = receivedData.split(",");
		return dataArray;
	}

} 