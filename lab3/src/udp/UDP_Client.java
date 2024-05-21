package udp;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDP_Client extends Thread{
    private byte[] buffer = new byte[256];
    private DatagramSocket socket;
    private int serverPort;
    private InetAddress inetAddress;

    public UDP_Client(int serverPort, String serverName) {
        this.serverPort = serverPort;
        try {
            this.inetAddress = InetAddress.getByName(serverName);
            socket = new DatagramSocket();
            System.out.println("Client Socket is created...");
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Log in...");

        String line;
        while ((line = scanner.nextLine())!=null){
            buffer = line.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,inetAddress,serverPort);
            try {
                socket.send(packet);
                buffer = new byte[256];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String received =new String( packet.getData(),0, buffer.length).trim();
                System.out.println("TCP_CLIENT RECEIVED: "+ received);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line.equals("logout")){
                socket.close();
                break;
            }
        }
    }

    public static void main(String[] args) {
        UDP_Client client = new UDP_Client(4445,"localhost");
        client.start();
    }
}
