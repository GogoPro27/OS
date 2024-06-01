package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread{
    public static Map<String, Subject> subjectsMap;
    public int serverPort;
    public static String sharedServerName;
    public static int sharedServerPort;


    public Server(int serverPort, String sharedServerName, int sharedServerPort) {
        this.serverPort = serverPort;
        Server.sharedServerName = sharedServerName;
        Server.sharedServerPort = sharedServerPort;
        subjectsMap = new HashMap<>();
        initializeMap();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.serverPort);
            Thread.sleep(1000);// to make sure that the shared server is set up
            System.out.println("Server started...");

            Socket socket = null;
            while (true){
                socket = serverSocket.accept();
                System.out.println(String.format("Server just got a new client...(%s:%d)",socket.getInetAddress().getHostAddress(),socket.getPort()));
                Worker worker = new Worker(socket);
                worker.start();
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeMap(){
        subjectsMap.put("ADS", new Subject(
                "Algorithms and Data Structures",
                934,
                new String[]{"Object Oriented Programming", "Structural Programming"}
        ));

        subjectsMap.put("OS", new Subject(
                "Operating Systems",
                870,
                new String[]{"Computer Architecture", "C Programming"}
        ));

        subjectsMap.put("DBMS", new Subject(
                "Database Management Systems",
                810,
                new String[]{"Data Structures", "Discrete Mathematics"}
        ));

        subjectsMap.put("CN", new Subject(
                "Computer Networks",
                750,
                new String[]{"Operating Systems", "Probability and Statistics"}
        ));

        subjectsMap.put("AI", new Subject(
                "Artificial Intelligence",
                670,
                new String[]{"Machine Learning", "Linear Algebra"}
        ));
    }
    public static void main(String[] args) {
        String sharedServerName = System.getenv("SHARED_SERVER");
        int sharedServerPort = Integer.parseInt(System.getenv("SHARED_SERVER_PORT"));
        int serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        Server server = new Server(serverPort,sharedServerName,sharedServerPort);
        server.start();

    }
}
    class Subject {

        private String name;
        private int numberOfStudents;
        private String[] prerequisiteSubjects;

        public Subject(String name, int numberOfStudents, String[] prerequisiteSubjects) {
            this.name = name;
            this.numberOfStudents = numberOfStudents;
            this.prerequisiteSubjects = prerequisiteSubjects;
        }

        public String getName() {
            return name;
        }

        public int getNumberOfStudents() {
            return numberOfStudents;
        }

        public String[] getPrerequisiteSubjects() {
            return prerequisiteSubjects;
        }
        @Override
        public String toString() {
            StringBuilder prerequisites = new StringBuilder();
            for (String prerequisite : prerequisiteSubjects) {
                prerequisites.append(prerequisite).append(", ");
            }
            // Remove the last comma and space
            if (!prerequisites.isEmpty()) {
                prerequisites.setLength(prerequisites.length() - 2);
            }
            return "Name: " + name + "\nNumber of Students: " + numberOfStudents + "\nPrerequisite subjects: " + prerequisites.toString();
        }
    }

