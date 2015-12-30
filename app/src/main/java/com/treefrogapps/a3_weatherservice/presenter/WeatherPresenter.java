package com.treefrogapps.a3_weatherservice.presenter;

import android.util.Log;

import com.treefrogapps.a3_weatherservice.MVP;
import com.treefrogapps.a3_weatherservice.model.WeatherModel;

import java.lang.ref.WeakReference;

/**
 * Presenter layer that handles all requests from the view layer, as well as responding
 * back to the view layer.  This abstracts the view layer away from any hard links to the data/model
 */
public class WeatherPresenter implements MVP.WeatherPresenterInterface {

    public static final String TAG = WeatherPresenter.class.getSimpleName();

    private WeakReference<MVP.WeatherViewInterface> mViewInterface;

    private WeatherModel mWeatherModel;


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

        mViewInterface = new WeakReference<>(viewInterface);
    }

    @Override
    public void onDestroy() {

    }
}
