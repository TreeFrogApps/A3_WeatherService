package com.treefrogapps.a3_weatherservice.model.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Weather Data class which will be used to hold the information returned from the Web service
 * returned json data. Extends the Parcelable interface so we can 'Parcel' across from service
 * to App (using aidl it abstracts this away, however this is required as under hood it still
 * crosses processes which can serialise/de-serialise parcelable data).
 * <p/>
 * Uses Gson @SerializedName annotations to serialise / de-serialise json data into
 * Java primitive types and objects.
 * <p/>
 * json link & typical data :
 * <p/>
 * http://api.openweathermap.org/data/2.5/forecast?q=London,uk&mode=json&appid=d013bcc0366f96630c0558393113cd8b
 * <p/>
 * {"city":{"id":2643743,"name":"London","coord":{"lon":-0.12574,"lat":51.50853},"country":"GB"},
 * "cod":"200","list":[{"dt":1451347200,"main":{"temp":284.66,"temp_min":284.011,"temp_max":284.66,
 * "pressure":1014.84,"sea_level":1024.75,"grnd_level":1014.84,"humidity":79},"weather":[{"id":500,
 * "main":"Rain","description":"light rain","icon":"10n"}],"clouds":{"all":64},"wind":{"speed":9.44,
 * "deg":176.007},"rain":{"3h":0.105},"dt_txt":"2015-12-29 00:00:00"}, -- and list array continues
 * <p/>
 * item definitions http://openweathermap.org/weather-data
 */
public class WeatherForecastData implements Parcelable {

    private static String TAG = WeatherForecastData.class.getSimpleName();

    @SerializedName("city")
    private City mCity;

    @SerializedName("cod")
    private String mCode;

    @SerializedName("list")
    private ArrayList<WeatherList> mWeatherLists = new ArrayList<>();

    // used in Parcelable constructor
    private ArrayList<CurrentWeather> mCurrentWeather = new ArrayList<>();

    /**
     * Getter methods for top level objects/primitives
     */
    public City getCity() {
        return mCity;
    }

    public String getCode() {
        return mCode;
    }

    public ArrayList<WeatherList> getWeatherLists() {
        return mWeatherLists;
    }


    public static class City {

        // Constructor required for writing in from parcel
        public City(int id, String name, Coordinates coords, String country) {

            this.mCityId = id;
            this.mCityName = name;
            this.mCoordinates = coords;
            this.mCountry = country;
        }

        @SerializedName("id")
        private int mCityId;

        @SerializedName("name")
        private String mCityName;

        @SerializedName("coord")
        private Coordinates mCoordinates;

        @SerializedName("country")
        private String mCountry;


        public int getCityId() {
            return mCityId;
        }

        public String getCityName() {
            return mCityName;
        }

        public Coordinates getCoordinates() {
            return mCoordinates;
        }

        public String getCountry() {
            return mCountry;
        }
    }

    public static class Coordinates {

        // Constructor required for writing in from parcel
        public Coordinates(double longitude, double latitude) {

            this.mLongitude = longitude;
            this.mLatitude = latitude;
        }

        @SerializedName("lon")
        private double mLongitude;

        @SerializedName("lat")
        private double mLatitude;


        public double getLongitude() {
            return mLongitude;
        }

        public double getLatitude() {
            return mLatitude;
        }
    }


    public static class WeatherList {

        // Constructor required for writing in from parcel
        public WeatherList(long dateTime, MainInfo info, ArrayList<CurrentWeather> currentWeather,
                           Clouds clouds, Wind wind, Rain rain, Snow snow, String date) {

            this.mDateTime = dateTime;
            this.mMainInfo = info;
            this.mCurrentWeather = currentWeather;
            this.mClouds = clouds;
            this.mWind = wind;
            this.mRain = rain;
            this.mSnow = snow;
            this.mDateAsString = date;
        }

        @SerializedName("dt")
        private long mDateTime;

        @SerializedName("main")
        private MainInfo mMainInfo;

        @SerializedName("weather")
        ArrayList<CurrentWeather> mCurrentWeather;

        @SerializedName("clouds")
        private Clouds mClouds;

        @SerializedName("wind")
        private Wind mWind;

        @SerializedName("rain")
        private Rain mRain;

        @SerializedName("snow")
        private Snow mSnow;

        @SerializedName("dt_txt")
        private String mDateAsString;


        public long getDateTime() {
            return mDateTime;
        }

        public MainInfo getMainInfo() {
            return mMainInfo;
        }

        public ArrayList<CurrentWeather> getCurrentWeather() {
            return mCurrentWeather;
        }

        public Clouds getClouds() {
            return mClouds;
        }

        public Wind getWind() {
            return mWind;
        }

        public Rain getRain() {
            return mRain;
        }

        public Snow getSnow() {
            return mSnow;
        }

        public String getDateAsString() {
            return mDateAsString;
        }
    }

    public static class MainInfo {

        // Constructor required for writing in from parcel
        public MainInfo(double temp, double tempMin, double tempMax, double pressure,
                        double seaLevel, double groundLevel, int humidity) {

            this.mTemp = temp;
            this.mTempMin = tempMin;
            this.mTempMax = tempMax;
            this.mPressure = pressure;
            this.mSeaLevel = seaLevel;
            this.mGroundLevel = groundLevel;
            this.mHumidity = humidity;
        }

        @SerializedName("temp")
        private double mTemp;

        @SerializedName("temp_min")
        private double mTempMin;

        @SerializedName("temp_max")
        private double mTempMax;

        @SerializedName("pressure")
        private double mPressure;

        @SerializedName("sea_level")
        private double mSeaLevel;

        @SerializedName("grnd_level")
        private double mGroundLevel;

        @SerializedName("humidity")
        private int mHumidity;


        public double getTemp() {
            return mTemp;
        }

        public double getTempMin() {
            return mTempMin;
        }

        public double getTempMax() {
            return mTempMax;
        }

        public double getPressure() {
            return mPressure;
        }

        public double getSeaLevel() {
            return mSeaLevel;
        }

        public double getGroundLevel() {
            return mGroundLevel;
        }

        public int getHumidity() {
            return mHumidity;
        }
    }


    public static class CurrentWeather {

        // Constructor required for writing in from parcel
        public CurrentWeather(int code, String type, String description, String icon) {

            this.mWeatherCode = code;
            this.mWeatherType = type;
            this.mWeatherDescription = description;
            this.mIcon = icon;
        }

        @SerializedName("id")
        private int mWeatherCode;

        @SerializedName("main")
        private String mWeatherType;

        @SerializedName("description")
        private String mWeatherDescription;

        @SerializedName("icon")
        private String mIcon;


        public int getWeatherCode() {
            return mWeatherCode;
        }

        public String getWeatherType() {
            return mWeatherType;
        }

        public String getWeatherDescription() {
            return mWeatherDescription;
        }

        public String getIcon() {
            return mIcon;
        }
    }


    public static class Clouds {

        // Constructor required for writing in from parcel
        public Clouds(int cloudCover) {

            this.mCloudCover = cloudCover;
        }

        @SerializedName("all")
        private int mCloudCover;

        public int getCloudCover() {
            return mCloudCover;
        }
    }

    public static class Wind {

        // Constructor required for writing in from parcel
        public Wind(double windSpeed, double degrees) {

            this.mWindSpeed = windSpeed;
            this.mDegrees = degrees;
        }

        @SerializedName("speed")
        private double mWindSpeed;

        @SerializedName("deg")
        private double mDegrees;


        public double getWindSpeed() {
            return mWindSpeed;
        }

        public double getDegrees() {
            return mDegrees;
        }
    }

    public static class Rain {

        // Constructor required for writing in from parcel
        public Rain(double rain3hr) {

            this.mRain3hr = rain3hr;
        }

        @SerializedName("3h")
        private double mRain3hr;


        public double getRain3hr() {
            return mRain3hr;
        }
    }

    public static class Snow {

        // Constructor required for writing in from parcel
        public Snow(double snow3hr) {

            this.mSnow3hr = snow3hr;
        }

        @SerializedName("3h")
        private double mSnow3hr;


        public double getSnow3hr() {
            return mSnow3hr;
        }
    }


    /**
     * Parcelable Implementation - A lot of this code is boilerplate code
     * where 'blanks' are filled in to complete and make sure the class gets 'parcelled'
     * correctly.
     */

    /**
     * Private constructor provided for the CREATOR interface, which
     * is used to de-marshal an WeatherCurrentData from the Parcel of data.
     * <p/>
     * The order of reading in variables HAS TO MATCH the order in
     * writeToParcel(Parcel, int)
     *
     * @param in parcel object that contains all objects/primitives passed in from
     *           writeToParcel
     */

    private WeatherForecastData(Parcel in) {

        /**
         * Write back into the WeatherCurrentData class all the objects/primitive types
         * after they have been passed between address spaces/activities etc.
         * in EXACT order they were put in the writeToParcel method
         */

        Log.d(TAG, "Reading from Parcel");

        // ** ADD CURRENT WEATHER OBJECT FIRST **
        mCurrentWeather.add(new CurrentWeather(in.readInt(),
                in.readString(),
                in.readString(),
                in.readString()));

        mCity = new City(in.readInt(),
                in.readString(),
                new Coordinates(in.readDouble(),
                        in.readDouble()),
                in.readString());

        mCode = in.readString();

        mWeatherLists.add(new WeatherList(in.readLong(),
                new MainInfo(in.readDouble(),
                        in.readDouble(),
                        in.readDouble(),
                        in.readDouble(),
                        in.readDouble(),
                        in.readDouble(),
                        in.readInt()),
                mCurrentWeather,
                new Clouds(in.readInt()),
                new Wind(in.readDouble(),
                        in.readDouble()),
                new Rain(in.readDouble()),
                new Snow(in.readDouble()),
                in.readString()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Log.d(TAG, "Writing to Parcel");

        /**
         * Write all the elements into the Parcel, these have to be put in
         * the EXACT order they will be taken out in the Constructor :
         *      private WeatherCurrentData(Parcel in)
         */

        // ** ADD CURRENT WEATHER OBJECT FIRST **
        dest.writeInt(mCurrentWeather.get(0).getWeatherCode());
        dest.writeString(mCurrentWeather.get(0).getWeatherType());
        dest.writeString(mCurrentWeather.get(0).getWeatherDescription());
        dest.writeString(mCurrentWeather.get(0).getIcon());

        dest.writeInt(mCity.getCityId());
        dest.writeString(mCity.getCityName());
        dest.writeDouble(mCity.getCoordinates().getLongitude());
        dest.writeDouble(mCity.getCoordinates().getLatitude());
        dest.writeString(mCity.getCountry());

        dest.writeString(mCode);

        dest.writeLong(mWeatherLists.get(0).getDateTime());
        dest.writeDouble(mWeatherLists.get(0).getMainInfo().getTemp());
        dest.writeDouble(mWeatherLists.get(0).getMainInfo().getTempMin());
        dest.writeDouble(mWeatherLists.get(0).getMainInfo().getTempMax());
        dest.writeDouble(mWeatherLists.get(0).getMainInfo().getPressure());
        dest.writeDouble(mWeatherLists.get(0).getMainInfo().getSeaLevel());
        dest.writeDouble(mWeatherLists.get(0).getMainInfo().getGroundLevel());
        dest.writeInt(mWeatherLists.get(0).getMainInfo().getHumidity());
        dest.writeInt(mWeatherLists.get(0).getClouds().getCloudCover());
        dest.writeDouble(mWeatherLists.get(0).getWind().getWindSpeed());
        dest.writeDouble(mWeatherLists.get(0).getWind().getDegrees());
        dest.writeDouble(mWeatherLists.get(0).getRain().getRain3hr());
        dest.writeDouble(mWeatherLists.get(0).getSnow().getSnow3hr());
        dest.writeString(mWeatherLists.get(0).getDateAsString());
    }


    public static final Creator<WeatherForecastData> CREATOR = new Creator<WeatherForecastData>() {
        @Override
        public WeatherForecastData createFromParcel(Parcel in) {
            return new WeatherForecastData(in);
        }

        @Override
        public WeatherForecastData[] newArray(int size) {
            return new WeatherForecastData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
