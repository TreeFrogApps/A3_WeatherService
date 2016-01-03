package com.treefrogapps.a3_weatherservice.common;

import android.content.Context;
import android.support.v4.app.FragmentManager;

/**
 * Common Interface for gaining a context
 */

public interface ContextView {

    Context getActivityContext();

    Context getAppContext();

    FragmentManager getFragManager();

}
