package jejunu.hackathon.walkingdead.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;
import jejunu.hackathon.walkingdead.R;
import jejunu.hackathon.walkingdead.model.Record;
import jejunu.hackathon.walkingdead.util.DateFormatter;
import jejunu.hackathon.walkingdead.util.TimeFormatter;

/**
 * Created by Kim on 2016-08-15.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private RealmResults<Record> records;

    public RecordAdapter(RealmResults<Record> records) {
        this.records = records;
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_record, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.resultText.setText(records.get(position).getResult());
        holder.dateText.setText(DateFormatter.format(records.get(position).getDate()));
        holder.distanceText.setText(String.valueOf(records.get(position).getDistance()));
        holder.timeText.setText(TimeFormatter.format((int) (records.get(position).getTime())));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView resultText, dateText, distanceText, timeText, calrorieText;

        public ViewHolder(View v) {
            super(v);
            resultText = (TextView) v.findViewById(R.id.result_text);
            dateText = (TextView) v.findViewById(R.id.date_text);
            distanceText = (TextView) v.findViewById(R.id.distance_text);
            timeText = (TextView) v.findViewById(R.id.time_text);
            calrorieText = (TextView) v.findViewById(R.id.calorie_text);
        }
    }
}
