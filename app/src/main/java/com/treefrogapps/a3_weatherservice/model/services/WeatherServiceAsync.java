package com.treefrogapps.a3_weatherservice.model.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.treefrogapps.a3_weatherservice.common.WeatherDataCache;
import com.treefrogapps.a3_weatherservice.model.WeatherModel;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherOneWayReply;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherOneWayRequest;
import com.treefrogapps.a3_weatherservice.utils.DownloadUtils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Weather Service which is ASYNCHRONOUS - one way AIDL method call that doesn't return (void).
 * This will NOT block the calling thread.
 */
public class WeatherServiceAsync extends Service {

    private static String TAG = WeatherServiceAsync.class.getSimpleName();

    private ExecutorService mExecutorService;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate Called");

        /**
         * Create Thread Pool, otherwise one way aidl calls (Async) are done on a single thread
         */
        mExecutorService = Executors.newCachedThreadPool();
    }

    /**
     * Static factory method for making an Explicit Intent - used when binding to the service
     *
     * @param context supplied context
     * @return new explicit intent
     */
    public static Intent makeIntent(Context context) {

        return new Intent(context, WeatherServiceAsync.class);
    }

    /**
     * Binder object sent back to the callback ServiceConnection.
     * <p/>
     * The WeatherOneWayRequest.Stub extends a Binder Class which implements IBinder
     * so the returned IBinder is cast from WeatherOneWayRequest.Stub
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mASyncOneWayServiceRequest;
    }

    /**
     * System generated Class : app/build/generated/source/aidl/debug/com/treefrogapps/a3_weatherservice/model/aidl
     * <p/>
     * static abstract inner class 'Stub' which has the defined methods in the corresponding aidl file
     * which MUST be overridden.  This runs in a separate thread
     */
    private final WeatherOneWayRequest.Stub mASyncOneWayServiceRequest = new WeatherOneWayRequest.Stub() {

        @Override
        public void getCurrentWeatherRequest(String location, final WeatherOneWayReply resultsCallback)
                throws RemoteException {

            final String city = location;

            final Runnable currentWeatherRunnable = new Runnable() {
                @Override
                public void run() {

                    WeatherCurrentData weatherCurrentData = null;
                    String error = null;

                    try {
                        weatherCurrentData = (WeatherCurrentData)
                                DownloadUtils.weatherDataDownload(city, WeatherModel.CURRENT_WEATHER);
                    } catch (IOException e) {
                        error = e.getMessage();
                    }

                    /**
                     * Send either string with error in for debugging, or if successful send back
                     * weather data
                     */
                    try {

                        if (error != null) {

                            resultsCallback.sendError(error);

                        } else {

                            Log.d(TAG, "New Current data for " + city.toUpperCase() +
                                    " added to Concurrent HashMap");

                            WeatherDataCache.putCurrentHashMap(city, weatherCurrentData);

                            resultsCallback.sendCurrentResults(weatherCurrentData);

                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            };

            mExecutorService.execute(currentWeatherRunnable);
        }

        @Override
        public void getForecastWeatherRequest(String location, final WeatherOneWayReply resultsCallback)
                throws RemoteException {

            final String city = location;

            final Runnable forecastWeatherRunnable = new Runnable() {
                @Override
                public void run() {

                    WeatherForecastData weatherForecastData = null;
                    String error = null;

                    try {
                        weatherForecastData = (WeatherForecastData)
                                DownloadUtils.weatherDataDownload(city, WeatherModel.FORECAST_WEATHER);
                    } catch (IOException e) {
                        error = e.getMessage();
                    }

                    /**
                     * Send either string with error in for debugging, or if successful send back
                     * weather data
                     */
                    try {

                        if (error != null) {

                            resultsCallback.sendError(error);

                        } else {

                            Log.d(TAG, "New Forecast data for " + city.toUpperCase() +
                                    " added to Concurrent HashMap");

                            WeatherDataCache.putForeCastHashMap(city, weatherForecastData);

                            resultsCallback.sendForecastResults(weatherForecastData);

                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
            };

            mExecutorService.execute(forecastWeatherRunnable);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        // Shutdown the executor when the service is being destroyed
        mExecutorService.shutdownNow();

        Log.d(TAG, "Shutting down");
    }

}
