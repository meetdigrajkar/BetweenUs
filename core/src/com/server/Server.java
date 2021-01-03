package com.server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
	
    private DatagramSocket udpSocket;
    private int port;
    private ArrayList<InetAddress> connectedClientAddresses;
    private int sendPort;
    
	public Server(int port) throws SocketException, IOException{
		this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
        this.connectedClientAddresses = new ArrayList<>();
        this.sendPort = 7077;
	}
    
	private void listen() throws Exception {
        System.out.println("-- Running Server at " + InetAddress.getLocalHost() + "--");
        String msg;
        
        while (true) {
            
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
            // blocks until a packet is received
            udpSocket.receive(packet);
            msg = new String(packet.getData()).trim();
            
            System.out.println(
                "Message from " + packet.getAddress().getHostAddress() + ": " + msg);
           
            //check if the client is in the connected clients list.
            //if client is not in the list add its host address to the list
            for(InetAddress c: connectedClientAddresses) {
            	InetAddress hostAddress = InetAddress.getByName(packet.getAddress().getHostAddress());
            	if(!c.equals(hostAddress)) {
            		this.connectedClientAddresses.add(hostAddress);
            	}
            }
           
            //relay the message received by the client to ALL the other clients
            for(InetAddress c: connectedClientAddresses) {
            	DatagramPacket p = new DatagramPacket(
                        msg.getBytes(), msg.getBytes().length, c, sendPort);
                    
                    this.udpSocket.send(p);          
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Server server = new Server(7077);
        server.listen();
    }
}
