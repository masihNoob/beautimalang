package com.example.xiinlaw.splashscreen;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SladerWarna extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slader_warna);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPageAdapterWarna viewPagerAdapter = new ViewPageAdapterWarna(this);

        viewPager.setAdapter(viewPagerAdapter);

    }
}
