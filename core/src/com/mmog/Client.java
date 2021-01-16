package com.mmog;

import java.net.*;
import java.io.*;
public class Client
{
	int port = 7077;
	static DatagramSocket socket;
	static InetAddress address;

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
					
					
				/*	receivedString = new String(incoming.getData(),
							0, incoming.getLength());
					if(receivedString != null) {
						System.out.println("Received from server: @message: " + receivedString + "...CONNECTED!");
					}*/
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
		System.out.println("Button pressed");
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

		//System.out.println("Ready to send your messages...");
		try
		{
			String input;

			// read input from the keyboard
			input = "Attempting to Connect to The Server...";
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
		ByteArrayOutputStream bos = new  ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
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
	
	public static void parseCommand(byte[] data) throws IOException
	{
		
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bis);
		int command = ois.readInt();
		
		if (command == 0)//connect command
		{
			
		}
		else if (command == 1)//update command
		{
			float x = ois.readFloat();
			float y = ois.readFloat();
			boolean isFlipped = ois.readBoolean();
			boolean isDead = ois.readBoolean();
			boolean isIdle = ois.readBoolean();
			//Server should append this after receiving a message before forwarding
			int playerID = ois.readInt();
			
			
			System.out.println("Message From player " + playerID + 
					String.format("(X, Y) = (%f, %f)", x,y   ));
			
		}
		
	}
	
	

} 