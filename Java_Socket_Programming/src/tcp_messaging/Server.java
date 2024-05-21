package tcp_messaging;

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

        System.out.println(String.format("SERVER: started on %s:%d",serverSocket.getInetAddress(),serverSocket.getLocalPort()));
        System.out.println("SERVER: waiting for connections...");

        while (true) {
            Socket newClient = null;
            try {
                newClient = serverSocket.accept();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            System.out.println("SERVER: new client - creating new worker thread...");
            new HttpWorkerThread(newClient).start();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(9000);
        server.start();
    }
}