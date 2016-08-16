package jejunu.hackathon.walkingdead.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import jejunu.hackathon.walkingdead.R;
import jejunu.hackathon.walkingdead.model.Record;
import jejunu.hackathon.walkingdead.util.TimeFormatter;

public class MypageActivity extends AppCompatActivity {

    private TextView distanceText, timeText, calorieText;
    private Button shopButton, characterButton, recordButton;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        distanceText = (TextView) findViewById(R.id.distance_text);
        timeText = (TextView) findViewById(R.id.time_text);
        calorieText = (TextView) findViewById(R.id.calorie_text);
        shopButton = (Button) findViewById(R.id.shop_button);
        characterButton = (Button) findViewById(R.id.character_button);
        recordButton = (Button) findViewById(R.id.record_button);

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MypageActivity.this, ShopActivity.class));
            }
        });

        characterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MypageActivity.this, CharacterActivity.class));
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MypageActivity.this, RecordActivity.class));
            }
        });


        realm = Realm.getDefaultInstance();
        RealmResults<Record> results = realm.where(Record.class).findAll();
        Long distanceSum = (Long) results.sum("distance");
        Long timeSum = (Long) results.sum("time");

        distanceText.setText(String.valueOf(distanceSum));
        System.out.println(timeSum);
        timeText.setText(TimeFormatter.totalFormat(timeSum));
    }
}
