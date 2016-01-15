package com.treefrogapps.a3_weatherservice.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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
import com.treefrogapps.a3_weatherservice.presenter.WeatherPresenter;
import com.treefrogapps.a3_weatherservice.utils.utils;

import java.lang.ref.WeakReference;

/**
 * Download Model layer - does all the interacting with data - holds a WeakReference to
 * the Presenter layer through the MVP Interface
 */
public class WeatherModel implements MVP.WeatherModelInterface {

    private static final String TAG = WeatherModel.class.getSimpleName();

    public static int CURRENT_WEATHER = 10;
    public static int FORECAST_WEATHER = 20;

    private static String CACHED_DATA = "Cached Weather data retrieved";
    private static String NEW_DATA = "New Weather data downloaded";

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

    public void bindServices() {

        Log.d(TAG, "Attempting to Bind Services");

        mPresenterInterface.get().getAppContext()
                .bindService(WeatherServiceAsync.makeIntent(mPresenterInterface.get().getAppContext())
                        , mServiceConnectionASYNC, Context.BIND_AUTO_CREATE);

        mPresenterInterface.get().getAppContext()
                .bindService(WeatherServiceSync.makeIntent(mPresenterInterface.get().getActivityContext())
                        , mServiceConnectionSYNC, Context.BIND_AUTO_CREATE);
    }


