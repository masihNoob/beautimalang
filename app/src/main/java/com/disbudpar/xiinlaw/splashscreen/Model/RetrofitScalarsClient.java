package com.disbudpar.xiinlaw.splashscreen.Model;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitScalarsClient {
    private static Retrofit retrofit = null;
    public static Retrofit getScalarClient(String _baseUrl)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(_baseUrl).addConverterFactory(ScalarsConverterFactory.create()).build();

        }
        return retrofit;
    }
}
