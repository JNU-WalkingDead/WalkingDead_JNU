package jejunu.hackathon.walkingdead.util;

/**
 * Created by Kim on 2016-08-15.
 */
public class RandomGenerator {

    public static double generate(){
        double result;
        int random = (int) (Math.random() * 2);
        if(random == 0){
            result = -1;
        }
        else{
            result = 1;
        }
        result = result * ((int) (Math.random() * 100) + 1 + 0.01) / 10000;
        return result;
    }
}
