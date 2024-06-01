package shared;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ServerSharedResources extends Thread{
    private String loggerFile;
    private String counterFile;
    private int serverPort;
    public static Semaphore semaphore = new Semaphore(1);

    public ServerSharedResources(String loggerFile, String counterFile, int serverPort) {
        this.loggerFile = loggerFile;
        this.counterFile = counterFile;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.serverPort);
            System.out.println("Server started...waiting for other server...");

            Socket socket = null;
            while (true){
                socket = serverSocket.accept();
                System.out.println(String.format("Server just got a new client...(%s:%d)",socket.getInetAddress().getHostAddress(),socket.getPort()));
                Worker worker = new Worker(socket,new File(loggerFile),new File(counterFile));
                worker.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String logger_path = System.getenv("LOGGER_PATH");
        String counter_path = System.getenv("COUNTER_PATH");
        int serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        ServerSharedResources serverSharedResources = new ServerSharedResources(logger_path,counter_path,serverPort);
        serverSharedResources.start();
    }
}
