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
 *
 * Uses Gson @SerializedName annotations to de-serialise json data into
 * Java primitive types and objects.
 *
 * json link & typical data :
 *
 * http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=d013bcc0366f96630c0558393113cd8b
 *
 * {"coord":{"lon":-0.13,"lat":51.51},"weather":[{"id":500,"main":"Rain","description":
 * "light rain","icon":"10n"}],"base":"stations","main":{"temp":284.81,"pressure":1012,
 * "humidity":81,"temp_min":283.15,"temp_max":286.48},"visibility":10000,"wind":{"speed":4.6,
 * "deg":170},"clouds":{"all":90},"dt":1451344865,"sys":{"type":1,"id":5078,"country":"GB",
 * "sunrise":1451289970,"sunset":1451318323},"id":2643743,"name":"London","cod":200}
 *
 * item definitions http://openweathermap.org/weather-data#current
 */
public class WeatherCurrentData implements Parcelable {

    private static String TAG = WeatherCurrentData.class.getSimpleName();

    /**
     * Time stamp to be added after json has been parsed using gson
     * Time stamp is first into parcel in, an first out
     */
    private long mTimeStamp;

    public long getTimeStamp(){
        return this.mTimeStamp;
    }

    public void setTimeStamp(long timeStamp){
        this.mTimeStamp = timeStamp;
    }



    @SerializedName("coord")
    private Coordinates mCoordinates;

    @SerializedName("weather")
    private ArrayList<Weather> mWeatherList = new ArrayList<>();

    @SerializedName("base")
    private String mBase;

    @SerializedName("main")
    private Main mMain;

    @SerializedName("visibility")
    private long mVisibility;

    @SerializedName("wind")
    private Wind mWind;

    @SerializedName("clouds")
    private Clouds mClouds;

    @SerializedName("dt")
    private long mDateTime;

    @SerializedName("sys")
    private CityInfo mCityInfo;

    @SerializedName("id")
    private long mWeatherId;

    @SerializedName("name")
    private String mCity;

    @SerializedName("cod")
    private int mCode;


    /**
     * Getter methods
     */
    public Coordinates getCoordinates() {
        return mCoordinates;
    }

    public ArrayList<Weather> getWeatherList() {
        return mWeatherList;
    }

    public String getBase() {
        return mBase;
    }

    public Main getMain() {
        return mMain;
    }

    public long getVisibility() {
        return mVisibility;
    }

