package udp_messaging;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client extends Thread{
    private DatagramSocket socket;
    private byte[] buffer;
    private String serverName;
    private int serverPort;

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        try {
            socket = new DatagramSocket();
            System.out.println("Socket open...write a message...");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                buffer = scanner.nextLine().getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length, InetAddress.getByName(serverName), serverPort);
                socket.send(packet);
                buffer = new byte[256];
                packet = new DatagramPacket(buffer, 0, 256);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, 256);
                System.out.println(received);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost",8888);
        client.start();
    }
}
