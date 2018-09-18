//Dasbor

package com.example.xiinlaw.splashscreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.xiinlaw.splashscreen.GPSLok.GPSLocation;

public class Home extends AppCompatActivity implements GPSLocation.LocationCallback{
    private static final int MY_PERMISSION_CODE = 1000;
    private GPSLocation mGPSLocation;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGPSLocation = new GPSLocation(this, this);

        tv= findViewById(R.id.runningText);
        tv.setSelected(true);

        mGPSLocation.init();

    }
    public void NextMethod(View view) {
        switch (view.getId()){
            case R.id.buttonGelintung:{
                Common.setLatGreen("-7.946027");
                Common.setLngGreen("112.638604");
                Common.setPlaceName("Go Green Glintung");
            }
            break;
            case R.id.buttonTridi:{
                Common.setLatGreen("-7.981849");
                Common.setLngGreen("112.6359163");
                Common.setPlaceName("Kampung Tridi");
            }
            break;
            case R.id.buttonKayutangan:{
                Common.setLatGreen("-7.9814255");
                Common.setLngGreen("112.628141");
                Common.setPlaceName("kajoetangan heritage");
            }
            break;case R.id.buttonWarna:{
                Common.setLatGreen("-7.9832215");
                Common.setLngGreen("112.6354428");
                Common.setPlaceName("warna warni jodipan");
            }
            break;
        }
        startActivity(new Intent(this, MapsActivity.class));
    }
    @Override
    public void onLastKnowLocationFetch(Location location) {

    }

    @Override
    public void onLocationUpdate(Location location) {

    }

    @Override
    public void onLocationPermissionDenied() {

    }

    @Override
    public void onLocationSettingsError() {

    }

    @Override
    protected void onStart() {
        mGPSLocation.connect();
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        mGPSLocation.startLocationUpdates();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mGPSLocation.stopLocationUpdates();
    }


    @Override
    protected void onStop() {
        mGPSLocation.disconnect();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        mGPSLocation.close();
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GPSLocation.LOCATION_PERMISSION_REQUEST_CODE) {
            mGPSLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPSLocation.REQUEST_CHECK_SETTINGS) {
            mGPSLocation.onActivityResult(requestCode, resultCode, data);
        }
    }
}