    public Wind getWind() {
        return mWind;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public long getDateTime() {
        return mDateTime;
    }

    public CityInfo getCityInfo() {
        return mCityInfo;
    }

    public long getWeatherId() {
        return mWeatherId;
    }

    public String getCity() {
        return mCity;
    }

    public int getCode() {
        return mCode;
    }


    /**
     * Coordinates Static Inner Class - with getter methods
     */
    public static class Coordinates {

        // Constructor required for writing in from parcel
        public Coordinates(double lon, double lat){

            this.mLongitude = lon;
            this.mLatitude = lat;
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


    /**
     * Weather Static Inner Class - with getter methods
     */
    public static class Weather {

        // Constructor required for writing in from parcel
        public Weather (int id, String weather, String description, String icon){

            this.mWeatherId = id;
            this.mWeatherType = weather;
            this.mWeatherDescription = description;
            this.mIcon = icon;
        }

        @SerializedName("id")
        private int mWeatherId;

        @SerializedName("main")
        private String mWeatherType;

        @SerializedName("description")
        private String mWeatherDescription;

        @SerializedName("icon")
        private String mIcon;


        public int getWeatherId() {
            return mWeatherId;
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


    /**
     * Main Static Inner Class - with getter methods
     */
    public static class Main {

        // Constructor required for writing in from parcel
        public Main (double temp, int pressure, int humidity, double tempMin, double tempMax) {

            this.mTemp = temp;
            this.mPressure = pressure;
            this.mHumidity = humidity;
            this.mTempMin = tempMin;
            this.mTempMax = tempMax;
        }

        @SerializedName("temp")
        private double mTemp;

        @SerializedName("pressure")
        private int mPressure;

        @SerializedName("humidity")
        private int mHumidity;

        @SerializedName("temp_min")
        private double mTempMin;

        @SerializedName("temp_max")
        private double mTempMax;


        public double getTemp() {
            return mTemp;
        }

        public int getPressure() {
            return mPressure;
        }

        public int getHumidity() {
            return mHumidity;
        }

        public double getTempMin() {
            return mTempMin;
        }

        public double getTempMax() {
            return mTempMax;
        }
    }


    /**
     * Wind Static Inner Class - with getter methods
     */
    public static class Wind {

        // Constructor required for writing in from parcel
        public Wind (double windSpeed, int degrees){

            this.mWindSpeed = windSpeed;
            this.mDegrees = degrees;
        }

        @SerializedName("speed")
        private double mWindSpeed;

        @SerializedName("deg")
        private int mDegrees;


        public double getWindSpeed() {
            return mWindSpeed;
        }

        public int getDegress() {
            return mDegrees;
        }
    }


    /**
     * Clouds Static Inner Class - with getter methods
     */
    public static class Clouds {

        // Constructor required for writing in from parcel
        public Clouds (int cloud) {

            this.mCloudCover = cloud;
        }

        @SerializedName("all")
        private int mCloudCover;


        public int getCloudCover() {
            return mCloudCover;
        }
    }


    /**
     * City Static Inner Class - with getter methods
     */
    public static class CityInfo {

        // Constructor required for writing in from parcel
        public CityInfo (int type, int id, String code, long sunrise, long sunset) {

            this.mType = type;
            this.mCityId = id;
            this.mCountryCode = code;
            this.mSunriseTime = sunrise;
            this.mSunsetTime = sunset;
        }

        @SerializedName("type")
        private int mType;

        @SerializedName("id")
        private int mCityId;

        @SerializedName("country")
        private String mCountryCode;

        @SerializedName("sunrise")
        private long mSunriseTime;

        @SerializedName("sunset")
        private long mSunsetTime;


        public int getType() {
            return mType;
        }

        public int getCityId() {
            return mCityId;
        }

        public String getCountryCode() {
            return mCountryCode;
        }

        public long getSunriseTime() {
            return mSunriseTime;
        }

        public long getSunsetTime() {
            return mSunsetTime;
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
     * <p>
     * The order of reading in variables HAS TO MATCH the order in
     * writeToParcel(Parcel, int)
     *
     * @param in parcel object that contains all objects/primitives passed in from
     *           writeToParcel
     */
    private WeatherCurrentData(Parcel in) {

        Log.d(TAG, "Reading from Parcel");

        /**
         * Write back into the WeatherCurrentData class all the objects/primitive types
         * after they have been passed between address spaces/activities etc.
         * in EXACT order they were put in the writeToParcel method
         */

        // get current system time when object is parceled
        mTimeStamp = in.readLong();

        mCoordinates = new Coordinates(in.readDouble(),
                                       in.readDouble());

        mWeatherList.add(new Weather(in.readInt(),
                                     in.readString(),
                                     in.readString(),
                                     in.readString()));

        mBase = in.readString();

        mMain = new Main(in.readDouble(),
                             in.readInt(),
                             in.readInt(),
                             in.readDouble(),
                             in.readDouble());

        mVisibility = in.readLong();

        mWind = new Wind(in.readDouble(),
                         in.readInt());

        mClouds = new Clouds(in.readInt());

        mDateTime = in.readLong();

        mCityInfo = new CityInfo(in.readInt(),
                                 in.readInt(),
                                 in.readString(),
                                 in.readLong(),
                                 in.readLong());

        mWeatherId = in.readLong();

        mCity = in.readString();

        mCode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Log.d(TAG, "Writing to Parcel");

        /**
         * Write all the elements into the Parcel, these have to be put in
         * the EXACT order they will be taken out in the Constructor :
         *      private WeatherCurrentData(Parcel in)
         */

        dest.writeLong(mTimeStamp);
        dest.writeDouble(mCoordinates.getLongitude());
        dest.writeDouble(mCoordinates.getLatitude());

        Weather mWeather = mWeatherList.get(0);
        dest.writeInt(mWeather.getWeatherId());
        dest.writeString(mWeather.getWeatherType());
        dest.writeString(mWeather.getWeatherDescription());
        dest.writeString(mWeather.getIcon());

        dest.writeString(mBase);

        dest.writeDouble(mMain.getTemp());
        dest.writeInt(mMain.getPressure());
        dest.writeInt(mMain.getHumidity());
        dest.writeDouble(mMain.getTempMin());
        dest.writeDouble(mMain.getTempMax());

        dest.writeLong(mVisibility);

        dest.writeDouble(mWind.getWindSpeed());
        dest.writeInt(mWind.getDegress());

        dest.writeInt(mClouds.getCloudCover());

        dest.writeLong(mDateTime);

        dest.writeInt(mCityInfo.getType());
        dest.writeInt(mCityInfo.getCityId());
        dest.writeString(mCityInfo.getCountryCode());
        dest.writeLong(mCityInfo.getSunriseTime());
        dest.writeLong(mCityInfo.getSunsetTime());

        dest.writeLong(mWeatherId);

        dest.writeString(mCity);

        dest.writeInt(mCode);
    }

    public static final Creator<WeatherCurrentData> CREATOR = new Creator<WeatherCurrentData>() {
        @Override
        public WeatherCurrentData createFromParcel(Parcel in) {
            return new WeatherCurrentData(in);
        }

        @Override
        public WeatherCurrentData[] newArray(int size) {
            return new WeatherCurrentData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
