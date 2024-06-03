import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemperatureMonitor {
    public static void main(String[] args) {
        String inputFilePath = "./data/temperature.txt";
        String outputFilePath = "./data/temperaturelevel.txt";
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

                // Determine the monitor result
                String monitorResult;
                if (average >= 5 && average < 19) {
                    monitorResult = "low";
                } else if (average >= 19 && average < 35) {
                    monitorResult = "medium";
                } else if (average >= 35 && average <= 50) {
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

