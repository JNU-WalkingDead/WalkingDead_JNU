package jejunu.hackathon.walkingdead.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Kim on 2016-08-15.
 */
public class TimeFormatter {

    private static DateFormat timeFormat = new SimpleDateFormat("mm:ss.SS");

    public static String format(int time) {
        return timeFormat.format(time).substring(0, 7);
    }

    public static String totalFormat(Long time) {
        String result = "";
        long second = time / 1000;
        long minute = time / 60000;
        long hour = time / 1440000;
        if (hour > 0) {
            result = result + hour + "시간 ";
        }
        if (minute > 0) {
            result = result + minute + "분 ";
        }
        if (second > 0) {
            result = result + second + "초";
        }
        return result;
    }
}
