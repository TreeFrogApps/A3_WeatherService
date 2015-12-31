package com.treefrogapps.a3_weatherservice.utils;

import com.treefrogapps.a3_weatherservice.model.WeatherModel;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherDataParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Net Utils class
 */
public class DownloadUtils {

    public static String WEATHER_URL_PART_A_CURRENT = "http://api.openweathermap.org/data/2.5/weather?q=";
    public  static String WEATHER_URL_PART_B_CURRENT = "&appid=d013bcc0366f96630c0558393113cd8b";

    public static String WEATHER_URL_PART_A_FORECAST = "http://api.openweathermap.org/data/2.5/forecast?q=";
    public  static String WEATHER_URL_PART_B_FORECAST = "&mode=json&appid=d013bcc0366f96630c0558393113cd8b";


    public static Object weatherDataDownload (String location, int weatherType){

        try {
            URL url = null;

            if (weatherType == WeatherModel.CURRENT_WEATHER) {
                url = new URL(WEATHER_URL_PART_A_CURRENT + location + WEATHER_URL_PART_B_CURRENT);
            } else {
                url = new URL(WEATHER_URL_PART_A_FORECAST + location + WEATHER_URL_PART_B_FORECAST);
            }

            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-length", "0");
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setConnectTimeout(10000); // 10 seconds
                connection.setReadTimeout(10000); // 10 seconds
                connection.connect();

                switch (connection.getResponseCode()) {

                    case 200 :
                    case 201 :
                    case 202 :
                        InputStream inputStream = connection.getInputStream();

                        if (weatherType == WeatherModel.CURRENT_WEATHER){
                            return WeatherDataParser.parseJsonToGsonCurrentWeather(inputStream);
                        } else {
                            return WeatherDataParser.parseJsonToGsonForecastWeather(inputStream);
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
