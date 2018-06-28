//Mapping Google Gelintunng

package com.example.xiinlaw.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GelintungMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelintung_map);
    }
    public void pindah(View view) {

        Intent intent=new Intent(GelintungMap.this,PetaGelintung.class);
        startActivity(intent);

    }
}
