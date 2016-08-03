package jejunu.hackathon.walkingdead;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class RunningActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "RunningActivity";

    private GoogleMap mMap;
    private LatLng startLatLng, endLatLng;
    private List<Zombie> zombies;

    private Handler handler;
    private MyThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        Intent intent = getIntent();
        startLatLng = (LatLng) intent.getParcelableExtra("start");
        endLatLng = (LatLng) intent.getParcelableExtra("end");

        zombies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Zombie zombie = new Zombie();
            double randomLatitude = (double) ((int) (Math.random() * 100) + 1) / 10000;
            randomLatitude = randomLatitude + startLatLng.latitude;
            double randomLongitude = (double) ((int) (Math.random() * 100) + 1) / 10000;
            randomLongitude = randomLongitude + startLatLng.longitude;
            LatLng position = new LatLng(randomLatitude, randomLongitude);
            zombie.setPosition(position);
            zombies.add(zombie);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        handler = new Handler();
        thread = new MyThread();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void setDefaultMarkers() {
        MarkerOptions startMarker = new MarkerOptions()
                .position(startLatLng).draggable(true);
        mMap.addMarker(startMarker);
        MarkerOptions endMarker = new MarkerOptions()
                .position(endLatLng).draggable(true);
        mMap.addMarker(endMarker);
    }

    // 지도가 준비되는 불리는 콜백메서드 인듯?
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
        mMap.setMyLocationEnabled(true);

        LatLng myLocation = new LatLng(33.499234, 126.530714);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

        // 출발지 목적지 마커를 설정
        setDefaultMarkers();

        // 좀비 객체들을 각각 마커로 찍어줌
        for (Zombie zombie : zombies) {
            MarkerOptions markerOptions = new MarkerOptions().position(zombie.getPosition())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.skull));
            Log.d(TAG, "" + zombie.getPosition());
            mMap.addMarker(markerOptions);
        }

        // 지도가 준비되면 쓰레드 시작
        thread.start();
    }

    class MyThread extends Thread {


        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    // 좀비는 5초마다 움직임.
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Thread InterruptedException" + e);
                }
                // 좀비의 움직임을 위해 UI 업데이트
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMap.clear();

                        setDefaultMarkers();

                        // 좀비를 새로 생성하고
                        List<Zombie> newZombies = new ArrayList<>();
                        for (Zombie zombie : zombies) {
                            Zombie newZombie = new Zombie();
                            // 좀비의 현재 위치와 시작점의 위치를 반으로 잘라서 쫓아옴
                            double newLatitude = (startLatLng.latitude + zombie.getPosition().latitude) / 2;
                            double newLongitude = (startLatLng.longitude + zombie.getPosition().longitude) / 2;
                            LatLng newPosition = new LatLng(newLatitude, newLongitude);
                            newZombie.setPosition(newPosition);
                            newZombies.add(newZombie);
                        }

                        // 좀비를 다시 없애는 과정
                        zombies.clear();
                        zombies.addAll(newZombies);

                        // 생성된 좀비를 마커로 찍는 과정
                        for (Zombie zombie : zombies) {
                            MarkerOptions markerOptions = new MarkerOptions().position(zombie.getPosition())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.skull));
                            Log.d(TAG, "" + zombie.getPosition());
                            mMap.addMarker(markerOptions);
                        }

                    }
                });
            }
        }
    }
}
