// WeatherOneWayRequest.aidl
package com.treefrogapps.a3_weatherservice.model.aidl;

// ******** IMPORTANT ********
// Declare any non-default types here with import statements

import com.treefrogapps.a3_weatherservice.model.aidl.WeatherOneWayReply;

interface WeatherOneWayRequest {
    /**
     * Non-blocking ONE WAY methods for asynchronous weather results.
     *
     * Parameters: String for location for weather and the WeatherOneWayReply callback - this is required
     * so we have a reference where we want to send the results back to.
     */
    oneway void  getCurrentWeatherRequest (in String location, in WeatherOneWayReply resultsCallback);

    oneway void  getForecastWeatherRequest (in String location, in WeatherOneWayReply resultsCallback);

}
