package com.example.xiinlaw.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xiinlaw.splashscreen.buttonmapgelintung.ButtGel0;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;

public class PetaGelintung extends AppCompatActivity {
private GyroscopeObserver gyroscopeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_gelintung);


    }
    public void pindah(View view) {

        Intent intent=new Intent(PetaGelintung.this,ButtGel0.class);
        startActivity(intent);

    }


}
