package jejunu.hackathon.walkingdead.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import jejunu.hackathon.walkingdead.R;

public class ModeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ImageView walkingButton, runningButton;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        googleApiClient = new GoogleApiClient
                .Builder(getBaseContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        walkingButton = (ImageView) findViewById(R.id.walking_button);
        runningButton = (ImageView) findViewById(R.id.running_button);

        walkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ModeActivity.this, WalkingActivity.class));
            }
        });

        runningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetworkAndGPS()) {
                    finish();
                    startActivity(new Intent(ModeActivity.this, RunningModeSettingActivity.class));
                }
            }
        });
    }

    private boolean checkNetworkAndGPS() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null) {
            Toast.makeText(this, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, "GPS 상태를 확인해해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
//        if (LocationServices.FusedLocationApi.getLastLocation(googleApiClient) == null) {
//            Toast.makeText(this, "현재위치를 받아오고 있습니다. \n잠시후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
