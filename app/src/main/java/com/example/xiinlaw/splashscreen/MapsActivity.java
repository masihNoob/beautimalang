package com.example.xiinlaw.splashscreen;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.xiinlaw.splashscreen.Model.IGoogleAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    Location lastlocation;
    Marker currentMarker;

    Polyline polyline;
    IGoogleAPIService iGoogleAPIService;

    //variable
    private static final int MY_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //init service
        iGoogleAPIService = Common.getGoogleAPIServiceScalars();

        //request permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkLocationPermission();

        buildLocationCallBack();
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        buildLocationCallBack();
                        buildLocationRequest();

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            }
            break;
        }
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

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                /*lastlocation = locationResult.getLastLocation();

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude()))
                        .title("your location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                currentMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                //marker for destination
                LatLng destinationLatLng = new LatLng(Double.parseDouble(Common.getLatGreen()),
                        Double.parseDouble(Common.getLngGreen()));
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(destinationLatLng)
                        .title(Common.getPlaceName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                drawPath(lastlocation, Common.getLatGreen()+","+Common.getLngGreen());*/
            }
        };
    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                lastlocation = location;

                if(lastlocation!=null) {
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude()))
                            .title("your location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    currentMarker = mMap.addMarker(markerOptions);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                    //marker for destination
                    LatLng destinationLatLng = new LatLng(Double.parseDouble(Common.getLatGreen()),
                            Double.parseDouble(Common.getLngGreen()));
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .position(destinationLatLng)
                            .title(Common.getPlaceName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    drawPath(lastlocation, Common.getLatGreen() + "," + Common.getLngGreen());
                }
            }
        });
    }
    private void drawPath(Location lastlocation, String destination) {
        if(polyline != null) polyline.remove();

        String origin = new StringBuilder(String.valueOf(lastlocation.getLatitude())).append(",").append(String.valueOf(lastlocation.getLongitude())).toString();


        iGoogleAPIService.getDirections(origin, destination).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                new ParserTask().execute(response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        AlertDialog waitDialog = new SpotsDialog.Builder().setContext(MapsActivity.this).build();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waitDialog.show();
            waitDialog.setMessage("please wait, processing your request");

        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(jsonObject);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);

            ArrayList points = new ArrayList();
            PolylineOptions polylineOptions = new PolylineOptions();

            String distance = "";
            String duration = "";

            for(int i = 0; i < lists.size();i++)
            {

                List<HashMap<String, String>> path = lists.get(i);

                for (int j = 0; j < path.size(); j++)
                {
                    HashMap<String, String> point = path.get(j);

                    if(j==0){ // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    Double lat = Double.parseDouble(point.get("lat"));
                    Double lng = Double.parseDouble(point.get("lng"));

                    LatLng positions = new LatLng(lat, lng);

                    points.add(positions);
                }
                polylineOptions.addAll(points);
                polylineOptions.width(10);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);

            }

            polyline = mMap.addPolyline(polylineOptions);
            waitDialog.dismiss();
        }
    }
    //button infolokasi
    public void infoLokasi(View view) {
        Intent intent;
        switch (Common.getPlaceName()){
            case "Go Green Glintung":{
                intent = new Intent(this,PetaGelintung.class);
                startActivity(intent);
            }
            break;
            case "Kampung Tridi":{
                intent=new Intent(this,Slader3d.class);
                startActivity(intent);
            }
            break;
            case "kajoetangan heritage":{
                intent=new Intent(this,PetaKayuTangan.class);
                startActivity(intent);
            }
            break;
            case "warna warni jodipan":{
                intent=new Intent(this,PetaKampungWarnaWarni.class);
                startActivity(intent);
            }
            break;
        }
    }
}
