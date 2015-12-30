package com.treefrogapps.a3_weatherservice;

import com.treefrogapps.a3_weatherservice.common.ContextView;
import com.treefrogapps.a3_weatherservice.common.ModelOperations;
import com.treefrogapps.a3_weatherservice.common.PresenterOperations;

/**
 * Model View Presenter Interfaces used with the corresponding classes
 * all methods for each class come from the interfaces.
 * <p/>
 * Each interface extends a common Interface - this could be used for other Activities
 * so code is not types more than once, all common interface methods are extended here
 * (interfaces can extend other interfaces just like classes can extend super classes)
 */
public interface MVP {


    /**
     * WeatherViewInterface :
     * <p/>
     * Extends ContextView - necessary as Generic Activity Implements ContextView Interface.
     * This interface needs to have access to the methods of ContextView in the super class
     * that Implements them. Weather Activity doesn't have this method so will call to the
     * super class method if requested from a WeatherActivity reference.
     */
    interface WeatherViewInterface extends ContextView {

    }

    /**
     * WeatherPresenterInterface :
     * <p/>
     * Extends the 'Base' Presenter Operations Interface required by every Presenter layer
     * Presenter Operations is given a View Interface Reference
     */
    interface WeatherPresenterInterface extends PresenterOperations<WeatherViewInterface> {


    }

    /**
     * WeatherModelInterface :
     * <p/>
     * Extends the 'Base' Model Operations Interface required by every Model layer
     * Model Operations is given a Presenter Interface Reference
     */
    interface WeatherModelInterface extends ModelOperations<WeatherPresenterInterface> {


    }
}
