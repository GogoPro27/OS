import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TemperatureMonitor {
    public static void main(String[] args) {
        String inputFilePath = "./data/temperatures/temperature.txt";
        String outputFilePath = "./data/levels/temperaturelevel.txt";

        // Ensure the input file exists
        try {
            File inputFile = new File(inputFilePath);
            if (!inputFile.exists()) {
                inputFile.getParentFile().mkdirs(); // Create directories if they do not exist
                inputFile.createNewFile(); // Create the file if it does not exist
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure the output file exists
        try {
            File outputFile = new File(outputFilePath);
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs(); // Create directories if they do not exist
                outputFile.createNewFile(); // Create the file if it does not exist
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            List<Integer> temperatures = new ArrayList<>();

            // Read temperatures from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    temperatures.add(Integer.parseInt(line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!temperatures.isEmpty()) {
                // Calculate average temperature
                double average = temperatures.stream().mapToInt(Integer::intValue).average().orElse(0);
                int low = Integer.parseInt(System.getenv("LOW_TEMPERATURE"));
                int medium = Integer.parseInt(System.getenv("MEDIUM_TEMPERATURE"));
                int high = Integer.parseInt(System.getenv("HIGH_TEMPERATURE"));

                // Determine the monitor result
                String monitorResult;
                if (average >= low && average < medium) {
                    monitorResult = "low";
                } else if (average >= medium && average < high) {
                    monitorResult = "medium";
                } else if (average >= high) {
                    monitorResult = "high";
                } else {
                    monitorResult = "out of range";
                }

                // Append monitor result to the output file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
                    writer.write(monitorResult + "\n");
                    writer.flush();
                    System.out.println("Spored merenjata na monitorot, temperaturata do sega e "+monitorResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Sleep for 10 seconds
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

