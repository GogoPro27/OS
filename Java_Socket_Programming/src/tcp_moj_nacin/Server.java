package tcp_moj_nacin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Server starting ...");
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Server failed to start !");
            return;
        }
        System.out.println("Server started !");
        System.out.println("Server waiting for connections ...");

        Main.semaphore.release(Main.NUM_CLIENTS);

        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();//blokiracka metoda (kako semafor)
                //na sekoe vlaganje vo SERVER_SOCKET se kreira SOCKET na klientot

                //VO SOKET KJE GI IMAME SITE INFORMACII KOI KLIENTOT GI PUSHTA

                //isto taka , preku soketot kje mu pushtime informacii na klientot

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            System.out.println("Server got a new client ! Created a new WorkingThread");

            Worker worker = new Worker(socket);
            worker.start();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(7001);
        server.start();

    }
}
