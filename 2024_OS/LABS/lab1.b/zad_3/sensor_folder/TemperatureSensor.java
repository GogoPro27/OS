import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TemperatureSensor {
    public static void main(String[] args) {
        Random random = new Random();
        String filePath = "./data/temperature.txt";

        // Ensure the file exists
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Create directories if they do not exist
                file.createNewFile(); // Create the file if it does not exist
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                for (int i = 0; i < 5; i++) {
                    int temperature = 5 + random.nextInt(46); // Random integer in range [5, 50]
                    writer.write(temperature + "\n");
                }
                writer.flush();
                System.out.println("Napisani 5 novi statistiki vo temperature.txt...");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000); // Sleep for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

