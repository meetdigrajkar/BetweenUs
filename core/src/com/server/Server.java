package com.server;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.*;

public class Server {

	private static ArrayList<InetAddress> connectedClientAddresses;
	static ByteArrayOutputStream bos;
	static ObjectOutputStream oos;

	public static void main(String args[]) throws ClassNotFoundException, IOException
	{
		bos = new  ByteArrayOutputStream();
		oos = new ObjectOutputStream(bos);
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

				//received data from the client
				input = new String(buffer);
				String output=(new StringBuilder()).append(connectedClientAddresses.indexOf(hostAddress)).append(",").append(input).toString();
				System.out.println(output);

				String[] data = parseData(output);

				System.out.println("Server received command: " + data[1] + " from @hostAddress: " + hostAddress);
				//System.out.println("command: " + data[0] + " ,playerID: " + data[1]);

				//relay the message received by the client to ALL the other clients
				for(InetAddress c: connectedClientAddresses) {
					System.out.println(c);
					if(!c.equals(hostAddress)) {
						DatagramPacket toClients = new DatagramPacket(output.getBytes(), output.length(), c, 8000);
						serverDatagramSocket.send(toClients);
					}
				}
				
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

	public static String[] parseData(String receivedData) throws IOException {
		String dataArray[] = receivedData.split(",");
		return dataArray;
	}
}