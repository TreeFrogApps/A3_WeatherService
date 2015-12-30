package com.treefrogapps.a3_weatherservice.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Create a RetainedFragmentManager to handle configuration changes
 * <p/>
 * WeakReferences used so proper Garbage Collection can occur. Checks if this is First time in
 * and returns true or false by creating a retained fragment from findFragmentByTag, if null a new
 * retained fragment is created and returns true, else returns false (configuration change)
 */

public class RetainedFragmentManager {

    public static final String TAG = RetainedFragmentManager.class.getSimpleName();


    private WeakReference<FragmentManager> mFragmentManager;

    private String mRetainedFragmentTag;

    private RetainedFragment mRetainedFragment;


    public RetainedFragmentManager(FragmentManager fragmentManager, String fragmentTag) {

        this.mFragmentManager = new WeakReference<>(fragmentManager);

        this.mRetainedFragmentTag = fragmentTag;
    }

    /**
     * Method to check if there is an instance of the retained fragment already
     * using findFragmentByTag to check - if null then FirstTimeIn true, and create a
     * new Retained Fragment (Headless Fragment - No IU), else fragment already exists
     *
     * @return boolean value
     */
    public boolean firstTineIn() {

        mRetainedFragment = (RetainedFragment)
                mFragmentManager.get().findFragmentByTag(mRetainedFragmentTag);

        if (mRetainedFragment == null) {

            Log.d(TAG, "First Time in - TRUE - creating Retained Fragment");

            mRetainedFragment = new RetainedFragment();

            mFragmentManager.get().beginTransaction().add(mRetainedFragment, TAG).commit();

            return true;

        } else {

            Log.d(TAG, "First Time in - FALSE - retrieve Retained Fragment");

            return false;
        }
    }

    /**
     * Put a object into the Retained Fragment HashMap
     *
     * @param key    String key of object
     * @param object Object to be placed into the HashMap
     */
    public void putObject(String key, Object object) {

        mRetainedFragment.putObject(key, object);
    }

    /**
     * @param key String key of object
     * @param <T> Generic object type to retrieve from Hash map
     * @return Generic object - type of object is unchecked, so will throw ClassCastException
     * if not cast to the object type in the hash map correctly.
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {

        return (T) mRetainedFragment.getObject(key);
    }

    /**
     * "Headless" Fragment that retains state information between
     * configuration changes.
     */
    public static class RetainedFragment extends Fragment {

        private HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            /**
             * MUST be set to TRUE in order for it to survive config changes
             */
            setRetainInstance(true);
        }

        public void putObject(String key, Object object) {

            mData.put(key, object);
        }

        @SuppressWarnings("unchecked")
        public <T> T getObject(String key) {

            return (T) mData.get(key);
        }
    }
}
