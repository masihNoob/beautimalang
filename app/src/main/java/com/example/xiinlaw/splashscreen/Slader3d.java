package com.example.xiinlaw.splashscreen;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Slader3d extends AppCompatActivity {


    //slider 3d
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slader3d);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPageAdapter3d viewPagerAdapter = new ViewPageAdapter3d(this);

        viewPager.setAdapter(viewPagerAdapter);

    }

}
