package com.treefrogapps.a3_weatherservice.common;

import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper Class with static methods for placing objects in a Concurrent Hash Map
 */
public class WeatherDataCache {

    public static ConcurrentHashMap<String, WeatherCurrentData> weatherCurrentHashMap
            = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, WeatherForecastData> weatherForecastHashMap
            = new ConcurrentHashMap<>();

    private static final long TIMEOUT_CURRENT_WEATHER = 1000 * 30; // 30 seconds

    private static final long TIMEOUT_FORECAST_WEATHER = 1000 * 60 ; // 1 minute


    /**
     * Method to check the Concurrent HashMap (Thread Safe) cache if the location has already been
     * downloaded within the last five minutes, if so return that locations data.  If location already
     * exists but is past the timeout, then remove that key from the Hash Map (new up dated version
     * will be put in after download of updated data
     *
     * @param location String 'Key' in Concurrent HashMap
     * @return either null, or with current weather cache data item
     */
    public static WeatherCurrentData currentWeatherLookUp(String location) {

        if (weatherCurrentHashMap.containsKey(location)) {

            WeatherCurrentData weatherCurrentData
                    = weatherCurrentHashMap.get(location);

            long dataTime = weatherCurrentData.getTimeStamp();
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
     * @return either either null, or with current weather cache data item
     */
    public static WeatherForecastData forecastWeatherLookUp(String location) {

        if (weatherForecastHashMap.containsKey(location)) {

            WeatherForecastData weatherforecastData
                    = weatherForecastHashMap.get(location);

            long dataTime = weatherforecastData.getTimeStamp();
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
                                         WeatherCurrentData weatherCurrentData) {

        weatherCurrentHashMap.putIfAbsent(location, weatherCurrentData);
    }


    public static void putForeCastHashMap(String location,
                                          WeatherForecastData weatherForecastData) {

        weatherForecastHashMap.putIfAbsent(location, weatherForecastData);
    }

}
