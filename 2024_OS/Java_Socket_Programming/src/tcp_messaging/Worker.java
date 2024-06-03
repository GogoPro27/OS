package tcp_messaging;

import java.io.*;
import java.net.Socket;

public class Worker extends Thread {
    private Socket socket;
    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

            System.out.println(String.format("Connected: %s:%d",socket.getInetAddress(),socket.getPort()));


            String line = null ;
            while (!(line = reader.readLine()).isEmpty()) {
                System.out.printf("New message from %s:%d:  %s\n", socket.getInetAddress(), socket.getPort(), line);
                writer.write(line+"\n");
                writer.flush(); //interesno u while
            }

        } catch (IOException e) {
            e.printStackTrace();
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
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}