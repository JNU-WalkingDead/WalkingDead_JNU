package jejunu.hackathon.walkingdead.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import jejunu.hackathon.walkingdead.R;
import jejunu.hackathon.walkingdead.ResultDialog;
import jejunu.hackathon.walkingdead.model.Record;
import jejunu.hackathon.walkingdead.model.Zombie;
import jejunu.hackathon.walkingdead.util.RandomGenerator;
import jejunu.hackathon.walkingdead.util.TimeFormatter;


public class RunningActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "RunningActivity";

    private static final int CAMERA_TILT = 80;
    private static final int ZOMBIE_SPEED = 5;

    private GoogleMap mMap;
    private LatLng startLatLng, endLatLng;
    private List<Zombie> zombies;

    // 현재 위치를 받아옴
    private GoogleApiClient googleApiClient;
    private Location myLocation;

    // Timer
    private TextView timerTextView;
    private int currentTime;

    private Handler handler;
    private boolean isFinished = false;

    // DB
    private Realm realm;

    // 좀비 소리
    private MediaPlayer zombieSound;

    // 결과 다이얼로그
    private ResultDialog resultDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        timerTextView = (TextView) findViewById(R.id.timer_text_view);
        currentTime = 0;

        if (googleApiClient == null)
            googleApiClient = new GoogleApiClient.Builder(getBaseContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();


        Intent intent = getIntent();
        startLatLng = (LatLng) intent.getParcelableExtra("start");
        endLatLng = (LatLng) intent.getParcelableExtra("end");

        findViewById(R.id.current_location_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLocation == null)
                    return;
                Location endLocation = new Location("endLocation");
                endLocation.setLatitude(endLatLng.latitude);
                endLocation.setLongitude(endLatLng.longitude);
                float bearing = myLocation.bearingTo(endLocation);

                LatLng target = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(target)
                        .zoom(17)
                        .tilt(CAMERA_TILT)
                        .bearing(bearing)
                        .build();
                // duration 2000
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
            }
        });

        // 좀비 객체 10마리 생성
        zombies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            /// 좀비마커옵션 생성
            double randomLatitude = RandomGenerator.generate();
            randomLatitude = randomLatitude + startLatLng.latitude;
            double randomLongitude = RandomGenerator.generate();
            randomLongitude = randomLongitude + startLatLng.longitude;
            MarkerOptions zombieMarker = new MarkerOptions().position(new LatLng(randomLatitude, randomLongitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.zombie_icon));

            // 좀비 객체 생성
            Zombie zombie = new Zombie(zombieMarker);
            zombies.add(zombie);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        handler = new Handler();

        // DB 세팅
        realm = Realm.getDefaultInstance();

        // 소리 세팅
        zombieSound = MediaPlayer.create(this, R.raw.zombie_sound);
        zombieSound.setLooping(true);

    }

    public void setDefaultMarkers() {
        MarkerOptions startMarker = new MarkerOptions()
                .position(startLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_start));
        mMap.addMarker(startMarker);
        MarkerOptions endMarker = new MarkerOptions()
                .position(endLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_end));
        mMap.addMarker(endMarker);
    }

    // 지도가 준비되는 불리는 콜백메서드
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // 현재 위치를 가져올 수 있는지 체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(RunningActivity.this, "현재 위치 설정을 확인해주세요.", Toast.LENGTH_SHORT).show();
            Intent goSettings = new Intent(Settings.ACTION_SEARCH_SETTINGS);
            startActivity(goSettings);
            return;
        }

        LatLng myLocation = new LatLng(startLatLng.latitude, startLatLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
    }

    // 현재 위치를 가져오기 위한 connect
    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isFinished) {
                    handler.postDelayed(this, 100);
                    currentTime += 100;
                    timerTextView.setText(TimeFormatter.format(currentTime));
                }
            }
        });
    }

    // 현재 위치를 가져오기 위한 connect
    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        zombieSound.stop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected()");
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        // 현재위치가 준비되면 핸들러 post
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isFinished) {

                    handler.postDelayed(this, 500);
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        return;
                    myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                    // 500 밀리초마다 반복
                    mMap.clear();
                    setDefaultMarkers();

                    if (myLocation != null) {
                        // 좀비의 위치 변경
                        checkZombiesAreNear(zombies);
                        for (Zombie zombie : zombies) {
                            // 좀비의 속도 / 1000 만큼 쫓아옴
                            double newLatitude = zombie.getPosition().latitude +
                                    (myLocation.getLatitude() - zombie.getPosition().latitude) * ZOMBIE_SPEED / 1000;
                            double newLongitude = zombie.getPosition().longitude +
                                    (myLocation.getLongitude() - zombie.getPosition().longitude) * ZOMBIE_SPEED / 1000;

                            LatLng newPosition = new LatLng(newLatitude, newLongitude);
                            zombie.setPosition(newPosition);
                            mMap.addMarker(zombie.getZombieMarkerOptions());

                            checkGameStatus(zombie);
                        }
                    }
                }

            }
        });
    }

    private double distanceToZombie(Zombie zombie) {
        Location zombieLocation = new Location("zombieLocation");
        zombieLocation.setLatitude(zombie.getPosition().latitude);
        zombieLocation.setLongitude(zombie.getPosition().longitude);
        return myLocation.distanceTo(zombieLocation);
    }

    private double distanceToEndPoint() {
        Location endPointLocation = new Location("endPontLocation");
        endPointLocation.setLatitude(endLatLng.latitude);
        endPointLocation.setLongitude(endLatLng.longitude);
        return myLocation.distanceTo(endPointLocation);
    }

    private double distanceFromStartToEnd() {
        Location startLocation = new Location("startLocation");
        startLocation.setLatitude(startLatLng.latitude);
        startLocation.setLongitude(startLatLng.longitude);
        return myLocation.distanceTo(startLocation);
    }

    private void gameOver(String result) {
        realm.beginTransaction();
        Record record = new Record();
        record.setResult(result);
        record.setDate(new Date());
        record.setDistance(Math.round(distanceFromStartToEnd()));
        record.setTime(currentTime);
        realm.copyToRealm(record);
        realm.commitTransaction();
        isFinished = true;

        resultDialog = new ResultDialog(this, record);
        resultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        resultDialog.setCancelable(false);
        resultDialog.show();
    }

    private void checkZombiesAreNear(List<Zombie> zombies){
        boolean zombieIsNear = false;
        double nearestDistanceToZombie = 10000000;
        float volumeSize = 0;
        for (Zombie zombie : zombies) {
            if(distanceToZombie(zombie) < 200){
                if(nearestDistanceToZombie > distanceToZombie(zombie)){
                    nearestDistanceToZombie = distanceToZombie(zombie);
                }
                zombieIsNear = true;
            }
        }
        volumeSize = (float) (200 - nearestDistanceToZombie) / 200;
        if (zombieIsNear){
            if(!zombieSound.isPlaying()){
                zombieSound.start();
            }
            zombieSound.setVolume(volumeSize, volumeSize);
        }
        else{
            if(zombieSound.isPlaying()){
                zombieSound.stop();
            }
        }
    }

    private void checkGameStatus(Zombie zombie) {
        if (distanceToEndPoint() < 10) {
            gameOver("성공");
        } else if (distanceToZombie(zombie) < 10) {
            gameOver("실패");
            zombieSound.stop();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed()");
    }
}
