//Gambar PetaGelitnung

package com.example.xiinlaw.splashscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.gjiazhe.panoramaimageview.GyroscopeObserver;

public class PetaGelintung extends AppCompatActivity {
private GyroscopeObserver gyroscopeObserver;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_gelintung);
    }
    public void pindah(View view) {
        setContentView(R.layout.activity_buttgel0);
        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }


   public void pindah2(View view) {



            setContentView(R.layout.activity_buttgel1);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            getWindow().setLayout((int) (width * .8), (int) (height * .6));

    }
    public void cancell (View view)
    {
        Intent intent=new Intent(this,PetaGelintung.class);
        startActivity(intent);

    }




}
