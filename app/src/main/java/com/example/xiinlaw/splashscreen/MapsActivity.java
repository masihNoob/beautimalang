package com.example.xiinlaw.splashscreen;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.example.xiinlaw.splashscreen.GPSLok.GPSLocation;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GPSLocation.LocationCallback {

    private GoogleMap mMap;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    Location lastlocation;
    Marker currentMarker;

    Polyline polyline;
    IGoogleAPIService iGoogleAPIService;
    private GPSLocation mGPSLocation;

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

        mGPSLocation = new GPSLocation(this, this);
        mGPSLocation.init();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                lastlocation = location;
                if(lastlocation!=null) {
                    drawPath(lastlocation, Common.getLatGreen() + "," + Common.getLngGreen());

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude()))
                            .title("your location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeperson));
                    currentMarker = mMap.addMarker(markerOptions);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                    //marker for destination
                    LatLng destinationLatLng = new LatLng(Double.parseDouble(Common.getLatGreen()),
                            Double.parseDouble(Common.getLngGreen()));
                    mMap.addMarker(new MarkerOptions()
                            .position(destinationLatLng)
                            .title(Common.getPlaceName())
                            .snippet("your destination")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
                }
            }
        });
    }
    private void drawPath(Location lastlocation, String destination) {
        if(polyline != null)  polyline.remove();

        String origin = new StringBuilder(String.valueOf(lastlocation.getLatitude())).append(",").append(String.valueOf(lastlocation.getLongitude())).toString();

        iGoogleAPIService.getDirections(origin, destination, "AIzaSyBFLxUSTw6x2q7EDnudqZlxP9Ih5acCBB0").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                new ParserTask().execute(response.body().toString());
                Log.d( "onResponse: ", response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("darpath error", "onFailure: "+ t.toString());
            }
        });
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        AlertDialog waitDialog = new SpotsDialog.Builder().setContext(MapsActivity.this).build();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(waitDialog != null && waitDialog.isShowing()) {
                waitDialog.show();
                waitDialog.setMessage("please wait, processing your request");
            }
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
                do {
                    routes = parser.parse(jsonObject);
                }while (routes ==null);
                Log.d( "doInBackground: ", routes.toString());
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

            for(int i = 0; i < lists.size();i++)
            {
                List<HashMap<String, String>> path = lists.get(i);

                for (int j = 0; j < path.size(); j++)
                {
                    HashMap<String, String> point = path.get(j);

                    if(j==0){ // Get distance from the list
                        Common.setDistance(point.get("distance"));
                        continue;
                    }else if(j==1){ // Get duration from the list
                        Common.setDuration(point.get("duration"));
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
            Log.d("mapInformation", "Distance:"+Common.getDistance() + ", Duration:"+Common.getDuration());
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
                intent=new Intent(this,petatridi.class);
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

    //GPS location callback
    @Override
    public void onLastKnowLocationFetch(Location location) {
        if(location != null) {
            Log.d("ali ", "onLastKnowLocationFetch " + location);
        }
    }

    @Override
    public void onLocationUpdate(Location location) {
        if(location != null) {
            Log.d("ali ", "onLocationUpdate " + location);
        }
    }

    @Override
    public void onLocationPermissionDenied() {
        Log.d( "onLocationPermissionDenied: ", "denied");
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
            mMap.setMyLocationEnabled(true);

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
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
