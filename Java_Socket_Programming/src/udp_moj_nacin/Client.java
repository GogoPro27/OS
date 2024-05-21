package udp_moj_nacin;

import udp.UDPClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client extends Thread{
    private String servername;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress inetAddress;
    private String message;
    private byte[] buf;

    public Client(String servername, int serverPort, String message) {
        this.servername = servername;
        this.serverPort = serverPort;
        this.message = message;
        try {
            this.buf = message.getBytes(StandardCharsets.UTF_8);
            socket = new DatagramSocket();
            inetAddress = InetAddress.getByName(servername);
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(buf,buf.length,inetAddress,serverPort);

        try {
            socket.send(packet);
            packet = new DatagramPacket(new byte[256], buf.length);
            socket.receive(packet);
            System.out.println(new String(packet.getData(),0,packet.getLength()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        UDPClient client = new UDPClient("localhost",4445,"Hello World!");
        client.start();
    }
}
