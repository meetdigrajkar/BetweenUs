package com.server;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.mmog.players.Player;

import java.io.*;

public class Server {

	//private static ArrayList<InetAddress> connectedClientAddresses;

	private static HashMap<Integer,InetAddress> connectedPlayers;
	private static HashMap<Integer, String> connectedPlayersNames;
	private static Random r;
	private static ArrayList<String> rolelist;
	private static HashMap<String,Boolean> playersReady;
	private static boolean everyoneReady = false;
	private static boolean gameStarted = false;

	public static void main(String args[]) throws ClassNotFoundException, IOException
	{
		connectedPlayers = new HashMap<Integer,InetAddress>();
		connectedPlayersNames = new HashMap<Integer,String>();
		playersReady = new HashMap<String,Boolean>();
		rolelist = new ArrayList<>();

		r = new Random();
		int port = 7077;
		// create the server...
		DatagramSocket serverDatagramSocket = null;
		try
		{
			serverDatagramSocket = new DatagramSocket(port);
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		try
		{
			byte buffer[] = new byte[1024];
			DatagramPacket datagramPacket = new
					DatagramPacket(buffer, buffer.length);
			while(true)
			{
				// listen for datagram packets
				serverDatagramSocket.receive(datagramPacket);

				String receivedData = new String(buffer);
				String[] dataArray = parseData(receivedData);

				//check if the client is in the connected clients list.
				//if client is not in the list add its host address to the list
				InetAddress hostAddress = InetAddress.getByName(datagramPacket.getAddress().getHostAddress());
				if(!connectedPlayers.containsValue(hostAddress)) {
					connectedPlayers.put(connectedPlayers.size() - 1, hostAddress);
				}

				//get the command that was sent by the client
				int command = Integer.parseInt(dataArray[0].trim());

				if(!gameStarted) {
					for(Entry<String, Boolean> r: playersReady.entrySet()) {
						if(r.getValue()) {
							everyoneReady = true;
						}
					}
				}
				//System.out.println(gameStarted + "command: "+ command);

				if(!gameStarted && everyoneReady) {
					command = 3;
				}

				//parse the command
				parseCommandAndSend(command, dataArray, hostAddress,serverDatagramSocket);
				//print output command
				//System.out.println(output);
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}


	public static void parseCommandAndSend(int command, String[] dataArray, InetAddress hostAddress,DatagramSocket serverDatagramSocket) {
		StringBuilder toAllClients = new StringBuilder();
		StringBuilder toLocalc = new StringBuilder();

		boolean toLocal = false, toAll = false;

		//the server received a connection request from the client
		//send each players ID
		if(command == 0) {
			toLocal = true;
			toAll = true;

			String playerName = dataArray[1].trim();

			if(!playersReady.containsKey(playerName)) {
				playersReady.put(playerName, false);
			}

			//if the player is not in the map, add him to the map
			if(!connectedPlayersNames.containsValue(playerName)) {
				connectedPlayersNames.put(connectedPlayersNames.size() - 1, playerName);
			}

			for(Entry<Integer,InetAddress> e: connectedPlayers.entrySet()) {
				InetAddress address = e.getValue();
				int playerID = e.getKey();
				String name = connectedPlayersNames.get(playerID);

				//sending to the client the packet came from
				if(address.equals(hostAddress)) {
					if(!name.equals(playerName)) {
						toLocalc.append(playerID).append(",").append(name).append(",");
					}		
				}
				//sending to all the other clients
				else if(!address.equals(hostAddress)) {
					toAllClients.append(playerID).append(",").append(name).append(",");
					toAllClients.append(command);
				}
			}
			//append the command at the end.
			toLocalc.append(command);
			toAllClients.append(command);

			createRoleList();
		}
		//the server received an update request from the client
		else if(command == 1) {
			//needs to send this to all the other clients
			toLocal = false;
			toAll = true;

			toAllClients = (new StringBuilder());

			for(int i = 1; i < dataArray.length; i++) {
				toAllClients.append(dataArray[i]).append(",");
			}
			toAllClients.append(command);
		}
		//the server received a Is ready command from the client
		else if(command == 2) {
			//System.out.println(dataArray[1]);
			if(playersReady.containsKey(dataArray[1])) {
				playersReady.replace(dataArray[1], true);
			}
			everyoneReady = true;
		}
		//server sends the role assigned to each player
		else if(command == 3) {
			toLocal = true;
			toAll = true;
			gameStarted = true;
		}//the server received a disconnect request from the client
		else if(command == 4) {
			toLocal = false;
			toAll = true;

			toAllClients = (new StringBuilder());
			System.out.println("CLOSED REQUESTED!");
			String name = dataArray[1].trim();

			for(Entry<Integer,String> entry: connectedPlayersNames.entrySet()) {
				if(entry.getValue().equals(name)) {
					connectedPlayersNames.remove(entry.getKey());
					connectedPlayers.remove(entry.getKey());
					playersReady.remove(name);

					toAllClients.append(name).append(",");
				}
			}

			toAllClients.append(command);
		}

		//send the command
		sendCommand(toLocalc.toString(),toAllClients.toString(),serverDatagramSocket, command, toLocal, toAll, hostAddress);
	}

	public static void sendCommand(String toLocalc, String toAllClients, DatagramSocket serverDatagramSocket, int command, boolean toLocal, boolean toAll, InetAddress hostAddress) {
		DatagramPacket toSend = new DatagramPacket((toLocalc.toString()).getBytes(), toLocalc.length(), hostAddress, 8000);
		StringBuilder toAllR;

		for(Entry<Integer, InetAddress> e : connectedPlayers.entrySet()) {
			InetAddress address = e.getValue();

			//4 situations------------------------------------------------------
			//1
			//command == 0, need to send to both local and all other clients
			//toLocalc to local and toAllClients to all the other clients
			//2
			//command == 1, need to send update command to ONLY all other clients
			//toAllClients to all the other clients
			//3
			//command == 3, need to send role assignment to ONLY the local client
			//toLocalc to the local client
			//4
			//command == 4, need to send close command to ONLY all the other clients
			//toAllClients to all the other clients
			//doing the same shit above for update command == 1

			if(command == 0 && address.equals(hostAddress)) {
				toSend = new DatagramPacket((toLocalc.toString()).getBytes(), toLocalc.length(), hostAddress, 8000);
			}
			else if((command == 0 || command == 1 || command == 4 ) && !address.equals(hostAddress)) {
				toSend = new DatagramPacket((toAllClients.toString()).getBytes(), toAllClients.length(), address, 8000);
			}
			else if(command == 3) {
				String assignedRole = "";

				//give the player a role
				toAllR = new StringBuilder();
				if(!rolelist.isEmpty()) {
					assignedRole = assignRole();
				}

				toAllR.append("Imposter").append(",");
				toAllR.append(command);

				toSend = new DatagramPacket((toAllR.toString()).getBytes(), toAllR.length(), address, 8000);
			}

			try {
				//change the packet to send based on whether to send to local, all (except local), and both
				System.out.println("Server is sending @command: " + command + " to @ClientID: LOCAL" + " @Address: " + hostAddress);
				serverDatagramSocket.send(toSend);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void createRoleList() {
		//populate role list with roles
		for(int i = 0; i <connectedPlayers.size(); i++) {
			if(connectedPlayers.size() >= 8 && i < 2 ) {
				rolelist.add("Imposter");
			}
			else if(connectedPlayers.size() < 8 && i == 0 ) {
				rolelist.add("Imposter");
			}
			else
				rolelist.add("CrewMember");
		}
	}

	public static String assignRole() {
		String role = "";
		//every player has a 50/50 chance of being imposter or crewmember
		int upperBound = 100;
		int playerTypeSelector = r.nextInt(upperBound);

		if(playerTypeSelector < 50 && rolelist.contains("Imposter")) {
			role = "Imposter";
			rolelist.remove("Imposter");
		}
		else if(playerTypeSelector >= 50 && rolelist.contains("CrewMember")) {
			role = "CrewMember";
			rolelist.remove("CrewMember");
		}
		else if (!rolelist.contains("Imposter") && rolelist.contains("CrewMember")) {
			role = "CrewMember";
			rolelist.remove("CrewMember");
		}

		return role;
	}

	public static String[] parseData(String receivedData) throws IOException {
		String dataArray[] = receivedData.split(",");
		return dataArray;
	}
}