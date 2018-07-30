

//splash screen
package com.example.xiinlaw.splashscreen;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;




public class MainActivity extends AppCompatActivity {
    private int waktu_loading=1000;

    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent Home=new Intent(MainActivity.this, Home.class);
                startActivity(Home);
                finish();

            }
        },waktu_loading);
    }
}
