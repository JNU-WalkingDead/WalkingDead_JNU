package jejunu.hackathon.walkingdead;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;
import jejunu.hackathon.walkingdead.adapter.RecordAdapter;
import jejunu.hackathon.walkingdead.model.Record;

public class RecordActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        realm = Realm.getDefaultInstance();
        RealmResults<Record> records = realm.where(Record.class).findAll();
        adapter = new RecordAdapter(records);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
