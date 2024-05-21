package tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread{
    private int port;
    private static int messagesReceived;

    public TCPServer(int port) {
        this.port = port;
        messagesReceived = 0;

    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket= new ServerSocket(port);
            System.out.println("Server Socket is opened...");

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("New client accepted! Opened a new Thread!");
                WorkerThread thread = new WorkerThread(socket);
                thread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void addMessagesFromClient(int num){
        messagesReceived+=num;
        System.out.println("Num of messages after client: "+messagesReceived);
    }

    public static void main(String[] args) {
        int serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        TCPServer server = new TCPServer(serverPort);
        server.start();
    }
}
