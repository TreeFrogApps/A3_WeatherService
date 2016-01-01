package com.treefrogapps.a3_weatherservice.model.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.treefrogapps.a3_weatherservice.model.WeatherModel;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherTwoWay;
import com.treefrogapps.a3_weatherservice.utils.DownloadUtils;

import java.io.IOException;

/**
 * Weather Service which is SYNCHRONOUS - AIDL method call that returns a result
 * this will block the calling thread so should be performed in a AsyncTask
 */
public class WeatherServiceSync extends Service {

    private static  String TAG = WeatherServiceSync.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate Called");
    }

    /**
     * Static factory method for making an Explicit Intent - used when binding to the service
     * @param context supplied context
     * @return new explicit intent
     */
    public static Intent makeIntent (Context context){

        return new Intent(context, WeatherServiceSync.class);
    }

    /**
     * Binder object sent back to the callback ServiceConnection.
     *
     * The WeatherOneWayRequest.Stub extends a Binder Class which implements IBinder
     * so the returned IBinder is cast from WeatherOneWayRequest.Stub
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mSyncTwoWayServiceCall;
    }

    /**
     * System generated Class : app/build/generated/source/aidl/debug/com/treefrogapps/a3_weatherservice/model/aidl
     *
     * static abstract inner class 'Stub' which has the defined methods in the corresponding aidl file
     * which MUST be overridden.  This runs in a separate thread
     */
    private final WeatherTwoWay.Stub mSyncTwoWayServiceCall = new WeatherTwoWay.Stub() {
        @Override
        public WeatherCurrentData getCurrentWeatherData(String location)
                throws RemoteException {

            /**
             * Download data for location and cast object to correct weather data type
             */
            try {
                return (WeatherCurrentData)
                        DownloadUtils.weatherDataDownload(location, WeatherModel.CURRENT_WEATHER);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public WeatherForecastData getForecastWeatherData(String location)
                throws RemoteException {

            /**
             * Download data for location and cast object to correct weather data type
             */
            try {
                return (WeatherForecastData)
                        DownloadUtils.weatherDataDownload(location, WeatherModel.FORECAST_WEATHER);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Shutting down");
    }
}
