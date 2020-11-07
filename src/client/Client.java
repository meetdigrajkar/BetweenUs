package client;
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
        this.serverAddress = InetAddress.getLocalHost();
        this.port = port;
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
        }
    }
    
	public static void main(String[] args) throws NumberFormatException, IOException {
		Client sender = new Client(7078);
        System.out.println("-- Running UDP Client at " + InetAddress.getLocalHost() + " --");
        sender.start();
	}
}
