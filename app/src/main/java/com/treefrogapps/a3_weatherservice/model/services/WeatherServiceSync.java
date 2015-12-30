package com.treefrogapps.a3_weatherservice.model.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Weather Service which is SYNCHRONOUS - AIDL method call that returns a result
 * this will block the calling thread so should be performed in a AsyncTask
 */
public class WeatherServiceSync extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
