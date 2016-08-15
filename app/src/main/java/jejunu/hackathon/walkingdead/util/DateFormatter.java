package jejunu.hackathon.walkingdead.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kim on 2016-08-15.
 */
public class DateFormatter {

    private static DateFormat dayFormatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    public static String format(Date date){
        return dayFormatter.format(date);
    }
}
