package com.disbudpar.xiinlaw.splashscreen;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Sladerkajoe extends AppCompatActivity { //ubah
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sladerkajoe);//ubah

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPageAdapterKajoe viewPagerAdapter = new ViewPageAdapterKajoe(this);//ubah

        viewPager.setAdapter(viewPagerAdapter);

    }
}
