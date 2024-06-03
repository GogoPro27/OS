package tcp_messaging_plus_stuff;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private ServerSocket serverSocket;

    public Server(int serverPort) {
        try {
             this.serverSocket = new ServerSocket(serverPort);
            System.out.println("Server started...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Socket socket = null;
        System.out.println("Server waiting for connections...");

        try {
            while (true) {
                socket = serverSocket.accept();
                System.out.println(String.format("New client (%s:%d)...",socket.getInetAddress().getHostAddress(),socket.getPort()));
                Worker worker = new Worker(socket);
                worker.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8888);
        server.start();
    }
}
