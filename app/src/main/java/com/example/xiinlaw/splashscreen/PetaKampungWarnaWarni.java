package com.example.xiinlaw.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;

public class PetaKampungWarnaWarni extends AppCompatActivity {
    private GyroscopeObserver gyroscopeObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_kampung_warna_warni);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.petawarna);
    }


    public void buton1(View view)
    {
        Intent intent=new Intent(this,SladerWarna.class);
        startActivity(intent);

    }


}
