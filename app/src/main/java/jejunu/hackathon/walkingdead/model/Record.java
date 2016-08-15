package jejunu.hackathon.walkingdead.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Kim on 2016-08-15.
 */
public class Record extends RealmObject {

    private Date date;
    private Long distance;
    private Long calorie;
    private Long time;
}
