package jejunu.hackathon.walkingdead;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class WinLoseDialogActivity extends AppCompatActivity {

    private TextView resultTextView, timeCheckTextView, walkingDistanceTextView;

    public static final String EXTRA_RESULT_TITLE = "result_title";
    public static final String EXTRA_TIME_CHECK = "time_check";
    public static final String EXTRA_REAL_TIME = "real_time";
    public static final String EXTRA_WALKING_DISTANCE = "walking_distance";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_lose_dialog);

        // View init
        resultTextView = (TextView) findViewById(R.id.win_lose_title_textview);
        timeCheckTextView = (TextView) findViewById(R.id.time_check);
        walkingDistanceTextView = (TextView) findViewById(R.id.walking_distance);

        // 받아온 값들로 TextView 갑 설정
        Intent getIntent = getIntent();
        resultTextView.setText(getIntent.getStringExtra(EXTRA_RESULT_TITLE));
        timeCheckTextView.setText(getIntent.getStringExtra(EXTRA_TIME_CHECK));
        walkingDistanceTextView.setText(getIntent.getStringExtra(EXTRA_WALKING_DISTANCE)+"m");

        // 메인 화면으로 이동
        findViewById(R.id.to_main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinLoseDialogActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
