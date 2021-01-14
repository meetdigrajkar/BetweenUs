package com.mmog;

import java.net.*;
import java.io.*;
public class Client
{
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
					receivedString = new String(incoming.getData(),
							0, incoming.getLength());
					System.out.println("Received from server:" + receivedString);
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
		InetAddress address = null;
		int port = 7077;
		DatagramSocket datagramSocket = null;
		BufferedReader keyboardReader = null;
		// Create a Datagram Socket...
		try
		{ 
			address = InetAddress.getByName("72.137.66.20");
			if(address.equals(InetAddress.getLocalHost())) {
				address = InetAddress.getByName("127.0.0.1");
			}

			datagramSocket = new DatagramSocket(8000);
			keyboardReader = new BufferedReader(new
					InputStreamReader(System.in));
		}
		catch (IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		
		UDPEchoReader reader = new UDPEchoReader(datagramSocket);
		reader.setDaemon(true);
		reader.start();
		
		System.out.println("Ready to send your messages...");
		try
		{
			String input;
			while (true)
			{
				// read input from the keyboard
				input = keyboardReader.readLine();
				// send datagram packet to the server
				DatagramPacket datagramPacket = new DatagramPacket
						(input.getBytes(), input.length(), address, port);
				datagramSocket.send(datagramPacket);
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}	
	}

} 