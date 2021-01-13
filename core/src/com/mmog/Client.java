package com.mmog;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private DatagramSocket udpSocket;
    private InetAddress serverAddress;
    private int port,sendport;
    private Scanner scanner;
	
	public Client(int port) throws IOException {
		//this address is replaced by the address of the machine the server actually runs on
		//the network that the server runs on needs to have the port forwarded for 7077
        this.serverAddress = InetAddress.getByName("72.137.66.20");
        this.port = port;
        //send port is the port the server listens on.
        this.sendport = 7077;
        udpSocket = new DatagramSocket(this.port);
        scanner = new Scanner(System.in);
	}
	
    private int start() throws IOException {
        String in;
        while (true) {
            in = scanner.nextLine();
            
            DatagramPacket p = new DatagramPacket(
                in.getBytes(), in.getBytes().length, serverAddress, sendport);
            
            this.udpSocket.send(p);    
            
            
            
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
            // blocks until a packet is received
            udpSocket.receive(packet);
            String msg = new String(packet.getData()).trim();
            
            System.out.println(
                "Message from " + packet.getAddress().getHostAddress() + ": " + msg);
        }
    }
    
	public static void main(String[] args) throws NumberFormatException, IOException {
		Client client = new Client(7078);
        System.out.println("-- Running UDP Client at " + InetAddress.getLocalHost() + " --");
        client.start();
	}
}
