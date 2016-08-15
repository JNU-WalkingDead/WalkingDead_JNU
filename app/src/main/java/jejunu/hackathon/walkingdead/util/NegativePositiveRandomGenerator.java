package jejunu.hackathon.walkingdead.util;

/**
 * Created by Kim on 2016-08-15.
 */
public class NegativePositiveRandomGenerator {

    public static double generate(){
        double result;
        int random = (int) (Math.random() * 2);
        if(random == 0){
            result = -1;
        }
        else{
            result = 1;
        }
        return result;
    }
}
