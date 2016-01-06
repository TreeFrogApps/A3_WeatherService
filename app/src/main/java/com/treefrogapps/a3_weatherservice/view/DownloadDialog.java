package com.treefrogapps.a3_weatherservice.view;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.treefrogapps.a3_weatherservice.MVP;
import com.treefrogapps.a3_weatherservice.R;
import com.treefrogapps.a3_weatherservice.presenter.WeatherPresenter;
import com.treefrogapps.a3_weatherservice.utils.DownloadUtils;
import com.treefrogapps.a3_weatherservice.utils.Utils;

import java.lang.ref.WeakReference;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class DownloadDialog extends DialogFragment implements View.OnClickListener {

    private static String TAG = DownloadDialog.class.getSimpleName();

    private static final int SYNC = 1;
    private static final int ASYNC = 2;

    private WeakReference<MVP.WeatherPresenterInterface> mPresenterInterface;
    private Dialog mDialog;

    private EditText mLocation;
    private EditText mCountryCode;

    private RadioGroup mRadioGroup;
    private RadioButton mRadioCurrent;

    public DownloadDialog() {
        // Required empty public constructor
    }

    /**
     * New instance method to declare Object passed into the method
     * avoids non default fragment constructor - which is highly discouraged
     * usual method is to pass a 'Bundle' to the fragment. Objects can be passed if
     * they use the Serializable or Parcelable (quicker) interfaces
     *
     * @param presenterInterface presenterInterface instance (for calling methods and getting context)
     * @return DialogFragment instance with Presenter object initialised
     */
    public static DownloadDialog newInstance(MVP.WeatherPresenterInterface presenterInterface) {

        Log.d(TAG, "Dialog New Instance created");

        DownloadDialog downloadDialog = new DownloadDialog();
        downloadDialog.mPresenterInterface = new WeakReference<>(presenterInterface);

        return downloadDialog;
    }


    /**
     * used to provide dialog dialog with a New WeakReference to the Presenter Layer
     * which calls this method after a configuration change and provdes a new instance of
     * itself with new references to the View (Activity) Layer (Activities can come and go with
     * screen rotation meaning any contexts held to the activity will point to a null object reference)
     *
     * @param presenterInterface presenter layer
     */
    public void setPresenterInterface(MVP.WeatherPresenterInterface presenterInterface) {

        Log.d(TAG, "New WeakReference to Presenter Created");
        this.mPresenterInterface = new WeakReference<>(presenterInterface);

    }

    /**
     * Hook method called when .show();  is called (along with other lifecycle hook methods)
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /**
         * Create New dialog and return dialog instance
         */
        mDialog = new Dialog(mPresenterInterface.get().getActivityContext());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.download_dialog);
        mDialog.setCancelable(false);

        mLocation = (EditText) mDialog.findViewById(R.id.dialogCityEditText);
        mCountryCode = (EditText) mDialog.findViewById(R.id.dialogCodeEditText);

        Button mCancel = (Button) mDialog.findViewById(R.id.dialogCancelButton);
        mCancel.setOnClickListener(this);

        Button mSync = (Button) mDialog.findViewById(R.id.dialogSyncButton);
        mSync.setOnClickListener(this);

        Button mAsync = (Button) mDialog.findViewById(R.id.dialogASyncButton);
        mAsync.setOnClickListener(this);

        mRadioGroup = (RadioGroup) mDialog.findViewById(R.id.radioGroup);
        mRadioCurrent = (RadioButton) mDialog.findViewById(R.id.radioCurrent);

        return mDialog;
    }

    /**
     * On Click listener interface method
     *
     * @param v view passed in that was clicked
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.dialogCancelButton:
                cancelPressed();
                break;

            case R.id.dialogSyncButton:
                if (DownloadUtils.checkConnection(mPresenterInterface.get().getActivityContext())){

                    syncAsyncPressed(mLocation.getText().toString(),
                            mCountryCode.getText().toString(), SYNC);
                } else {Utils.showToast(mPresenterInterface.get().getActivityContext(), "No Connection");}
                break;

            case R.id.dialogASyncButton:
                if (DownloadUtils.checkConnection(mPresenterInterface.get().getActivityContext())){
                syncAsyncPressed(mLocation.getText().toString(),
                        mCountryCode.getText().toString(), ASYNC);
                } else {Utils.showToast(mPresenterInterface.get().getActivityContext(), "No Connection");}
                break;
        }

    }

    /**
     * Close the dialog window
     */
    public void cancelPressed() {

        mDialog.dismiss();
    }

    /**
     * SYNC / ASYNC method
     * Check the location is not empty (and country code) and get the type of weather data -
     * Current, or 5 Day Forecast weather from the radio group (checked id) then push request to
     * the Presenter for the weather data and dismiss dialog
     *
     * @param location    String location
     * @param countryCode String country code (optional)
     */
    public void syncAsyncPressed(String location, String countryCode, int type) {

        int checkedId = mRadioGroup.getCheckedRadioButtonId();

        if (Utils.checkText(location)) {

            if (!countryCode.equals("")) {

                location = location.trim() + "," + countryCode.trim();
            }

            WeatherPresenter.RETRIEVING_DATA = true;

            Utils.showToast(mPresenterInterface.get().getAppContext(), "Getting Weather Data");

            switch (type) {

                case SYNC:

                    if (checkedId == mRadioCurrent.getId()) {
                        mPresenterInterface.get().getWeatherCurrentSync(location);
                    } else {
                        mPresenterInterface.get().getWeatherForecastSync(location);
                    }
                    break;

                case ASYNC:

                    if (checkedId == mRadioCurrent.getId()) {
                        mPresenterInterface.get().getWeatherCurrentASync(location);
                    } else {
                        mPresenterInterface.get().getWeatherForecastASync(location);
                    }
                    break;
            }

            mDialog.dismiss();

        } else {

            Utils.showToast(mPresenterInterface.get().getAppContext(), "Enter a location");
        }
    }


}
