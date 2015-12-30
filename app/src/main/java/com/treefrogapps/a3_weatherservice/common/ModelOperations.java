package com.treefrogapps.a3_weatherservice.common;

/**
 * Model Operations interface
 *
 * @param <PresenterInterface> Generic type - Presenter Interface
 */
public interface ModelOperations<PresenterInterface> {

    /**
     * Generic model operations
     *
     * @param presenterInterface Interface passed in
     */
    void onCreate(PresenterInterface presenterInterface);

    void onDestroy();
}
