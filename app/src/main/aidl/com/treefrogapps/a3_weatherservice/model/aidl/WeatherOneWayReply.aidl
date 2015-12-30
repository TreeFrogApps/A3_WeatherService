// WeatherOneWayReply.aidl
package com.treefrogapps.a3_weatherservice.model.aidl;

// ******** IMPORTANT ********
// Declare any non-default types here with import statements

import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;

interface WeatherOneWayReply {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    oneway void sendCurrentResults(in WeatherCurrentData weatherCurrentData);

    oneway void sendForecastResults(in WeatherForecastData weatherForecastData);


    oneway void sendError(in String error);
}
