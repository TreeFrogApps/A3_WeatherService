// WeatherTwoWay.aidl
package com.treefrogapps.a3_weatherservice.model.aidl;

// ******** IMPORTANT ********
// Declare any non-default types here with import statements

import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;

interface WeatherTwoWay {

/**
 *  Two way BLOCKING calls for weather data - should be performed off the main UI thread
 *
 */
    WeatherCurrentData getCurrentWeatherData (in String location);

    WeatherForecastData getForecastWeatherData (in String location);

}
