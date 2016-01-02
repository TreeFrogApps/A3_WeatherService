package com.treefrogapps.a3_weatherservice.common;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.treefrogapps.a3_weatherservice.presenter.WeatherPresenter;

/**
 * Generic Base Activity - used to initialise presenter layer, and other generic tasks
 * common to all applicable activities
 * <p/>
 * Generics used to define the ViewInterface and Presenter Class references.  'Generic' objects
 * can 'extend' interfaces using the 'extend' keyword, instead of implements - the compiler doesn't
 * follow usual convention of 'implements' with interfaces when used in conjunction with Generics
 *
 * @param <ViewInterface> Generic view interface (defined when Generic Activity is extended)
 * @param <Presenter>     Generic Presenter Class (defined with Generic Activity is extended)
 */
public abstract class GenericActivity<ViewInterface, Presenter extends PresenterOperations<ViewInterface>>
        extends AppCompatActivity implements ContextView {

    private static String TAG = GenericActivity.class.getSimpleName();

    /**
     * Create new RetainedFragmentManager - this creates a Fragment that has: setRetainInstance(true)
     * and holds a Presenter object in a HashMap
     */
    private final RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(getSupportFragmentManager(), RetainedFragmentManager.TAG);

    /**
     * Presenter Instance
     */
    private Presenter mPresenter;

    /**
     * Method to initialise/reinitialise Presenter Layer and its reference to the View Layer
     * (not to be confused with the Activity Life Cycle Hook Method with the same name)
     *
     * @param viewInterface Generic View Interface
     * @param presenter     Generic as Class object (required to have access to class method newInstance())
     */
    public void onCreate(ViewInterface viewInterface, Class<Presenter> presenter) {

        Log.d(TAG, "onCreate - called");

        if (mRetainedFragmentManager.firstTineIn()) {

            initialise(viewInterface, presenter);

        } else {

            reinitialise(viewInterface);
        }
    }

    /**
     * Method to initialise the Generic Presenter Layer
     *
     * @param viewInterface Generic View Interface
     * @param presenter     Generic as Class object (required to have access to class method newInstance())
     *                      the generic Presenter is implicit reference - when Generic Activity is extended
     *                      the the Explicit reference is made (example - see WeatherActivity)
     */
    private void initialise(ViewInterface viewInterface, Class<Presenter> presenter) {

        try {

            // calls zero argument constructor and creates new object
            // this is a implicit reference.
            mPresenter = presenter.newInstance();

            mRetainedFragmentManager.putObject(WeatherPresenter.TAG, mPresenter);

            mPresenter.onCreate(viewInterface);

        } catch (InstantiationException e) {
            Log.e(TAG, "Instantiation Exception " + e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Illegal Access Exception  " + e);
        }
    }

    /**
     * Method to reinitialise the Generic Presenter Layer, uses the Retained Fragment Manager /
     * Retained Fragment to retrieve the Presenter from a HashMap and passed onto the Presenter
     * to establish a new Weak Reference to the View Layer
     *
     * @param viewInterface Generic View Interface
     */
    private void reinitialise(ViewInterface viewInterface) {

        mPresenter = mRetainedFragmentManager.getObject(WeatherPresenter.TAG);

        mPresenter.onConfigChange(viewInterface);
    }

    /**
     * Method to return a Context
     *
     * @return current Activity context
     */
    @Override
    public Context getActivityContext() {
        return this;
    }

    /**
     * Method to return Application context
     *
     * @return current application context
     */
    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    /**
     * Method to get presenter - for use in subclasses (WeatherActivity - View Layer)
     * when a reference to the presenter is required
     */
    public Presenter getPresenter() {

        return mPresenter;
    }
}
