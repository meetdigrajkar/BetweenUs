package com.mmog;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
public class Client
{
	int port = 7077;
	static DatagramSocket socket;
	static InetAddress address;
    static ByteArrayOutputStream bos;
	static ObjectOutputStream oos;
	
	public Client() throws IOException {
		bos = new ByteArrayOutputStream();
		oos = new ObjectOutputStream(bos);
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
			String receivedString;
			while(active)
			{
				try
				{
					// listen for incoming datagram packet
					datagramSocket.receive(incoming);
					// print out received string
					
					parseCommand(incoming.getData());
					
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

	public void startClient() {
		System.out.println("Client Ready...");
	}

	public static boolean connectClientToServer() {
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
			String input = "";
			
			//command int for connection request.
			input += "0,";
			 
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
	
	public static void sendUpdate(float x, float y, boolean isFlipped, boolean isDead, boolean isIdle) throws Exception
	{
		  oos.writeInt(1); 
	      oos.writeFloat(x);
	      oos.writeFloat(y);
	      oos.writeBoolean(isFlipped);
	      oos.writeBoolean(isDead);
	      oos.writeBoolean(isIdle);
	      oos.flush();
	      byte [] data = bos.toByteArray();
	      
	      socket = new DatagramSocket(8000);
	      
	      DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, 7077);	     
	      socket.send(datagramPacket);	      	      
	}
	
	/*
	 * This parseCommand method is called when the Client receives a datagram packet from the server (byte array)
	 */
	public static void parseCommand(byte[] data) throws IOException
	{
		String receivedData = new String(data);
		String[] dataArray = parseData(receivedData);
		int playerID = Integer.parseInt(dataArray[0]);
		
		if (dataArray[1].equals("0"))//connect command
		{
			System.out.println("Connected with @ClientID: " + playerID);
		}
		else if (dataArray[0].equals("1"))//update command
		{
			//float x = ois.readFloat();
			//float y = ois.readFloat();
			//boolean isFlipped = ois.readBoolean();
			//boolean isDead = ois.readBoolean();
			//boolean isIdle = ois.readBoolean();
			//Server should append this after receiving a message before forwarding
			//int playerID = ois.readInt();
			
			
			//System.out.println("Message From player " + playerID + 
			//String.format("(X, Y) = (%f, %f)", x,y   ));
			
		}
		
	}
	
	public static String[] parseData(String receivedData) throws IOException {
		String dataArray[] = receivedData.split(",");
		return dataArray;
	}

} 