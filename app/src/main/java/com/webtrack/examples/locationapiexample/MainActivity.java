package com.webtrack.examples.locationapiexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView latitudeText;
    TextView longitudeText;
    TextView tvBattery;
    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Double myLatitude;
    private Double myLongitude;

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeText = (TextView) findViewById(R.id.tvLatitude);
        longitudeText = (TextView) findViewById(R.id.tvLongitude);

        tvBattery = (TextView) findViewById(R.id.tvBattery);

        registerReceiver(batteryLevelReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_LOW));

        runnable = new Runnable() {
            @Override
            public void run() {

                int level = (int) batteryLevel();
                tvBattery.setText("Battery: " + level + "%");
                handler.postDelayed(runnable, 5000);

            }


        };

        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //handler = new Handler();
        //handler.postDelayed(runnable, 0);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    public float batteryLevel(){
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if(level == -1 || scale == -1){
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LocationTest","requestLocationUpdates() NO");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("LocationTest","FusedLocationAPI");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
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
    protected void onResume() {
        super.onResume();
        if(googleApiClient.isConnected()){
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationTest", "onLocationChanged");
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        latitudeText.setText("Latitude: " + String.valueOf(myLatitude));
        longitudeText .setText("Longitude: " + String.valueOf(myLongitude));
    }

    BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            //boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
            //        status == BatteryManager.BATTERY_STATUS_FULL;

            //int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            Toast.makeText(MainActivity.this, "Bateria baja", Toast.LENGTH_SHORT).show();
            //boolean usbCharge = chargePlug == BATTERY_PLUGGED_USB;
            //boolean acCharge = chargePlug == BATTERY_PLUGGED_AC;
        }
    };
}
