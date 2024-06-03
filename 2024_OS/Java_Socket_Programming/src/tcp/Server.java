package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("SERVER: starting...");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Socket Server failed to start");
            return;
        }

        System.out.println("SERVER: started");
        System.out.println("SERVER: waiting for connections...");

        while (true) {
            Socket newClient = null;
            try {
                newClient = serverSocket.accept();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            System.out.println("SERVER: new client - creating new worker thread...");
            new Worker(newClient).start();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(7002);
        server.start();
    }
}