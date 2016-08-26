package jejunu.hackathon.walkingdead.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import jejunu.hackathon.walkingdead.R;


public class RunningModeSettingActivity extends AppCompatActivity implements GoogleMap.OnMarkerDragListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "RunningModeSetting";
    private SupportMapFragment mapFragment;
    private Toolbar toolbar;
    private GoogleMap mMap;

    // 현재 위치 값을 넘겨줌.
    private LatLng startLatLng;
    private LatLng endLatLng;
    private MarkerOptions endMarker;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_mode_setting);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("목적지 설정");
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient
                .Builder(getBaseContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (endLatLng != null && startLatLng != null) {
                    Intent intent = new Intent(RunningModeSettingActivity.this, RunningActivity.class);
                    intent.putExtra("start", startLatLng);
                    intent.putExtra("end", endLatLng);
                    startActivity(intent);
                    super.onBackPressed();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.499234, 126.530714), 13));
        mMap.setOnMapClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        mMap.setMyLocationEnabled(true);
        new MyLocationTask(this).execute();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        endLatLng = latLng;
        if (endLatLng != null) {
            endMarker = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_end))
                    .position(endLatLng).draggable(true);
            mMap.addMarker(endMarker);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.equals(endMarker)) {
            endLatLng = marker.getPosition();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    class MyLocationTask extends AsyncTask<Void, Void, LatLng> {

        ProgressDialog progressDialog;
        Context context;

        public MyLocationTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("현재 위치를 받아오고 있습니다.");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected LatLng doInBackground(Void... params) {
            Location currentLocation = null;
            if (ActivityCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "is not granted");

            } else {
                Log.d(TAG, "granted");

                while (startLatLng == null) {
                    currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    if (currentLocation != null){
                        startLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    }
                }

                Log.d(TAG, "startLatLng : " + startLatLng);

            }
            return startLatLng;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            Log.d(TAG, "result : " + latLng);
            if (latLng != null) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            } else {
                Toast.makeText(getBaseContext(), "권한을 승낙해야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // 연결 실패의 경우
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