    /**
     * Service Connections Called back after attempt to startServices() is made
     * to each Aidl service interface.
     * <p/>
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
     * Method that either returns cached data or uses the IBinder (two way) reference to download new
     * CURRENT weather data if either the cached time limit is up, or it hasn't been downloaded before
     *
     * @param location String - requested weather location
     */
    @Override
    public void getWeatherCurrentSync(String location) {

        // Check WeatherData Cache
        WeatherCurrentData weatherCurrentData = WeatherDataCache.currentWeatherLookUp(location);

        if (weatherCurrentData != null) {

            Log.d(TAG, "Current data for " + location +
                    " Timeout not expired, cached data retrieved");

            mPresenterInterface.get().displayCurrentResults(weatherCurrentData, CACHED_DATA);

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

                    if (weatherCurrentData != null) {

                        Log.d(TAG, "New Current data for "
                                + weatherCurrentData.getCity().toUpperCase() +
                                " added to Concurrent HashMap");

                        // put weather object into concurrent hash map
                        WeatherDataCache.putCurrentHashMap(weatherCurrentData.getCity(), weatherCurrentData);

                        mPresenterInterface.get().displayCurrentResults(weatherCurrentData, NEW_DATA);

                    } else {

                        WeatherPresenter.RETRIEVING_DATA = false;

                        utils.showToast(mPresenterInterface.get().getActivityContext(),
                                "No Weather data for " + location + " available");
                    }
                }
            }.execute(location);
        }
    }

    /**
     * SYNCHRONOUS Method that either returns cached data or uses the IBinder (two way) reference to download new
     * FORECAST weather data if either the cached time limit is up, or it hasn't been downloaded before
     *
     * @param location String - requested weather location
     */
    @Override
    public void getWeatherForecastSync(String location) {

        // Check WeatherData Cache
        WeatherForecastData weatherForecastData = WeatherDataCache.forecastWeatherLookUp(location);

        if (weatherForecastData != null) {

            Log.d(TAG, "Forecast data for " + location +
                    " Timeout not expired, cached data retrieved");

            mPresenterInterface.get().displayForecastResults(weatherForecastData, CACHED_DATA);

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

                    if (weatherForecastData != null) {

                        Log.d(TAG, "New Forecast data for "
                                + weatherForecastData.getCity().getCityName().toUpperCase() +
                                " added to Concurrent HashMap");

                        // put weather object into concurrent hash map
                        WeatherDataCache.putForeCastHashMap(weatherForecastData
                                .getCity().getCityName(), weatherForecastData);

                        mPresenterInterface.get().displayForecastResults(weatherForecastData, NEW_DATA);
                    } else {

                        WeatherPresenter.RETRIEVING_DATA = false;

                        utils.showToast(mPresenterInterface.get().getActivityContext(),
                                "No Weather data for " + location + " available");
                    }
                }
            }.execute(location);
        }
    }

    /**
     * ASYNCHRONOUS Method that either returns cached data or uses the IBinder (one way) reference to download new
     * FORECAST weather data if either the cached time limit is up, or it hasn't been downloaded before
     *
     * @param location String - requested weather location
     */
    @Override
    public void getWeatherCurrentASync(String location) {

        // Check WeatherData Cache
        WeatherCurrentData weatherCurrentData = WeatherDataCache.currentWeatherLookUp(location);

        if (weatherCurrentData != null) {

            Log.d(TAG, "Current data for " + location +
                    " Timeout not expired, cached data retrieved");

            mPresenterInterface.get().displayCurrentResults(weatherCurrentData, CACHED_DATA);

        } else {

            try {

                mWeatherOneWayRequestASYNC.getCurrentWeatherRequest(location,
                        mWeatherOneWayReplyCallBack);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ASYNCHRONOUS Method that either returns cached data or uses the IBinder (one way) reference to download new
     * FORECAST weather data if either the cached time limit is up, or it hasn't been downloaded before
     *
     * @param location String - requested weather location
     */
    @Override
    public void getWeatherForecastASync(String location) {

        // Check WeatherData Cache
        WeatherForecastData weatherForecastData = WeatherDataCache.forecastWeatherLookUp(location);

        if (weatherForecastData != null) {

            Log.d(TAG, "Forecast data for " + location +
                    " Timeout not expired, cached data retrieved");

            mPresenterInterface.get().displayForecastResults(weatherForecastData, CACHED_DATA);

        } else {

            try {

                mWeatherOneWayRequestASYNC.getForecastWeatherRequest(location,
                        mWeatherOneWayReplyCallBack);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Aidl one way Callback sent with one way request (observer pattern?)
     */
    private final WeatherOneWayReply.Stub mWeatherOneWayReplyCallBack = new WeatherOneWayReply.Stub() {
        @Override
        public void sendCurrentResults(WeatherCurrentData weatherCurrentData)
                throws RemoteException {

            Log.d(TAG, "New Current data for "
                    + weatherCurrentData.getCity().toUpperCase() +
                    " added to Concurrent HashMap");

            // put weather object into concurrent hash map
            WeatherDataCache.putCurrentHashMap(weatherCurrentData.getCity(), weatherCurrentData);

            // Send results back to the Presenter Layer
            mPresenterInterface.get().displayCurrentResults(weatherCurrentData, NEW_DATA);
        }

        @Override
        public void sendForecastResults(WeatherForecastData weatherForecastData)
                throws RemoteException {

            Log.d(TAG, "New Forecast data for "
                    + weatherForecastData.getCity().getCityName().toUpperCase() +
                    " added to Concurrent HashMap");

            // put weather object into concurrent hash map
            WeatherDataCache.putForeCastHashMap(weatherForecastData
                    .getCity().getCityName(), weatherForecastData);

            // Send results back the the Presenter Layer
            mPresenterInterface.get().displayForecastResults(weatherForecastData, NEW_DATA);
        }

        @Override
        public void sendError(String error) throws RemoteException {

            final String message = error;

            /**
             * Create new Handler to 'post' message to user - the handler has to given
             * the main looper (UI Looper) as this code block is running from a different thread
             */
            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {
                @Override
                public void run() {

                    WeatherPresenter.RETRIEVING_DATA = false;

                    utils.showToast(mPresenterInterface.get().getActivityContext(), message);
                }
            });

        }
    };

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */


    public void unBindServices() {

        Log.d(TAG, "Unbinding Services");

        if (mServiceConnectionASYNC != null) {
            mPresenterInterface.get().getAppContext().unbindService(mServiceConnectionASYNC);
        }

        if (mServiceConnectionSYNC != null) {
            mPresenterInterface.get().getAppContext().unbindService(mServiceConnectionSYNC);
        }
    }

    @Override
    public void onDestroy(boolean changingConfigurations) {

        if (!changingConfigurations) {
            unBindServices();

        } else {
            Log.d(TAG, "Changing Configurations");
        }
    }

}
