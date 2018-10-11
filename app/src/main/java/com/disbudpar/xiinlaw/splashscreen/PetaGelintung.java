//Gambar PetaGelitnung

package com.disbudpar.xiinlaw.splashscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;

public class PetaGelintung extends AppCompatActivity {
private GyroscopeObserver gyroscopeObserver;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_gelintung);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.petagelintung);
    }



   public void pindah2(View view) {



       Intent intent=new Intent(this,Slider.class);
       startActivity(intent);

    }






}
