package com.example.xiinlaw.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PetaKampungWarnaWarni extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_kampung_warna_warni);
    }

    public void kembali(View view)
    {
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);

    }
    public void buton1(View view)
    {
        Intent intent=new Intent(this,SladerWarna.class);
        startActivity(intent);

    }


}
