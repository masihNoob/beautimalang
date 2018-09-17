package com.example.xiinlaw.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

public class petatridi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petatridi);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.petatridi);

    }
    public void pindah2(View view) {



        Intent intent=new Intent(this,Slader3d.class);
        startActivity(intent);

    }
}
