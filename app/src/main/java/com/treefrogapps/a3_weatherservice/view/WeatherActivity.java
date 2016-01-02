package com.treefrogapps.a3_weatherservice.view;

import android.os.Bundle;
import android.util.Log;

import com.treefrogapps.a3_weatherservice.MVP;
import com.treefrogapps.a3_weatherservice.R;
import com.treefrogapps.a3_weatherservice.common.GenericActivity;
import com.treefrogapps.a3_weatherservice.presenter.WeatherPresenter;

public class WeatherActivity extends GenericActivity<MVP.WeatherViewInterface, WeatherPresenter>
        implements MVP.WeatherViewInterface {

    private static String TAG = WeatherActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Log.d(TAG, "onCreate called");

        /**
         * Call to the super class to initialise the Presenter,
         * WeatherActivity & Presenter Class passed in.
         */
        super.onCreate(this, WeatherPresenter.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // App being destroyed, notify other layer to perform
        // any necessary clean up operations (unbind service)
        if (isFinishing()) getPresenter().onDestroy();

    }


}
