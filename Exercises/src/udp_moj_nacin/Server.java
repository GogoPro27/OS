package udp_moj_nacin;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends Thread{
    private DatagramSocket socket;
    private byte[] buffer = new byte[256];

    public Server(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(new byte[256],buffer.length);

        while (true){
            try {
                socket.receive(packet);
                String received = new String(packet.getData(),0,buffer.length);
                System.out.println("RECIEVED: "+received);
                InetAddress address = packet.getAddress(); //kaj udp mora da ja znnaeme IP na klientot
                int clientPort = packet.getPort();

                packet = new DatagramPacket(buffer, buffer.length,address,clientPort);
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(4445);
        server.start();
    }
}
