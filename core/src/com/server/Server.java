package com.server;

import java.net.*;
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
				}	
				
				//if the room is empty, remove room
				ArrayList<Room> roomsToRemove = new ArrayList<Room>();
				for(Room room: rooms) {
					if(room.isRoomEmpty()) {
						roomsToRemove.add(room);
					}
				}
				rooms.removeAll(roomsToRemove);
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

	
	public static void sendReactorTaskCompletedCommand(Room room, DatagramSocket serverDatagramSocket) {
		StringBuilder toAllClients = (new StringBuilder());
		String toallString = "";
		
		toAllClients.append(true).append(",");
		toAllClients.append(9);
		toallString = toAllClients.toString();
		
		for(ServerPlayer player : room.allPlayers) {
			InetAddress address = player.getAddress();

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

	//sends the command to all the clients in the room to start the game
	public static void sendStartGameCommand(Room room, DatagramSocket serverDatagramSocket) {
		String toallString = "";
		String role = "";
		
		for(ServerPlayer player : room.allPlayers) {
			InetAddress address = player.getAddress();
			StringBuilder toAllClients = (new StringBuilder());
			
			//assign a random role from the role list
			System.out.println(room.printRoleList());
			
			if(room.rolelist.size() > 0) {
				role = room.assignRole();
				
				player.setRole(role);
				System.out.println("Player: " + player.getPlayerName() + " was assigned: " + player.getRole());
				
				toAllClients.append(role).append(",");
				toAllClients.append(3);
				toallString = toAllClients.toString();
				
				System.out.println("Sending role: " + role + " to address: " + address);
				DatagramPacket toSend = new DatagramPacket(toallString.getBytes(), toallString.getBytes().length, address, 8000);

				try {
					serverDatagramSocket.send(toSend);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return;
	}


	public static void addRoom(String hostName, String roomName, int numCrew, int numImp, InetAddress hostAddress) {
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
			
			System.out.print("connection from: " + playerName);
			for(Room room: rooms) {
				room.addPlayer(dataArray[1], hostAddress);
				
				if(room.getRoomName().equals(roomName)) {
					for(ServerPlayer player : room.allPlayers) {
						InetAddress address = player.getAddress();
						int playerID = player.getPlayerID();
						String name = player.getPlayerName();

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

					for(ServerPlayer player : room.allPlayers) {
						if(player.getPlayerName().equals(dataArray[1])) {
							playerID = player.getPlayerID();
						}
					}
				}
			}

			toAllClients.append(playerID).append(",");
			toAllClients.append(command);
		}
		//start command sent by a host of a room
		else if(command == 3) {
			String roomname = dataArray[1];

			for(Room room: rooms) {
				if(room.getRoomName().equals(roomname)) {
					room.startGame = true;
					System.out.println(room.printRoleList());
					sendStartGameCommand(room, serverDatagramSocket);
				}
			}
		}
		//the server received a disconnect request from the client
		else if(command == 4) {
			toLocal = false;
			toAll = true;

			roomName = dataArray[1];
			
			toAllClients = (new StringBuilder());
			String name = dataArray[2];
			System.out.println("CLOSED REQUESTED!" + "for @Player: " + name + "in @Room-name: " + roomName);

			for(Room room: rooms) {
				if(room.getRoomName().equals(roomName)) {
					ServerPlayer p = room.removePlayer(name);
					
					//check if the host has disconnected
					if(room.hostLeft()) {
						room.transferHost(p);
					}
			
					toAllClients.append(name).append(",");
				}
			}
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

			addRoom(dataArray[2], dataArray[1], (int) Float.parseFloat(dataArray[3]), (int) Float.parseFloat(dataArray[4]), hostAddress);

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
				if(!room.startGame) {
					toLocalc.append(room.getRoomName()).append(",");
				}
			}

			toLocalc.append(command);
		}
		//1/2 reactor task was completed
		else if(command == 9) {
			String roomname = dataArray[1];
			for(Room room: rooms) {
				if(room.getRoomName().equals(roomname)) {
					room.reactorTaskCompleted.add(true);
					break;
				}
				//System.out.println("reactor task completed message from @address: " + hostAddress + " size of reactor task list is 2: " + (room.reactorTaskCompleted.size() == 2))
			}
			
			//if both reactor tasks are complete send a message to all clients
			for(Room room: rooms) {
				if(room.reactorTaskCompleted.size() == 2) {
					boolean firstr = room.reactorTaskCompleted.get(0);
					boolean secondr = room.reactorTaskCompleted.get(1);
					
					System.out.println("1st reactor task: " + firstr + " 2nd reactor task: " + secondr);
					if(firstr && secondr) {
						System.out.println("sending completed: " + "1st reactor task: " + firstr + " 2nd reactor task: " + secondr);
						sendReactorTaskCompletedCommand(room,serverDatagramSocket);
					}
				}
			}
		}
		//player was killed command was sent
		else if(command == 10)
		{
			toAll = true;
			toLocal = false;
			
			roomName = dataArray[1];
			String pname = dataArray[2];
			
			toAllClients = (new StringBuilder());
			toAllClients.append(pname).append(",").append(command);
		}
		//imposter's sent the lights command
		else if(command == 11) {
			sendLightsToCrew(command, dataArray[1],serverDatagramSocket);
		}
		//imposter's vent command
		else if(command == 12) {
			toLocal = false;
			toAll = true;
			
			roomName = dataArray[1];
			String pn = dataArray[2];
			
			toAllClients = (new StringBuilder());
			toAllClients.append(pn).append(",").append("true").append(",").append(command);
		}
		else if(command == 13) {
			toLocal = false;
			toAll = true;
			
			roomName = dataArray[1];
			String pn = dataArray[2];
			
			toAllClients = (new StringBuilder());
			
			toAllClients.append(pn).append(",");
			toAllClients.append("false").append(",");
			toAllClients.append(dataArray[3]).append(",");
			toAllClients.append(dataArray[4]).append(",");
			toAllClients.append(dataArray[5]).append(",");
			toAllClients.append(dataArray[6]).append(",");
			toAllClients.append(dataArray[7]).append(",");
			
			toAllClients.append(command);
		}
		//reactor command sent
		else if(command == 14) {
			toLocal = false;
			toAll = true;
			
			roomName = dataArray[1];
			String pn = dataArray[2];
			
			toAllClients = (new StringBuilder());
			toAllClients.append(pn).append(",");
			toAllClients.append(command);
		}
		else if(command == 15) {
			sendLightsToCrew(command,dataArray[1],serverDatagramSocket);
		}
		//received a vote from a player
		else if(command == 16) {
			for(Room room: rooms) {
				if(dataArray[1].equals(room.getRoomName())){
					if(Boolean.parseBoolean(dataArray[2])) {
						room.addVote(dataArray[3]);
					}
					else {
						room.addVote("skipped");
					}
				}
			}
		}
		//get the votes command
		else if(command == 17) {
			toLocal = true;
			toAll = true;
			
			toAllClients = new StringBuilder();
			toLocalc = new StringBuilder();
			roomName = dataArray[1];
			
			for(Room room: rooms) {
				if(dataArray[1].equals(room.getRoomName())){
					toAllClients.append(room.getPlayerNameAndNumVotes());
					toLocalc.append(room.getPlayerNameAndNumVotes());
					
					room.votes.clear();
				}
			}
		
			toAllClients.append(command);
			toLocalc.append(command);
		}
		
		//send the command
		sendCommand(toLocalc.toString(),toAllClients.toString(),serverDatagramSocket, command, toLocal, toAll, hostAddress, roomName);
	}
	
	public static void sendLightsToCrew(int command, String roomName,DatagramSocket serverDatagramSocket) {
		ArrayList<ServerPlayer> crew = new ArrayList<ServerPlayer>();
		String toallString = "";
		
		StringBuilder toAllClients = (new StringBuilder());
		toAllClients.append(command);
		toallString = toAllClients.toString();
		
		for(Room room: rooms) {
			if(room.getRoomName().equals(roomName)) {
				crew = room.getCrewMembers();
				
				for(ServerPlayer p: crew) {
					InetAddress address = p.getAddress();
	
					DatagramPacket toSend = new DatagramPacket(toallString.getBytes(), toallString.getBytes().length, address, 8000);

					try {
						serverDatagramSocket.send(toSend);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public static void sendCommand(String toLocalc, String toAllClients, DatagramSocket serverDatagramSocket, int command, boolean toLocal, boolean toAll, InetAddress hostAddress, String roomName) {
		DatagramPacket toSend = new DatagramPacket(toLocalc.getBytes(), toLocalc.getBytes().length, hostAddress, 8000);

		//refresh available rooms command sent
		if(command == 6 || command == 5) {
			try {
				System.out.println("Sending: " + toLocalc + " to " + "@address:" + hostAddress);
				serverDatagramSocket.send(toSend);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		for(Room room: rooms) {
			if(room.getRoomName().equals(roomName)) {
				for(ServerPlayer player : room.allPlayers) {
					InetAddress address = player.getAddress();

					if((command == 0 || command == 17) && address.equals(hostAddress)) {
						toSend = new DatagramPacket(toLocalc.getBytes(), toLocalc.getBytes().length, hostAddress, 8000);
					}
					else if((command == 0 || command == 1 || command == 4 || command == 10 || command == 12 || command == 13 || command == 14 || command == 17) && !address.equals(hostAddress)) {
						toSend = new DatagramPacket(toAllClients.getBytes(), toAllClients.getBytes().length, address, 8000);
					}

					try {
						//change the packet to send based on whether to send to local, all (except local), and both
						System.out.println("Server is sending @command: " + command + " to @ClientID: " + player.getPlayerID() + " @Address: " + address);
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

	public static String[] parseData(String receivedData) throws IOException {
		String dataArray[] = receivedData.split(",");
		return dataArray;
	}
}