package com.treefrogapps.a3_weatherservice.common;

import android.content.Context;

/**
 * Common Interface for gaining a context
 */

public interface ContextView {

    Context getActivityContext();

    Context getAppContext();

}
