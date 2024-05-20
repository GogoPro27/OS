import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SoundLevelSensor {
    private static final int NUM_NUMBERS_TO_WRITE = 10;
    private static final Random RANDOM = new Random();
    private static int numbersWritten = 0;

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(SoundLevelSensor::writeRandomNumber, 0, 5, TimeUnit.SECONDS); //namesto 20 na sekoi 5 sekundi
    }
    private static void writeRandomNumber() {
        if (numbersWritten < NUM_NUMBERS_TO_WRITE+1) {
            try (FileWriter writer = new FileWriter(Paths.get("data", "soundlevel.txt").toString(), true)) {
                for (int i = 0; i < 10; i++) {
                    int randomNumber = RANDOM.nextInt(61) + 40;
                    writer.write(randomNumber + "\n");
                }
                System.out.println("Vneseni novi 5 statistiki");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

