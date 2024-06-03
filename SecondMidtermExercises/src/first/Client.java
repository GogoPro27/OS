package first;

import java.io.*;
import java.net.InetAddress;
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

        BufferedReader br = null;
        BufferedWriter bw = null;
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(serverName),serverPort);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line;
            Scanner scanner = new Scanner(System.in);
            while ((line = br.readLine())!=null){
                System.out.println(line);
                String toSend = scanner.nextLine();
                bw.write(toSend+"\n");
                bw.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                assert socket != null;
                socket.close();
                if (br != null) br.close();
                if (bw != null) bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) {
        Client client = new Client("localhost",8888);
        client.start();
    }
}
