package com.treefrogapps.a3_weatherservice.model.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * Weather Service which is ASYNCHRONOUS - one way AIDL method call that doesn't return (void).
 * This will NOT block the calling thread.
 */
public class WeatherServiceAsync extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
