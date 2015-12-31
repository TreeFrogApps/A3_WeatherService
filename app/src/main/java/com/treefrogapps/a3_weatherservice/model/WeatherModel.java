package com.treefrogapps.a3_weatherservice.model;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.treefrogapps.a3_weatherservice.MVP;
import com.treefrogapps.a3_weatherservice.common.WeatherDataCache;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherOneWayReply;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherOneWayRequest;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherTwoWay;
import com.treefrogapps.a3_weatherservice.model.services.WeatherServiceAsync;
import com.treefrogapps.a3_weatherservice.model.services.WeatherServiceSync;

import java.lang.ref.WeakReference;

/**
 * Download Model layer - does all the interacting with data - holds a WeakReference to
 * the Presenter layer through the MVP Interface
 */
public class WeatherModel implements MVP.WeatherModelInterface {

    private static final String TAG = WeatherModel.class.getSimpleName();

    public static int CURRENT_WEATHER = 10;
    public static int FORECAST_WEATHER = 20;

    private WeakReference<MVP.WeatherPresenterInterface> mPresenterInterface;

    /**
     * AIDL instances in services ASYNC and SYNC
     */
    private WeatherOneWayRequest mWeatherOneWayRequestASYNC;

    private WeatherTwoWay mWeatherTwoWaySYNC;


    @Override
    public void onCreate(MVP.WeatherPresenterInterface weatherPresenterInterface) {

        mPresenterInterface =
                new WeakReference<>(weatherPresenterInterface);

        Log.d(TAG, "onCreate called");

        /**
         * Attempt to bind services
         */
        bindServices();

    }

    public void bindServices(){

        Log.d(TAG, "Attempting to Bind Services");

        Intent intentAsync = WeatherServiceAsync.makeIntent(mPresenterInterface.get().getAppContext());
        mPresenterInterface.get().getAppContext().startService(intentAsync);

        Intent intentSync = WeatherServiceSync.makeIntent(mPresenterInterface.get().getActivityContext());
        mPresenterInterface.get().getAppContext().startService(intentSync);
    }


    /**
     * Service Connections Called back after attempt to startServices() is made
     * to each Aidl service interface.
     *
     * This could be done in a generic service connection class
     * to keep things tidy and more clean
     */

    private ServiceConnection mServiceConnectionASYNC = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d(TAG, "Connected to ASYNC service");
            /**
             * method called back when connected to the service
             * initialise the Aidl interface by getting instance of the interface
             */
            mWeatherOneWayRequestASYNC = WeatherOneWayRequest.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d(TAG, "Disconnected from ASYNC service");

            mWeatherOneWayRequestASYNC = null;
        }
    };

    private ServiceConnection mServiceConnectionSYNC = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d(TAG, "Connected to SYNC service");
            /**
             * method called back when connected to the service
             * initialise the Aidl interface by getting instance of the interface
             */
            mWeatherTwoWaySYNC = WeatherTwoWay.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d(TAG, "Disconnected from SYNC service");

            mWeatherTwoWaySYNC = null;
        }
    };


    /**
     * * * * * * * * * * * * * * * * * Methods for Getting Weather Data  * * * * * * * * * * * * * *
     */

    /**
     * Method that either returns cached data or uses the IBinder reference to download new
     * CURRENT weather data if either the cached time limit is up, or it hasn't been downloaded before
     * @param location String - requested weather location
     */
    @Override
    public void getWeatherCurrentSync(String location) {

        // Check WeatherData Cache
        WeatherCurrentData weatherCurrentData = WeatherDataCache.currentWeatherLookUp(location);

        if (weatherCurrentData != null){

            Log.d(TAG, "Current data for " + location +
                    "Timeout not expired, cached data retrieved");

            mPresenterInterface.get().displayCurrentResults(weatherCurrentData, null);

        } else {

            /**
             * Anonymous AsyncTask to use the IBinder Aidl reference to the WeatherServiceSync
             */
                new AsyncTask<String, Void, WeatherCurrentData>() {

                    private String location;
                    @Override
                    protected WeatherCurrentData doInBackground(String... params) {

                        location = params[0];
                        try {

                            /**
                             * Call to the synchronous service
                             */
                            return mWeatherTwoWaySYNC.getCurrentWeatherData(location);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(WeatherCurrentData weatherCurrentData) {
                        super.onPostExecute(weatherCurrentData);

                        if (weatherCurrentData !=null){

                            Log.d(TAG, "New Current data for " + location +
                                    "added to Concurrent HashMap");

                            mPresenterInterface.get().displayCurrentResults(weatherCurrentData, null);
                        }
                    }
                }.execute(location);
        }

    }

    /**
     * Method that either returns cached data or uses the IBinder reference to download new
     * FORECAST weather data if either the cached time limit is up, or it hasn't been downloaded before
     * @param location String - requested weather location
     */
    @Override
    public void getWeatherForecastSync(String location) {

        // Check WeatherData Cache
        WeatherForecastData weatherForecastData = WeatherDataCache.forecastWeatherLookUp(location);

        if (weatherForecastData != null){

            Log.d(TAG, "Forecast data for " + location +
                    "Timeout not expired, cached data retrieved");

            mPresenterInterface.get().displayForecastResults(weatherForecastData, null);

        } else {

            /**
             * Anonymous AsyncTask to use the IBinder Aidl reference to the WeatherServiceSync
             */
            new AsyncTask<String, Void, WeatherForecastData>() {

                private String location;
                @Override
                protected WeatherForecastData doInBackground(String... params) {

                    location = params[0];
                    try {

                        /**
                         * Call to the synchronous service
                         */
                        return mWeatherTwoWaySYNC.getForecastWeatherData(location);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(WeatherForecastData weatherForecastData) {
                    super.onPostExecute(weatherForecastData);

                    if (weatherForecastData !=null){

                        Log.d(TAG, "New Forecast data for " + location +
                                "added to Concurrent HashMap");

                        mPresenterInterface.get().displayForecastResults(weatherForecastData, null);
                    }
                }
            }.execute(location);
        }
    }

    @Override
    public void getWeatherCurrentASync(String location) {

    }

    @Override
    public void getWeatherForecastASync(String location) {

    }

    /**
     * Aidl one way Callback sent with one way request (observer pattern?)
     */

    private WeatherOneWayReply.Stub mWeatherOneWayReplyCallBack = new WeatherOneWayReply.Stub() {
        @Override
        public void sendCurrentResults(WeatherCurrentData weatherCurrentData)
                throws RemoteException {

        }

        @Override
        public void sendForecastResults(WeatherForecastData weatherForecastData)
                throws RemoteException {

        }

        @Override
        public void sendError(String error) throws RemoteException {

        }
    };

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    public void unBindServices(){

        Log.d(TAG, "Unbinding Services");

        mPresenterInterface.get().getAppContext().unbindService(mServiceConnectionASYNC);
        mPresenterInterface.get().getAppContext().unbindService(mServiceConnectionSYNC);
    }

    @Override
    public void onDestroy(boolean changingConfigurations) {

        if (!changingConfigurations){
            unBindServices();

        } else {
            Log.d(TAG, "Changing Configurations");
        }
    }

}
