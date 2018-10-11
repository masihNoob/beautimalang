package com.disbudpar.xiinlaw.splashscreen.GPSLok;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class GPSLocation implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GPSStatusReceiver.GpsStatusChangeListener {

    public static final int REQUEST_CHECK_SETTINGS = 100;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 200;

    private static final int PERMISSION_GRANTED = 0;
    private static final int PERMISSION_DENIED = 1;
    private static final int PERMISSION_BLOCKED = 2;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationCallback mCallback;
    private Activity mActivity;
    private Context mContext;
    private LocationRequest mLocationRequest;
    private GPSStatusReceiver mGPSStatusReceiver;

    private long intervalMillis = 10000;
    private long fastestIntervalMillis = 5000;
    private int accuracy = LocationRequest.PRIORITY_HIGH_ACCURACY;

    private boolean isInitialized = false;
    private boolean isLocationEnabled = false;
    private boolean isPermissionLocked = false;

    public GPSLocation(Activity activity, LocationCallback callback) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mCallback = callback;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
        mGPSStatusReceiver = new GPSStatusReceiver(mContext, this);
    }


    public void init(){
        isInitialized = true;
        if(mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                requestPermission();
            } else {
                connect();
            }
        }
    }


    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(intervalMillis);
        mLocationRequest.setFastestInterval(fastestIntervalMillis);
        mLocationRequest.setPriority(accuracy);
    }


    public LocationRequest getLocationRequest() {
        return mLocationRequest;
    }


    public void connect(){
        if(mGoogleApiClient != null && isInitialized) {
            mGoogleApiClient.connect();
        }
    }


    public void disconnect(){
        if(mGoogleApiClient != null && isInitialized) {
            mGoogleApiClient.disconnect();
        }
    }


    private void getLastKnownLocation(){
        if(!mGoogleApiClient.isConnected()){
            Log.d("ali", "getLastKnownLocation restart ");
            mGoogleApiClient.connect();
        }
        else {
            if (checkLocationPermission(mContext) && isLocationEnabled) {
                Log.d("ali", "getLastKnownLocation read ");
                if(mCurrentLocation == null) {
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    mCallback.onLastKnowLocationFetch(mCurrentLocation);
                }
                startLocationUpdates();
            }else{
                Log.d("ali", "getLastKnownLocation get permission ");
                requestPermission();
            }
        }
        Log.d("ali", "mCurrentLocation " + mCurrentLocation);
    }


    public void startLocationUpdates() {
        if(checkLocationPermission(mContext)
                && mGoogleApiClient != null
                && mGoogleApiClient.isConnected()
                && isLocationEnabled) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }


    public void stopLocationUpdates() {
        if(mGoogleApiClient != null
                && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("ali", "onConnected");
        requestPermission();
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d("ali", "onConnectionSuspended");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("ali", "onConnectionFailed");
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("ali", "onLocationChanged : " + location);
        mCallback.onLocationUpdate(location);
    }


    @Override
    public void onGpsStatusChange() {
        Log.d("ali", "onGpsStatusChange");
        if(isInitialized && !isPermissionLocked) {
            if (!isLocationEnabled(mContext)) {
                isLocationEnabled = false;
                isPermissionLocked = true;
                stopLocationUpdates();
                requestPermission();
            }
        }
    }


    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            String[] appPerm = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(mActivity, appPerm, LOCATION_PERMISSION_REQUEST_CODE);
        }else{
            getLocationSetting();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPSLocation.REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                getLastKnownLocation();
            }else{
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
                mCallback.onLocationSettingsError();
            }
        }
    }


    private void getLocationSetting(){
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest
                        .Builder()
                        .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>(){
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d("ali", "SUCCESS");
                        isLocationEnabled = true;
                        isPermissionLocked = false;
                        getLastKnownLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.d("ali", "RESOLUTION_REQUIRED");
                        try {
                            status.startResolutionForResult(
                                    mActivity,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            mCallback.onLocationSettingsError();
                        }finally {
                            isPermissionLocked = false;
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d("ali", "SETTINGS_CHANGE_UNAVAILABLE");
                        Toast.makeText(mContext, "Location Unavailable", Toast.LENGTH_SHORT).show();
                        mCallback.onLocationSettingsError();
                        isPermissionLocked = false;
                        break;
                }
            }
        });

    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permState;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        if(!ActivityCompat.shouldShowRequestPermissionRationale(
                                mActivity,
                                Manifest.permission.ACCESS_FINE_LOCATION)){
                            permState = PERMISSION_BLOCKED;
                        }else{permState = PERMISSION_DENIED;}
                    }else {permState = PERMISSION_GRANTED;}
                }
                else{permState = PERMISSION_DENIED;}

                switch (permState){
                    case PERMISSION_BLOCKED:
                        Toast.makeText(mContext,"Please give gps location permission to use the app.",Toast.LENGTH_LONG).show();
                        startInstalledAppDetailsActivity(mContext);
                        mCallback.onLocationPermissionDenied();
                        break;
                    case PERMISSION_DENIED:
                        Toast.makeText(mContext,"Permission Denied, app cannot access the gps location.", Toast.LENGTH_LONG).show();
                        break;
                    case PERMISSION_GRANTED:
                        getLocationSetting();
                        break;
                }
                break;
        }
    }

    public static boolean isLocationEnabled(Context context){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return gpsEnabled && networkEnabled;
    }

    public static void startInstalledAppDetailsActivity(final Context context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public static boolean checkLocationPermission(Context context) {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public interface LocationCallback {
        void onLastKnowLocationFetch(Location location);
        void onLocationUpdate(Location location);
        void onLocationPermissionDenied();
        void onLocationSettingsError();
    }


    public void close() {
        mGPSStatusReceiver.unRegisterReceiver();
    }
}