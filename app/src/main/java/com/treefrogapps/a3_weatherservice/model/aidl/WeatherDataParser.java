package com.treefrogapps.a3_weatherservice.model.aidl;

import android.util.Log;

import com.google.gson.Gson;
import com.treefrogapps.a3_weatherservice.common.WeatherDataCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class with helper method to parse the InputStream
 */
public class WeatherDataParser {


    private static String TAG = WeatherDataParser.class.getSimpleName();


    public static WeatherCurrentData parseJsonToGsonCurrentWeather(InputStream inputStream)
            throws IOException {

        ArrayList<WeatherCurrentData> result = new ArrayList<>();
        Gson gson = new Gson();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

        /**
         * Pass input stream reader into Gson.fromJson (InputStreamReader is changed to a
         * JsonReader under the hood)
         */
        WeatherCurrentData weatherCurrentData
                = gson.fromJson(inputStreamReader, WeatherCurrentData.class);

        Log.i(TAG, "Json data : " + gson.toJson(weatherCurrentData));

        // close the reader - this will also close the input stream associated with it
        inputStreamReader.close();

        // set a time stamp for the data
        weatherCurrentData.setTimeStamp(System.currentTimeMillis());

        // put weather object into concurrent hash map
        WeatherDataCache.putCurrentHashMap(weatherCurrentData.getCity(), weatherCurrentData);

        return weatherCurrentData;
    }


    public static WeatherForecastData parseJsonToGsonForecastWeather(InputStream inputStream)
            throws IOException {

        ArrayList<WeatherForecastData> result = new ArrayList<>();
        Gson gson = new Gson();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

        /**
         * Pass input stream reader into Gson.fromJson (InputStreamReader is changed to a
         * JsonReader under the hood)
         */
        WeatherForecastData weatherForecastData
                = gson.fromJson(inputStreamReader, WeatherForecastData.class);

        Log.i(TAG, "Json data : " + gson.toJson(weatherForecastData));

        // close the reader - this will also close the input stream associated with it
        inputStreamReader.close();

        // set a time stamp for the weather data
        weatherForecastData.setTimeStamp(System.currentTimeMillis());

        // put weather object into concurrent hash map
        WeatherDataCache.putForeCastHashMap(weatherForecastData
                .getCity().getCityName(), weatherForecastData);

        return weatherForecastData;
    }
}
