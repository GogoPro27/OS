package tcp;

import java.io.*;
import java.net.Socket;

public class WorkerThread extends Thread{
    private Socket socket;
    private boolean isFirstMessage;

    public WorkerThread(Socket socket) {
        this.socket = socket;
        this.isFirstMessage = true;
    }

    @Override
    public void run() {
        int numMessages = 0;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                System.out.println(String.format("RECIEVED(from: %s:%d) : %s",socket.getInetAddress().getHostAddress(),socket.getPort(),line));
                numMessages++;

                if(isFirstMessage && !line.equals("login")){
                    writer.write("You are disconnected!\n");
                    writer.flush();
                    break;
                }else if (isFirstMessage){
                    isFirstMessage = false;
                    line = "logged in";
                }else if(line.equals("logout")){
                    writer.write("logged out!\n");
                    writer.flush();
                    break;
                }

                writer.write(line + "\n");
                writer.flush();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                socket.close();
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            TCPServer.addMessagesFromClient(numMessages);
        }
    }
}
