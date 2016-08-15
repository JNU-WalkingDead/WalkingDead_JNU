package jejunu.hackathon.walkingdead.util;

/**
 * Created by Kim on 2016-08-15.
 */
public class CalorieCalculator {

    // 칼로리 계산법 : 0.0669 Kcal * 체중(kg) * 시간(분)

    public static int defaultCalculate(int time){
        return (int) 0.0669 * 60 * time;
    }

    public static int calculate(int weight, int time){
        return (int) 0.0669 * weight * time;
    }

}
