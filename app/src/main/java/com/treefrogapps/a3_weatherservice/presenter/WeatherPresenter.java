package com.treefrogapps.a3_weatherservice.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.treefrogapps.a3_weatherservice.MVP;
import com.treefrogapps.a3_weatherservice.model.WeatherModel;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;
import com.treefrogapps.a3_weatherservice.view.DownloadDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Presenter layer that handles all requests from the view layer, as well as responding
 * back to the view layer.  This abstracts the view layer away from any hard links to the data/model
 */
public class WeatherPresenter implements MVP.WeatherPresenterInterface {

    public static final String TAG = WeatherPresenter.class.getSimpleName();

    private final String DIALOG_TAG = "dialog_tag";

    // boolean to hold whether a weather request is in progress
    public static volatile boolean RETRIEVING_DATA;

    private WeakReference<MVP.WeatherViewInterface> mViewInterface;

    private WeatherModel mWeatherModel;

    private DownloadDialog mDownloadDialog;

    private ArrayList<WeatherCurrentData> mCurrentWeatherList = new ArrayList<>();

    private ArrayList<WeatherForecastData> mForecastWeatherList = new ArrayList<>();

    @Override
    public void onCreate(MVP.WeatherViewInterface viewInterface) {

        Log.d(TAG, "onCreate Called");

        mViewInterface = new WeakReference<>(viewInterface);

        mViewInterface.get().getActivityContext();

        /**
         * Initialise the Model layer. Create a new instance and pass
         * in the presenter reference.
         */
        try {

            mWeatherModel = WeatherModel.class.newInstance();

            mWeatherModel.onCreate(this);

        } catch (InstantiationException e) {
            Log.e(TAG, "Instantiation Exception " + e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Illegal Access Exception  " + e);
        }
    }

    @Override
    public void onConfigChange(MVP.WeatherViewInterface viewInterface) {

        Log.d(TAG, "onConfigChangeCalled");

        mViewInterface = new WeakReference<>(viewInterface);

        /**
         * Update the Presenter Weak Reference in the Download dialog - if one exists
         * necessary at the presenter has a new reference to the view layer/interface
         * which has updated contexts
         */
        mDownloadDialog = (DownloadDialog) getFragManager().findFragmentByTag(DIALOG_TAG);

        if (mDownloadDialog != null) {
            mDownloadDialog.setPresenterInterface(this);
        }
    }


    /**
     * Method called from view layer to open new dialog window
     */
    @Override
    public void openDownloadDialog() {

        mDownloadDialog = DownloadDialog.newInstance(this);
        mDownloadDialog.show(mViewInterface.get().getFragManager(), DIALOG_TAG);
    }


    /**
     * Methods (4) for retrieving Weather Data from the Model layer
     * results are posted back to the Presenter Layer (displayResults)
     *
     * @param location location to get the weather for
     */
    @Override
    public void getWeatherCurrentSync(String location) {

        mWeatherModel.getWeatherCurrentSync(location);
    }

    @Override
    public void getWeatherForecastSync(String location) {

        mWeatherModel.getWeatherForecastSync(location);
    }

    @Override
    public void getWeatherCurrentASync(String location) {

        mWeatherModel.getWeatherCurrentASync(location);
    }

    @Override
    public void getWeatherForecastASync(String location) {

        mWeatherModel.getWeatherForecastASync(location);
    }


    /**
     * Method to display error or weather data results back to the View Layer
     * Must be run on the UI thread to handle results from ASYNC service
     *
     * @param weatherCurrentData weather data
     */
    @Override
    public void displayCurrentResults(final WeatherCurrentData weatherCurrentData, String message) {

        synchronized (this) {

            WeatherPresenter.RETRIEVING_DATA = false;

            Log.d(TAG, "Current Data Retrieved for : " + weatherCurrentData.getCity());

            mCurrentWeatherList.clear();
            mForecastWeatherList.clear();

            mCurrentWeatherList.add(weatherCurrentData);

            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mViewInterface.get().updateRecyclerView();
                }
            });
        }
    }

    /**
     * Method to display error or weather data results back to the View Layer
     * Must be run on the UI thread to handle results from ASYNC service
     *
     * @param weatherForecastData weather data
     */
    @Override
    public void displayForecastResults(final WeatherForecastData weatherForecastData, String message) {

        synchronized (this) {

            WeatherPresenter.RETRIEVING_DATA = false;

            Log.d(TAG, "Forecast Data Retrieved for : " + weatherForecastData.getCity().getCityName());

            mCurrentWeatherList.clear();
            mForecastWeatherList.clear();

            mForecastWeatherList.add(weatherForecastData);

            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mViewInterface.get().updateRecyclerView();
                }
            });
        }
    }

    /**
     * Get the Weather Current List - used in the recycler view
     *
     * @return ArrayList of current weather
     */
    @Override
    public ArrayList<WeatherCurrentData> getWeatherCurrentList() {
        return mCurrentWeatherList;
    }

    /**
     * Get the Weather Forecast list - used in the recyclerView
     *
     * @return ArrayList of forecast weather
     */
    @Override
    public ArrayList<WeatherForecastData> getWeatherForecastList() {
        return mForecastWeatherList;
    }


    /**
     * Methods for gaining contexts from the Weather Activity, which extends
     * Generic Activity which Implements this interface along with the Presenter interface
     * if Generic Activity doesn't implement the methods (declared abstract - so doesn't need to)
     * then any Sub Classes must implement them (non-abstract sub classes)
     *
     * @return a Context
     */
    @Override
    public Context getActivityContext() {
        return mViewInterface.get().getActivityContext();
    }

    @Override
    public Context getAppContext() {
        return mViewInterface.get().getAppContext();
    }

    @Override
    public FragmentManager getFragManager() {
        return mViewInterface.get().getFragManager();
    }

    @Override
    public void onDestroy() {

        // App is shutting down close down services connections
        mWeatherModel.onDestroy(false);

    }
}
