package com.treefrogapps.a3_weatherservice.utils;

import com.treefrogapps.a3_weatherservice.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Helper Methods associated with the Weather Data Classes
 */
public class WeatherUtils {


    public static String kelvinToDegrees(double kelvinValue) {

        return String.valueOf((int) Math.round(kelvinValue - 273.15)) + "\u2103";
    }

    public static String kelvinToFahrenheit(double kevinValue) {

        return String.format("%.1f", (kevinValue - 273.15) * 1.8 + 32) + "\u2109";
    }

    public static String getLastUpdateTime(long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        return sdf.format(calendar.getTime());
    }

    public static String getTimeOnly(long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        return sdf.format(calendar.getTime());
    }

    public static int setWeatherIcon(String icon) {

        switch (icon) {

            case "01d":
                return R.mipmap.ic_clear_sky;
            case "01n":
                return R.mipmap.ic_clear_sky_night;
            case "02d":
                return R.mipmap.ic_few_clouds;
            case "02n":
                return R.mipmap.ic_few_clouds_night;
            case "03d":
                return R.mipmap.ic_scattered_clouds;
            case "03n":
                return R.mipmap.ic_scattered_clouds;
            case "04d":
                return R.mipmap.ic_broken_clouds;
            case "04n":
                return R.mipmap.ic_broken_clouds;
            case "09d":
                return R.mipmap.ic_rain_light;
            case "09n":
                return R.mipmap.ic_rain_light_night;
            case "10d":
                return R.mipmap.ic_rain;
            case "10n":
                return R.mipmap.ic_rain;
            case "11d":
                return R.mipmap.ic_thunderstorm;
            case "11n":
                return R.mipmap.ic_thunderstorm;
            case "13d":
                return R.mipmap.ic_snow;
            case "13n":
                return R.mipmap.ic_snow;
            case "50d":
                return R.mipmap.ic_mist;
            case "50n":
                return R.mipmap.ic_mist;
        }

        return R.mipmap.ic_no_image;
    }
}
