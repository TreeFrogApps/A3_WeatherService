package com.treefrogapps.a3_weatherservice.model;

import android.util.Log;

import com.treefrogapps.a3_weatherservice.MVP;

import java.lang.ref.WeakReference;

/**
 * Download Model layer - does all the interacting with data - holds a WeakReference to
 * the Presenter layer through the MVP Interface
 */
public class WeatherModel implements MVP.WeatherModelInterface {

    private static final String TAG = WeatherModel.class.getSimpleName();

    private WeakReference<MVP.WeatherPresenterInterface> mPresenterInterfaceWeakReference;


    @Override
    public void onCreate(MVP.WeatherPresenterInterface weatherPresenterInterface) {

        mPresenterInterfaceWeakReference =
                new WeakReference<>(weatherPresenterInterface);

        Log.d(TAG, "onCreate called");

    }

    @Override
    public void onDestroy() {

    }
}
