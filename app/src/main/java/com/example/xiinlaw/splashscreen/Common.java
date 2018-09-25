package com.example.xiinlaw.splashscreen;

import com.example.xiinlaw.splashscreen.Model.IGoogleAPIService;
import com.example.xiinlaw.splashscreen.Model.RetrofitClient;
import com.example.xiinlaw.splashscreen.Model.RetrofitScalarsClient;

public class Common {
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static String getLatGreen() {
        return latGreen;
    }

    public static void setLatGreen(String latGreen) {
        Common.latGreen = latGreen;
    }

    public static String getLngGreen() {
        return lngGreen;
    }

    public static void setLngGreen(String lngGreen) {
        Common.lngGreen = lngGreen;
    }

    public static String getPlaceName() {
        return placeName;
    }

    public static void setPlaceName(String placeName) {
        Common.placeName = placeName;
    }

    public static String getDistance() {
        return distance;
    }

    public static void setDistance(String distance) {
        Common.distance = distance;
    }

    public static String getDuration() {
        return duration;
    }

    public static void setDuration(String duration) {
        Common.duration = duration;
    }

    private static String latGreen = "";
    private static String lngGreen = "";
    private static String placeName = "";
    private static String distance = "";
    private static String duration = "";

    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getRetrofit(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }

    public static IGoogleAPIService getGoogleAPIServiceScalars()
    {
        return RetrofitScalarsClient.getScalarClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
