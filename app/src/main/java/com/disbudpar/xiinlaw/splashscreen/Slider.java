package com.disbudpar.xiinlaw.splashscreen;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;



public class Slider extends AppCompatActivity {


    //slider gelintung
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPageAdapter viewPagerAdapter = new ViewPageAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

    }

}
