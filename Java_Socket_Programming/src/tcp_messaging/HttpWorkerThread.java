package tcp_messaging;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpWorkerThread extends Thread{
    private Socket socket;

    public HttpWorkerThread(Socket socket) {
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
            List<String> lines = new ArrayList<>();
            while (!(line = reader.readLine()).isEmpty()) {
                lines.add(line);
                System.out.println(line);
            }
            System.out.println();
            WebRequest webRequest = WebRequest.RequestBuilder(lines);
            writer.write("HTTP/1.1 200 OK\n\n");
            if (webRequest.getMethod().equals("GET") && webRequest.getPath().equals("/time")){
                String html = String.format("<html><body><h1>%s</h1></body></html>", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                writer.write(html);
            }
            else {
                String html = "<html><body><h1>Hello World</h1></body></html>";
                writer.write(html);
            }

            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (reader!=null){
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
    public static class WebRequest {
        private String method;
        private String path;
        private String protocol;
        private Map<String,String> headers;

        private WebRequest(String method, String path, String protocol, Map<String, String> headers) {
            this.method = method;
            this.path = path;
            this.protocol = protocol;
            this.headers = headers;
        }

        public static WebRequest RequestBuilder(List<String> lines){
            String method = lines.get(0).split("\\s+")[0];
            String path = lines.get(0).split("\\s+")[1];
            String protocol = lines.get(0).split("\\s+")[2];
            HashMap<String,String> hashMap = new HashMap<>();

            lines.stream().skip(1).forEach(i->{
                String[] parts = i.split(":\\s+");
                hashMap.put(parts[0],parts[1]);
            });
            return new WebRequest(method, path, protocol, hashMap);
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }
    }
}
