package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDP_Server extends Thread{
    private byte [] buffer = new byte[256];
    private final DatagramSocket socket;
    private boolean isLoggedIn;

    public UDP_Server(int port) {
        isLoggedIn = false;
        try {
            socket = new DatagramSocket(port);
            System.out.println("Server Socket created...");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Server started...waiting for messages...");

        while (true) {
            buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
            try {
                socket.receive(packet);
                String received_message = new String(packet.getData(),0,buffer.length).trim();

                System.out.println("UDP_SERVER RECEIVED: "+received_message);
                if(!received_message.equals("login") && !isLoggedIn) {
                    buffer = new byte[256];
                    buffer = "Please log in first".getBytes();
                }else if (received_message.equals("login")){
                    buffer = new byte[256];
                    buffer = "logged in".getBytes();
                    isLoggedIn = true;
                } else if (received_message.equals("logout")) {
                    buffer = new byte[256];
                    buffer = "logged out".getBytes();
                }

                InetAddress inetAddress = packet.getAddress();
                int port = packet.getPort();

                packet = new DatagramPacket(buffer,buffer.length,inetAddress,port);
                socket.send(packet);

                if(received_message.equals("logout")) {
                    socket.close();
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static void main(String[] args) {
        UDP_Server server = new UDP_Server(4445);
        server.start();
    }
}
