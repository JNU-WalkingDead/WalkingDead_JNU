package jejunu.hackathon.walkingdead;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jejunu.hackathon.walkingdead.model.Record;
import jejunu.hackathon.walkingdead.util.CalorieCalculator;
import jejunu.hackathon.walkingdead.util.TimeFormatter;

public class ResultDialog extends Dialog {

    private ImageView imageView;
    private TextView timeText, distanceText, calorieText, coinText;
    private Button toMainButton;

    private Activity activity;
    private Record record;


    public ResultDialog(Activity activity, Record record) {
        super(activity);
        this.activity = activity;
        this.record = record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_result);
        imageView = (ImageView) findViewById(R.id.imageView);
        timeText = (TextView) findViewById(R.id.time_text);
        distanceText = (TextView) findViewById(R.id.distance_text);
        calorieText = (TextView) findViewById(R.id.calorie_text);
        coinText = (TextView) findViewById(R.id.coin_text);
        toMainButton = (Button) findViewById(R.id.to_main_button);
        if (record.getResult().equals("실패")) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fail_2);
            imageView.setImageDrawable(drawable);
        }
        timeText.setText(TimeFormatter.format(record.getTime()));
        distanceText.setText(String.valueOf(record.getDistance()));
        calorieText.setText(String.valueOf(CalorieCalculator.defaultCalculate(record.getTime())));
        coinText.setText(String.valueOf(record.getDistance()));

        toMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultDialog.this.dismiss();
                activity.finish();
            }
        });
    }
}
