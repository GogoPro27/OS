package tcp_messaging;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{
    private String serverName;
    private int serverPort;

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        BufferedWriter writer= null;
        BufferedReader reader= null;

        try {
            socket = new Socket(serverName,serverPort);//host,port
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                String line = scanner.nextLine();
                writer.write(line+"\n");
                writer.flush();
                if (line.isEmpty())break;
            }

            String line = null ;
            while ((line = reader.readLine())!=null) {
                System.out.printf("New message from %s:%d:  %s\n", socket.getInetAddress(), socket.getPort(), line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                assert socket != null;
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Client client= new Client("localhost",9000);
        client.start();
    }
}
