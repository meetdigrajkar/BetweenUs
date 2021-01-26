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


	public static void main(String args[]) throws ClassNotFoundException, IOException
	{
		connectedPlayers = new HashMap<Integer,InetAddress>();
		connectedPlayersNames = new HashMap<Integer,String>();
		rolelist = new ArrayList<>();
		
		r = new Random();
		int port = 7077;
		// create the server...
		DatagramSocket serverDatagramSocket = null;
		try
		{
			serverDatagramSocket = new DatagramSocket(port);
			System.out.println("Created UDP Echo Server on port " + port);
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
			String input;
			while(true)
			{
				// listen for datagram packets
				serverDatagramSocket.receive(datagramPacket);

				String receivedData = new String(buffer);
				
				String[] dataArray = parseData(receivedData);
				StringBuilder output = new StringBuilder();

				//check if the client is in the connected clients list.
				//if client is not in the list add its host address to the list
				InetAddress hostAddress = InetAddress.getByName(datagramPacket.getAddress().getHostAddress());
				if(!connectedPlayers.containsValue(hostAddress)) {
					connectedPlayers.put(connectedPlayers.size() - 1, hostAddress);
				}

				//get the command that was sent by the client
				int command = Integer.parseInt(dataArray[0].trim());
				int size = dataArray.length;

				//the server received a connection request from the client
				//send each players ID
				if(command == 0) {
					String playerName = dataArray[size-1].trim();
					connectedPlayersNames.put(connectedPlayers.size() - 1, playerName);

					for(int i: connectedPlayers.keySet()) {
						output = (new StringBuilder()).append(i  + "," + connectedPlayersNames.get(i + 1) + ",");
					}
					
					createRoleList();
					output.append(command);
				}
				//the server received an update request from the client
				else if(command == 1) {
					output = (new StringBuilder()).append(dataArray[1] + "," + dataArray[2] +"," + dataArray[3] +"," + dataArray[4] +"," + dataArray[5] + "," + dataArray[6] + "," + command);
				}
				//the server received a Is ready command from the client
				else if(command == 2) {
					output = (new StringBuilder()).append(dataArray[1] + "," + command);
				}
				//the server received a assign role and start command from the client, only when everyone is ready assign roles
				else if(command == 3) {
					String assignedRole = assignRole();
					output = (new StringBuilder()).append(Integer.parseInt(dataArray[1]) + "," + assignedRole + "," + command);
				}//the server received a disconnect request from the client
				else if(command == 4) {
					System.out.println("CLOSED REQUESTED!");
					connectedPlayers.remove(Integer.parseInt(dataArray[1]));
					connectedPlayersNames.remove(Integer.parseInt(dataArray[1]));
					
					output = (new StringBuilder()).append(receivedData.substring(1) + connectedPlayers.containsKey(Integer.parseInt(dataArray[1])) + ",");
					output.append(command);
				}
	
				System.out.println("Server is sending @command: " + command);
				
				//relay the message received by the client to ALL the other clients
				for(Entry<Integer, InetAddress> e : connectedPlayers.entrySet()) {
					System.out.println("Server is sending @command: " + command + " to @ClientID: " + e.getKey() + " @Address: " + e.getValue());
					
					DatagramPacket toClients = new DatagramPacket((output.toString()).getBytes(), output.length(), e.getValue(), 8000);
					serverDatagramSocket.send(toClients);
				}
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
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
		
		if(playerTypeSelector >= 50) {
			role = "Imposter";
			rolelist.remove("Imposter");
		}
		else if(playerTypeSelector > 50) {
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