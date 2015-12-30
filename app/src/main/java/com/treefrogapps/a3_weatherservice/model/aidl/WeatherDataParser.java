package com.treefrogapps.a3_weatherservice.model.aidl;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with helper method to parse the InputStream
 */
public class WeatherDataParser {

    private Gson mGson = new Gson();

    private static String TAG = WeatherDataParser.class.getSimpleName();


    public List<WeatherCurrentData> parseJsonToGsonCurrentWeather(InputStream inputStream)
            throws IOException {

        ArrayList<WeatherCurrentData> result = new ArrayList<>();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

        /**
         * Pass input stream reader into Gson.fromJson (InputStreamReader is changed to a
         * JsonReader under the hood)
         */
        WeatherCurrentData weatherCurrentData
                = mGson.fromJson(inputStreamReader, WeatherCurrentData.class);

        Log.i(TAG, "Json data : " + mGson.toJson(inputStreamReader));

        // close the reader - this will also close the input stream associated with it
        inputStreamReader.close();

        result.add(0, weatherCurrentData);

        return result;
    }


    public List<WeatherForecastData> parseJsonToGsonForecastWeather(InputStream inputStream)
    throws IOException {

        ArrayList<WeatherForecastData> result = new ArrayList<>();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

        /**
         * Pass input stream reader into Gson.fromJson (InputStreamReader is changed to a
         * JsonReader under the hood)
         */
        WeatherForecastData weatherForecastData
                = mGson.fromJson(inputStreamReader, WeatherForecastData.class);

        Log.i(TAG, "Json data : " + mGson.toJson(inputStreamReader));

        // close the reader - this will also close the input stream associated with it
        inputStreamReader.close();

        result.add(0, weatherForecastData);

        return result;
    }
}
