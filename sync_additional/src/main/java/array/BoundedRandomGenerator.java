import java.util.Random;

public class BoundedRandomGenerator {
    static final Random random = new Random();
    static final int RANDOM_BOUND = 100;
    
    public int nextInt() {
        return random.nextInt(RANDOM_BOUND);
    }

}