package com.treefrogapps.a3_weatherservice.common;

/**
 * Presenter operations interface
 *
 * @param <ViewInterface> Generic type - view interface
 */
public interface PresenterOperations<ViewInterface> {

    void onCreate(ViewInterface viewInterface);

    void onConfigChange(ViewInterface viewInterface);

    void onDestroy();

}
