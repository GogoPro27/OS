package first;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    public static File dataCSV;
    public ServerSocket serverSocket;

    public Server(String filePath) {
        dataCSV = new File(filePath);
        try {
            serverSocket = new ServerSocket(8888);
            System.out.println("Server started...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Socket socket = null;
        while (true){

            try {
                socket = serverSocket.accept();
                System.out.println("Got a new client...");
                Worker worker = new Worker(socket,dataCSV);
                worker.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static synchronized void logInFile(String line) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataCSV,true)));
        bw.append(line).append("\n"); //iako samo po sebe e sinhronizirano
        bw.flush();
        bw.close();
    }

    public static void main(String[] args) {
        Server server = new Server("/Users/gorazd/IdeaProjects/os-2022-23/SecondMidtermExercises/src/first/data.csv");
        server.start();
    }
}
