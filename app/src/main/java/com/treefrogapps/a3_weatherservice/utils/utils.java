package com.treefrogapps.a3_weatherservice.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Generic Helper methods
 */

public class utils {


    public static void showToast(Context context, String message){

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean checkText(String text){

        /**
         * Very simple checking procedure to verify a location
         * given more time would have used this list : http://openweathermap.org/help/city_list.txt
         * in a predefined SQL database to check location against City ID
         */
        return text != null
                && text.trim().length() > 2;
    }
}
