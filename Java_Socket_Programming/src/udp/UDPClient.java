package udp;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {

    private String serverName;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private String message;
    private byte[] buffer;

    public UDPClient(String serverName, int serverPort, String message) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message = message;

        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
            //The InetAddress is obtained from the server name,
            // allowing the client to know where to send the message.
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, serverPort);

        try {
            socket.send(packet);
            packet = new DatagramPacket(buffer, buffer.length, address, serverPort);
            socket.receive(packet); //this method blocks until packet is received
            System.out.println(new String(packet.getData(), 0, packet.getLength()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UDPClient client = new UDPClient("localhost", 4445, "Hello :)");
        client.start();
    }
}
