package com.treefrogapps.a3_weatherservice.model.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;

import java.util.ArrayList;

/**
 * RecyclerView for both Data Types
 */
public class WeatherRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CURRENT_VIEW_TYPE = 1;
    private static final int FORECAST_VIEW_TYPE = 2;

    private Context mContext;
    private ArrayList<WeatherCurrentData> mCurrentData;
    private ArrayList<WeatherForecastData> mforecastData;


    public WeatherRecyclerAdapter (Context context, ArrayList<WeatherCurrentData> currentData,
                                   ArrayList<WeatherForecastData> forecastData) {

        this.mContext = context;
        this.mCurrentData = currentData;
        this.mforecastData = forecastData;
    }


    private static class CurrentViewHolder extends RecyclerView.ViewHolder {

        public CurrentViewHolder(View itemView){
            super(itemView);
        }
    }

    private static class ForecastViewHolder extends RecyclerView.ViewHolder {

        public ForecastViewHolder(View itemView){
            super(itemView);
        }
    }

    /**
     * Inflate Layouts
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * Bind data to the view types
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }



    /**
     * Return the view type based on the array list sizes
     */
    @Override
    public int getItemViewType(int position) {

        if (mCurrentData != null && mCurrentData.size() > 0){
            return CURRENT_VIEW_TYPE;
        } else if (mforecastData != null && mforecastData.size() > 0){
            return FORECAST_VIEW_TYPE;
        }
        return 0;
    }


    /**
     * Return the layout count based on the array list sizes
     * Forecast data will be based on the size of the inner static array list
     * for the weather lists ... should work .. I hope!
     */
    @Override
    public int getItemCount() {

        if (mCurrentData != null && mCurrentData.size() > 0){
            return mCurrentData.size();
        } else if (mforecastData != null && mforecastData.size() > 0){
            return mforecastData.get(0).getWeatherLists().size();
        }
        return 0;
    }
}
