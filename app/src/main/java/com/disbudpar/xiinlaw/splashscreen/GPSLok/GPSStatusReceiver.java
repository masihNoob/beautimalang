package com.disbudpar.xiinlaw.splashscreen.GPSLok;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class GPSStatusReceiver extends BroadcastReceiver {

    private GpsStatusChangeListener mCallback;
    private Context mContext;

    public GPSStatusReceiver(Context context, GpsStatusChangeListener callback) {
        mCallback = callback;
        mContext = context;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        context.registerReceiver(this, intentFilter);
    }

    public void unRegisterReceiver(){
        Log.d("ali", "unRegisterReceiver");
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            Log.d("ali", "in PROVIDERS_CHANGED");
            mCallback.onGpsStatusChange();
        }
    }

    public interface GpsStatusChangeListener{
        void onGpsStatusChange();
    }
}
