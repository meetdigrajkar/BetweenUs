package com.server;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server {

	private static ArrayList<InetAddress> connectedClientAddresses;

	public static void main(String args[])
	{
		connectedClientAddresses = new ArrayList<>();
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

				//check if the client is in the connected clients list.
				//if client is not in the list add its host address to the list
				InetAddress hostAddress = InetAddress.getByName(datagramPacket.getAddress().getHostAddress());
				if(!connectedClientAddresses.contains(hostAddress)) {
					connectedClientAddresses.add(hostAddress);
				}
				
			
				byte[] outputData = AppendData(hostAddress,datagramPacket.getData());
				
				ByteArrayInputStream bis = new ByteArrayInputStream(outputData);
				ObjectInputStream ois = new ObjectInputStream(bis);
				int command = ois.readInt();
				
				System.out.println("Server received command: " + command + " from @hostAddress: " + hostAddress);
				
				
				//relay the message received by the client to ALL the other clients
				for(InetAddress c: connectedClientAddresses) {
					System.out.println(c);
					DatagramPacket toClients = new DatagramPacket(outputData, outputData.length, c, 8000);
					serverDatagramSocket.send(toClients);
				}

			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	public static byte[] AppendData(InetAddress hostAddress, byte[] receivedData) throws IOException {
		
		ByteArrayOutputStream bos = new  ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		
		oos.writeInt(connectedClientAddresses.indexOf(hostAddress)); 
		oos.flush();
		
		byte [] data = bos.toByteArray();
		byte[] combinedData = new byte[receivedData.length + data.length];
		
		for(int i =0; i<combinedData.length;i++) {
			combinedData[i] = i < receivedData.length ? receivedData[i]: data[i - receivedData.length];
		}
		
		return combinedData;
		
	}
}