package tcp;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

            Request request = new Request(reader.readLine().split("\\s+"));
            System.out.println(request.command + " " + request.uri);

            String line = null;

            while (!(line = reader.readLine()).equals("")) {
                String[] parts = line.split(":\\s+", 2);
                request.headers.put(parts[0], parts[1]);
            }

            if (request.command.equals("POST") && request.headers.get("Content-Length") != null) {
                StringBuilder sb = new StringBuilder();
                int length = Integer.parseInt(request.headers.get("Content-Length").trim());
                while (length-- > 0)
                    sb.append((char) reader.read());

                request.body = sb.toString();
                System.out.println("BODY: " + request.body);
            }

            String clientName = Optional
                    .ofNullable(request.headers.get("User"))
                    .orElse(request.headers.get("User-Agent"));

            writer.write("HTTP/1.1 200 OK\n\n");
            writer.write("Hello, " + clientName + "!\n");
            writer.write("You requested to " + request.command + " the resource: " + request.uri + "\n");
            writer.write("\n");
            writer.flush();

            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Request {
        public String command;
        public String uri;
        public String version;
        public Map<String, String> headers;
        public String body;

        public Request(String[] line) {
            this.command = line[0];
            this.uri = String.join(" ", Arrays.copyOfRange(line, 1, line.length - 1));
            this.version = line[line.length - 1];
            this.headers = new HashMap<>();
        }
    }
}