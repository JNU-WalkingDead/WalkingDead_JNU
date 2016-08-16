package jejunu.hackathon.walkingdead;

import org.junit.Test;

import jejunu.hackathon.walkingdead.util.RandomGenerator;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void random_test() {
        double total = 0;
        for (int i = 1; i <= 10; i++) {
            double random = 0.0025 + (double) ((int) (Math.random() * 100) + 1) / 10000;
            System.out.println(random);
        }
    }

    @Test
    public void random_test2() {
        double total = 0;
        for (int i = 1; i <= 10; i++) {
            double random = RandomGenerator.generate();
            System.out.println(random);
        }
    }

}