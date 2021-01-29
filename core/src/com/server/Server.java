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

	//list of rooms
	private static ArrayList<Room> rooms;

	public static void main(String args[]) throws ClassNotFoundException, IOException
	{	
		rooms = new ArrayList<Room>();

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

				String receivedData = new String(datagramPacket.getData(),datagramPacket.getOffset(),datagramPacket.getLength());

				System.out.println("received data: " + receivedData);
				String[] dataArray = parseData(receivedData);

				//check if the client is in the connected clients list.
				//if client is not in the list add its host address to the list
				InetAddress hostAddress = InetAddress.getByName(datagramPacket.getAddress().getHostAddress());

				//get the command that was sent by the client
				int command = Integer.parseInt(dataArray[0].trim());

				//parse the command
				parseCommandAndSend(command, dataArray, hostAddress,serverDatagramSocket);

				if(!rooms.isEmpty()) {
					System.out.println("number of rooms: " + rooms.size());
					for(Room room: rooms) {
						System.out.println("# of players in @room: " + room.getRoomName() + " is: " + room.getAllPlayersNames());
					}
				}
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

	//sends the command to all the clients in the room to start the game
	public static void sendStartGameCommand(Room room, DatagramSocket serverDatagramSocket) {
		StringBuilder toAllClients = (new StringBuilder());
		String toallString = "";

		//appending only crew member for now, but should be the assigned role.
		toAllClients.append("CrewMember").append(",");
		toAllClients.append(3);
		toallString = toAllClients.toString();

		for(Entry<Integer, InetAddress> e : room.connectedPlayers.entrySet()) {
			InetAddress address = e.getValue();

			DatagramPacket toSend = new DatagramPacket(toallString.getBytes(), toallString.getBytes().length, address, 8000);

			try {
				serverDatagramSocket.send(toSend);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return;
	}


	public static void addRoom(String hostName, String roomName, float numCrew, float numImp, InetAddress hostAddress) {
		Room room = new Room(hostName, roomName, hostAddress,numCrew, numImp);
		room.addPlayer(hostName, hostAddress);
		rooms.add(room);
	}

	public static void parseCommandAndSend(int command, String[] dataArray, InetAddress hostAddress,DatagramSocket serverDatagramSocket) {
		StringBuilder toAllClients = new StringBuilder();
		StringBuilder toLocalc = new StringBuilder();
		String roomName = null;
		boolean toLocal = false, toAll = false;

		//the server received a connection request from the client
		//send each players ID
		if(command == 0) {
			toLocal = true;
			toAll = true;

			toLocalc = new StringBuilder();
			String playerName = dataArray[1].trim();
			roomName = dataArray[2];
			//get the room that the player wants to join

			for(Room room: rooms) {
				if(room.getRoomName().equals(roomName)) {
					room.addPlayer(dataArray[1], hostAddress);

					for(Entry<Integer,InetAddress> e: room.connectedPlayers.entrySet()) {
						InetAddress address = e.getValue();
						int playerID = e.getKey();
						String name = room.connectedPlayersNames.get(playerID);

						//sending to the client the packet came from
						if(!name.equals(playerName)) {
							toLocalc.append(playerID).append(",").append(name).append(",");
						}		

						//sending to all the other clients
						if(address.equals(hostAddress)) {
							toAllClients.append(playerID).append(",").append(name).append(",");
						}
					}

					//append the command at the end.
					toLocalc.append(command);
					toAllClients.append(command);

					createRoleList(roomName);
				}
			}
		}
		//the server received an update request from the client
		else if(command == 1) {
			//needs to send this to all the other clients
			toLocal = false;
			toAll = true;
			roomName = dataArray[2];
			int playerID = -5;
			System.out.println(roomName);
			toAllClients = (new StringBuilder());

			for(Room room: rooms) {
				if(room.getRoomName().equals(roomName)) {
					System.out.println("found room");

					room.addPlayer(dataArray[1], hostAddress);

					for(int i = 3; i < dataArray.length; i++) {
						//System.out.println(dataArray[i].trim());
						toAllClients.append(dataArray[i].trim()).append(",");
					}

					for(Entry<Integer, String> e: room.connectedPlayersNames.entrySet()) {
						if(e.getValue().equals(dataArray[1])) {
							playerID = e.getKey();
						}
					}
				}
			}

			toAllClients.append(playerID).append(",");
			toAllClients.append(command);
		}//host sent the command to start the game.
		else if(command == 3) {
			String rname = dataArray[1];
			
			for(Room room: rooms) {
				if(room.getRoomName().equals(rname)) {
					room.startGame = true;
					sendStartGameCommand(room, serverDatagramSocket);
					break;
				}
			}
		}
		//the server received a disconnect request from the client
		else if(command == 4) {
			toLocal = false;
			toAll = true;

			roomName = dataArray[1];
			String name = dataArray[2];

			toAllClients = (new StringBuilder());
			System.out.println("CLOSED REQUESTED!: " + "for @Player: " + name + " in @Room-name: " + roomName);

			for(Room room: rooms) {
				System.out.println(room.getRoomName());
				
				if(room.getRoomName().equals(roomName)) {
					//find the room that the player is in and then remove the player from the names and address hash map
					System.out.println("player removed: " + room.removePlayer(name));
				}
			}
			
			toAllClients.append(name).append(",");
			toAllClients.append(command);
		}
		//command to create a room and connect the host player
		else if(command == 5) {
			roomName = dataArray[1];
			//dataArray[0] = command
			//dataArray[1] = room name
			//dataArray[2] = host name
			//dataArray[3] = num of crew
			//dataArray[4] = num of imposters

			addRoom(dataArray[2], dataArray[1], Float.parseFloat(dataArray[3]), Float.parseFloat(dataArray[4]), hostAddress);

			//sending the room host the host name, and the room name back
			toLocalc = new StringBuilder();
			toLocalc.append(dataArray[1]).append(",").append(dataArray[2]).append(",").append(command);
		}
		//refresh command for updating available rooms
		else if(command == 6) {
			toLocalc = new StringBuilder();
			//dataArray[0] = command
			//dataArray[1] = player name

			for(Room room: rooms) {
				toLocalc.append(room.getRoomName()).append(",");
			}

			toLocalc.append(command);
		}

		//send the command
		sendCommand(toLocalc.toString(),toAllClients.toString(),serverDatagramSocket, command, toLocal, toAll, hostAddress, roomName);
	}

	public static void sendCommand(String toLocalc, String toAllClients, DatagramSocket serverDatagramSocket, int command, boolean toLocal, boolean toAll, InetAddress hostAddress, String roomName) {
		DatagramPacket toSend = new DatagramPacket(toLocalc.getBytes(), toLocalc.getBytes().length, hostAddress, 8000);

		//refresh available rooms command sent
		if(command == 6 || command == 5) {
			try {
				serverDatagramSocket.send(toSend);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		for(Room room: rooms) {
			if(room.getRoomName().equals(roomName)) {
				for(Entry<Integer, InetAddress> e : room.connectedPlayers.entrySet()) {
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

					//System.out.println(toAllClients);

					if((command == 0) && address.equals(hostAddress)) {
						toSend = new DatagramPacket(toLocalc.getBytes(), toLocalc.getBytes().length, hostAddress, 8000);
					}
					else if((command == 0 || command == 1 || command == 4 ) && !address.equals(hostAddress)) {
						toSend = new DatagramPacket(toAllClients.getBytes(), toAllClients.getBytes().length, address, 8000);
					}

					try {
						//change the packet to send based on whether to send to local, all (except local), and both
						System.out.println("Server is sending @command: " + command + " to @ClientID: " + e.getKey() + " @Address: " + hostAddress);
						if (toLocal && address.equals(hostAddress))
						{
							serverDatagramSocket.send(toSend);
						}
						else if (toAll && !address.equals(hostAddress))
						{
							serverDatagramSocket.send(toSend);
						}
						else if (!toAll && !toLocal)
						{
							System.out.println("ma-ma-ma-money shot");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public static void createRoleList(String roomName) {
		for(Room room: rooms) {
			if(room.getRoomName().equals(roomName)) {
				//populate role list with roles
				for(int i = 0; i <room.connectedPlayers.size(); i++) {
					if(room.connectedPlayers.size() >= 8 && i < 2 ) {
						room.rolelist.add("Imposter");
					}
					else if(room.connectedPlayers.size() < 8 && i == 0 ) {
						room.rolelist.add("Imposter");
					}
					else
						room.rolelist.add("CrewMember");
				}
			}
		}
	}

	public static String assignRole(String roomName) {
		String role = "";
		//every player has a 50/50 chance of being imposter or crewmember
		int upperBound = 100;

		for(Room room: rooms) {
			if(room.getRoomName().equals(roomName)) {
				int playerTypeSelector = room.getR().nextInt(upperBound);
				if(playerTypeSelector < 50 && room.rolelist.contains("Imposter")) {
					role = "Imposter";
					room.rolelist.remove("Imposter");
				}
				else if(playerTypeSelector >= 50 && room.rolelist.contains("CrewMember")) {
					role = "CrewMember";
					room.rolelist.remove("CrewMember");
				}
				else if (!room.rolelist.contains("Imposter") && room.rolelist.contains("CrewMember")) {
					role = "CrewMember";
					room.rolelist.remove("CrewMember");
				}
			}
		}

		return role;
	}

	public static String[] parseData(String receivedData) throws IOException {
		String dataArray[] = receivedData.split(",");
		return dataArray;
	}
}