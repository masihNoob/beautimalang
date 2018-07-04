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



   public void pindah2(View view) {



       Intent intent=new Intent(this,Slider.class);
       startActivity(intent);

    }
    public void cancell (View view)
    {
        Intent intent=new Intent(this,PetaGelintung.class);
        startActivity(intent);

    }
    public void kembali(View view)
    {
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);

    }





}
