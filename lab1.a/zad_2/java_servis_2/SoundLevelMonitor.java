import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SoundLevelMonitor {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(SoundLevelMonitor::write_avg, 0, 7, TimeUnit.SECONDS); // instead of every 30 seconds, do it every 7 seconds
    }

    private static void write_avg() {
        try {
            // Read numbers from the file
            BufferedReader reader = new BufferedReader(new FileReader(Paths.get("data", "soundlevel.txt").toString()));
            String line;
            int sum = 0;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                int number = Integer.parseInt(line.trim());
                sum += number;
                count++;
            }
            reader.close();

            // Calculate the average
            double average = count > 0 ? (double) sum / count : 0;

            // Write the average to a new file
            FileWriter writer = new FileWriter(Paths.get("data", "noisepollution.txt").toString(), true); // true for append

            String level = "";

            if (average <= 60) {
                level = "low";
            } else if (average <= 80) {
                level = "medium";
            } else {
                level = "high";
            }

            writer.write(level + "\n");
            writer.close();

            System.out.println("Levelot e '"+level+"', vnesen e vo 'data/noisepollution.txt'");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
