//Dasbor

package com.example.xiinlaw.splashscreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {
    private static final int MY_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkLocationPermission();
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
    private boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, MY_PERMISSION_CODE);
            else
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, MY_PERMISSION_CODE);
            return false;
        }
        else
            return true;
    }
}
