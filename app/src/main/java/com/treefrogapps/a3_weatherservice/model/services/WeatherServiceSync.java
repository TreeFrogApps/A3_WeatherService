package com.treefrogapps.a3_weatherservice.model.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherTwoWay;

import java.util.List;

/**
 * Weather Service which is SYNCHRONOUS - AIDL method call that returns a result
 * this will block the calling thread so should be performed in a AsyncTask
 */
public class WeatherServiceSync extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
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
        public List<WeatherCurrentData> getCurrentWeatherData(String location)
                throws RemoteException {

            // TODO - download and parse json data
            return null;
        }

        @Override
        public List<WeatherForecastData> getForecastWeatherData(String location)
                throws RemoteException {

            // TODO - download and parse json data
            return null;
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
