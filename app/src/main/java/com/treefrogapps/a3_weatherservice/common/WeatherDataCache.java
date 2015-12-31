package com.treefrogapps.a3_weatherservice.common;

import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper Class with static methods for placing objects in a Concurrent Hash Map
 */
public class WeatherDataCache {

    public static ConcurrentHashMap<String, ArrayList<WeatherCurrentData>> weatherCurrentHashMap
            = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<WeatherForecastData>> weatherForecastHashMap
            = new ConcurrentHashMap<>();

    private static final long TIMEOUT_CURRENT_WEATHER = 1000 * 60 * 5; // 5 minutes

    private static final long TIMEOUT_FORECAST_WEATHER = 1000 * 60 * 60 * 3; // 3 hours


    /**
     * Method to check the Concurrent HashMap (Thread Safe) cache if the location has already been
     * downloaded within the last five minutes, if so return that locations data.  If location already
     * exists but is past the timeout, then remove that key from the Hash Map (new up dated version
     * will be put in after download of updated data
     *
     * @param location String 'Key' in Concurrent HashMap
     * @return either an empty ArrayList, or with 1 item in it.
     */
    public static ArrayList<WeatherCurrentData> currentWeatherLookUp(String location) {

        if (weatherCurrentHashMap.containsKey(location)) {

            ArrayList<WeatherCurrentData> weatherCurrentData
                    = weatherCurrentHashMap.get(location);

            long dataTime = weatherCurrentData.get(0).getTimeStamp();
            long currentTime = System.currentTimeMillis();

            if ((currentTime - dataTime) < TIMEOUT_CURRENT_WEATHER) {

                return weatherCurrentData;

            } else {

                weatherCurrentHashMap.remove(location);
            }
        }

        return null;
    }


    /**
     * Method to check the Concurrent HashMap (Thread Safe) cache if the location has already been
     * downloaded within the last five minutes, if so return that locations data.  If location already
     * exists but is past the timeout, then remove that key from the Hash Map (new up dated version
     * will be put in after download of updated data
     *
     * @param location String 'Key' in Concurrent HashMap
     * @return either an empty ArrayList, or with 1 item in it.
     */
    public static ArrayList<WeatherForecastData> forecastWeatherLookUp(String location) {

        if (weatherForecastHashMap.containsKey(location)) {

            ArrayList<WeatherForecastData> weatherforecastData
                    = weatherForecastHashMap.get(location);

            long dataTime = weatherforecastData.get(0).getTimeStamp();
            long currentTime = System.currentTimeMillis();

            if ((currentTime - dataTime) < TIMEOUT_FORECAST_WEATHER) {

                return weatherforecastData;

            } else {

                weatherCurrentHashMap.remove(location);
            }
        }

        return null;
    }


    public static void putCurrentHashMap(String location,
                                         ArrayList<WeatherCurrentData> weatherCurrentData) {

        weatherCurrentHashMap.put(location, weatherCurrentData);
    }


    public static void putForeCastHashMap(String location,
                                          ArrayList<WeatherForecastData> weatherForecastData) {

        weatherForecastHashMap.put(location, weatherForecastData);
    }

}
