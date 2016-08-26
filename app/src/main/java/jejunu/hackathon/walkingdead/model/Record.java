package jejunu.hackathon.walkingdead.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Kim on 2016-08-15.
 */
public class Record extends RealmObject {

    private String result;
    private Date date;
    private Long distance;
    private Long time;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
