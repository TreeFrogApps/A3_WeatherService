package com.treefrogapps.a3_weatherservice.model.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.treefrogapps.a3_weatherservice.R;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherCurrentData;
import com.treefrogapps.a3_weatherservice.model.aidl.WeatherForecastData;
import com.treefrogapps.a3_weatherservice.utils.WeatherUtils;

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


    public WeatherRecyclerAdapter(Context context, ArrayList<WeatherCurrentData> currentData,
                                  ArrayList<WeatherForecastData> forecastData) {

        this.mContext = context;
        this.mCurrentData = currentData;
        this.mforecastData = forecastData;
    }


    /**
     * Inner static class that overrides the Recycler.ViewHolder so a custom view holder can be create
     * this is the 'Template' layout that will 'Bind' to the data.
     */
    private static class CurrentViewHolder extends RecyclerView.ViewHolder {

        private TextView currentTextViewCityName, currentTextViewWeatherType, currentTextViewWeatherDesc,
                currentTextViewTempDeg, currentTextViewTempFah, currentTextViewCountry,
                currentTextViewPressure, currentTextViewHumidity, currentTextViewHWSpeed,
                currentTextViewTime, currentTextViewClouds, currentTextViewSunRise,
                currentTextViewSunset;

        private ImageView currentImageViewWeatherIcon;

        public CurrentViewHolder(View itemView) {
            super(itemView);

            currentTextViewCityName = (TextView) itemView.findViewById(R.id.currentTextViewCityName);
            currentTextViewWeatherType = (TextView) itemView.findViewById(R.id.currentTextViewWeatherType);
            currentTextViewWeatherDesc = (TextView) itemView.findViewById(R.id.currentTextViewWeatherDesc);
            currentTextViewTempDeg = (TextView) itemView.findViewById(R.id.currentTextViewTempDeg);
            currentTextViewTempFah = (TextView) itemView.findViewById(R.id.currentTextViewTempFah);
            currentTextViewCountry = (TextView) itemView.findViewById(R.id.currentTextViewCountry);
            currentTextViewPressure = (TextView) itemView.findViewById(R.id.currentTextViewPressure);
            currentTextViewHumidity = (TextView) itemView.findViewById(R.id.currentTextViewHumidity);
            currentTextViewHWSpeed = (TextView) itemView.findViewById(R.id.currentTextViewHWSpeed);
            currentTextViewTime = (TextView) itemView.findViewById(R.id.currentTextViewTime);
            currentTextViewClouds = (TextView) itemView.findViewById(R.id.currentTextViewClouds);
            currentTextViewSunRise = (TextView) itemView.findViewById(R.id.currentTextViewSunRise);
            currentTextViewSunset = (TextView) itemView.findViewById(R.id.currentTextViewSunset);

            currentImageViewWeatherIcon = (ImageView) itemView.findViewById(R.id.currentImageViewWeatherIcon);
        }
    }

    private static class ForecastViewHolder extends RecyclerView.ViewHolder {

        private TextView forecastTextViewCityName, forecastTextViewWeatherType,
                forecastTextViewWeatherDesc, forecastTextViewTempDeg,
                forecastTextViewTempFah, forecastTextView3hrTime;

        private ImageView forecastImageViewWeatherIcon;

        public ForecastViewHolder(View itemView) {
            super(itemView);

            forecastTextViewCityName = (TextView) itemView.findViewById(R.id.forecastTextViewCityName);
            forecastTextViewWeatherType = (TextView) itemView.findViewById(R.id.forecastTextViewWeatherType);
            forecastTextViewWeatherDesc = (TextView) itemView.findViewById(R.id.forecastTextViewWeatherDesc);
            forecastTextViewTempDeg = (TextView) itemView.findViewById(R.id.forecastTextViewTempDeg);
            forecastTextViewTempFah = (TextView) itemView.findViewById(R.id.forecastTextViewTempFah);
            forecastTextView3hrTime = (TextView) itemView.findViewById(R.id.forecastTextView3hrTime);

            forecastImageViewWeatherIcon = (ImageView) itemView.findViewById(R.id.forecastImageViewWeatherIcon);
        }
    }

    /**
     * Inflate Layouts - A view holder will be returned which holds the itemView (inflated layout)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /**
         * Inflate different layouts depending on view type
         */
        switch (viewType) {

            case CURRENT_VIEW_TYPE:
                return new CurrentViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_current, parent, false));

            case FORECAST_VIEW_TYPE:
                return new ForecastViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_forecast, parent, false));

        }

        return null;
    }

    /**
     * Bind data to the view types
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        /**
         * Bind data to the view type using the getItemViewType method to resolve the
         * view type for the recycler position
         */
        switch (getItemViewType(position)) {

            case CURRENT_VIEW_TYPE:

                // cast the appropriate View Holder for the layout type
                CurrentViewHolder holderCurrent = (CurrentViewHolder) holder;
                WeatherCurrentData currentData = mCurrentData.get(0);

                holderCurrent.currentTextViewCityName.setText(currentData.getCity());

                holderCurrent.currentTextViewWeatherType.setText(currentData.getWeatherList()
                        .get(0).getWeatherType());
                holderCurrent.currentTextViewWeatherDesc.setText(currentData.getWeatherList()
                        .get(0).getWeatherDescription());

                holderCurrent.currentTextViewTempDeg.setText(WeatherUtils
                        .kelvinToDegrees(currentData.getMain().getTemp()));

                holderCurrent.currentTextViewTempFah.setText(WeatherUtils
                        .kelvinToFahrenheit(currentData.getMain().getTemp()));

                holderCurrent.currentTextViewCountry.setText(currentData
                        .getCityInfo().getCountryCode());

                String pressure = String.valueOf(currentData.getMain().getPressure()) + " hPa";
                holderCurrent.currentTextViewPressure.setText(pressure);

                String humidity = currentData.getMain().getHumidity() + " %";
                holderCurrent.currentTextViewHumidity.setText(humidity);

                String wind = String.valueOf(currentData.getWind().getWindSpeed()) + " mph";
                holderCurrent.currentTextViewHWSpeed.setText(wind);

                holderCurrent.currentTextViewTime.setText(WeatherUtils
                        .getLastUpdateTime(currentData.getDateTime()));

                String clouds = currentData.getClouds().getCloudCover() + " %";
                holderCurrent.currentTextViewClouds.setText(clouds);

                holderCurrent.currentTextViewSunRise.setText(WeatherUtils
                        .getTimeOnly(currentData.getCityInfo().getSunriseTime()));

                holderCurrent.currentTextViewSunset.setText(WeatherUtils
                        .getTimeOnly(currentData.getCityInfo().getSunsetTime()));

                holderCurrent.currentImageViewWeatherIcon.setImageResource(WeatherUtils
                        .setWeatherIcon(currentData.getWeatherList().get(0).getIcon()));

                break;

            case FORECAST_VIEW_TYPE:

                ForecastViewHolder forecastHolder = (ForecastViewHolder) holder;
                // cast the appropriate View Holder for the layout type
                WeatherForecastData forecastData = mforecastData.get(0);

                forecastHolder.forecastTextViewCityName.setText(forecastData
                        .getCity().getCityName());

                forecastHolder.forecastTextViewWeatherType.setText(forecastData
                        .getWeatherLists()
                        .get(position)
                        .getCurrentWeather().get(0)
                        .getWeatherType());

                forecastHolder.forecastTextViewWeatherDesc.setText(forecastData
                        .getWeatherLists()
                        .get(position)
                        .getCurrentWeather().get(0)
                        .getWeatherDescription());

                forecastHolder.forecastTextViewTempDeg.setText(WeatherUtils
                        .kelvinToDegrees(forecastData
                                .getWeatherLists()
                                .get(position)
                                .getMainInfo().getTemp()));

                forecastHolder.forecastTextViewTempFah.setText(WeatherUtils
                        .kelvinToFahrenheit(forecastData
                                .getWeatherLists()
                                .get(position)
                                .getMainInfo().getTemp()));

                String forecastDateTime = forecastData
                        .getWeatherLists()
                        .get(position)
                        .getDateAsString();

                forecastDateTime = forecastDateTime
                        .substring(5, forecastDateTime.length() - 3) + " (UTC)";

                forecastHolder.forecastTextView3hrTime.setText(forecastDateTime);

                forecastHolder.forecastImageViewWeatherIcon.setImageResource(WeatherUtils
                        .setWeatherIcon(forecastData
                                .getWeatherLists().get(position)
                                .getCurrentWeather().get(0).getIcon()));
                break;
        }
    }


    /**
     * Return the view type based on the array list sizes
     */
    @Override
    public int getItemViewType(int position) {

        /**
         * Only ONE array list passed into the constructor SHOULD ever have a size of 1
         * or greater - based on this we know what view type to return
         */
        if (mCurrentData != null && mCurrentData.size() > 0) {
            return CURRENT_VIEW_TYPE;
        } else if (mforecastData != null && mforecastData.size() > 0) {
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

        if (mCurrentData != null && mCurrentData.size() > 0) {
            return mCurrentData.size();
        } else if (mforecastData != null && mforecastData.size() > 0) {
            return mforecastData.get(0).getWeatherLists().size();
        }
        return 0;
    }
}
