package com.treefrogapps.a3_weatherservice.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.treefrogapps.a3_weatherservice.MVP;
import com.treefrogapps.a3_weatherservice.R;
import com.treefrogapps.a3_weatherservice.common.GenericActivity;
import com.treefrogapps.a3_weatherservice.model.recycler.WeatherRecyclerAdapter;
import com.treefrogapps.a3_weatherservice.presenter.WeatherPresenter;
import com.treefrogapps.a3_weatherservice.utils.utils;

public class WeatherActivity extends GenericActivity<MVP.WeatherViewInterface, WeatherPresenter>
        implements MVP.WeatherViewInterface, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private WeatherRecyclerAdapter mWeatherRecyclerAdapter;

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

        initialiseUI();
    }

    private void initialiseUI() {

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fabButton);
        mFab.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mWeatherRecyclerAdapter = new WeatherRecyclerAdapter(this,
                getPresenter().getWeatherCurrentList(), getPresenter().getWeatherForecastList());

        mRecyclerView.setAdapter(mWeatherRecyclerAdapter);

    }

    @Override
    public void updateRecyclerView() {

        mWeatherRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fabButton:

                if (!WeatherPresenter.RETRIEVING_DATA) {
                    openDialog();
                } else {
                    utils.showToast(this, "Currently Retrieving Weather Data");
                }

                break;
        }
    }

    private void openDialog() {

        getPresenter().openDownloadDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // App being changing configurations if isChangingConfigurations returns true.
        // Notify other layers if not true i.e. App is closing, to perform
        // any necessary clean up operations (unbind service)
        if (!isChangingConfigurations()) getPresenter().onDestroy();
    }
}
