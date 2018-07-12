package com.example.xiinlaw.splashscreen.Model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofit(String _baseUrl)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(_baseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
