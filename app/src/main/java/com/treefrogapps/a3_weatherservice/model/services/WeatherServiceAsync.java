package com.treefrogapps.a3_weatherservice.model.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.treefrogapps.a3_weatherservice.model.aidl.WeatherOneWayReply;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherOneWayRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Weather Service which is ASYNCHRONOUS - one way AIDL method call that doesn't return (void).
 * This will NOT block the calling thread.
 */
public class WeatherServiceAsync extends Service {

    private ExecutorService mExecutorService;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * Create Thread Pool, otherwise one way aidl calls are done on a single thread
         */
        mExecutorService = Executors.newCachedThreadPool();
    }

    /**
     * Static factory method for making an Explicit Intent - used when binding to the service
     * @param context supplied context
     * @return new explicit intent
     */
    public static Intent makeIntent(Context context){

        return new Intent(context, WeatherServiceAsync.class);
    }

    /**
     * Binder object sent back to the callback ServiceConnection.
     *
     * The WeatherOneWayRequest.Stub extends a Binder Class which implements IBinder
     * so the returned IBinder is cast from WeatherOneWayRequest.Stub
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mASyncOneWayServiceRequest;
    }

    /**
     * System generated Class : app/build/generated/source/aidl/debug/com/treefrogapps/a3_weatherservice/model/aidl
     *
     * static abstract inner class 'Stub' which has the defined methods in the corresponding aidl file
     * which MUST be overridden.  This runs in a separate thread
     */
    private final WeatherOneWayRequest.Stub mASyncOneWayServiceRequest = new WeatherOneWayRequest.Stub() {


        @Override
        public void getCurrentWeatherRequest(String location, WeatherOneWayReply resultsCallback)
                throws RemoteException {

            final Runnable currentWeatherRunnable = new Runnable() {
                @Override
                public void run() {

                    // TODO - add utils for downloading and returning a list to the calling object

                }
            };

            mExecutorService.execute(currentWeatherRunnable);
        }

        @Override
        public void getForecastWeatherRequest(String location, WeatherOneWayReply resultsCallback)
                throws RemoteException {

            final Runnable forecastWeatherRunnable = new Runnable() {
                @Override
                public void run() {

                    // TODO - add utils for downloading and returning a list to the calling object

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

    }

}
