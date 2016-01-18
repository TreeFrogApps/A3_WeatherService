package com.treefrogapps.a3_weatherservice.tests;

import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;
import com.treefrogapps.a3_weatherservice.R;
import com.treefrogapps.a3_weatherservice.view.WeatherActivity;

/**
 * Basic Testing
 */
public class WeatherActivityTests extends ActivityInstrumentationTestCase2 {


    private Solo mSolo;

    public WeatherActivityTests() {
        super(WeatherActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        mSolo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testRun(){

        mSolo.assertCurrentActivity("Wrong Activity", WeatherActivity.class);

        assertTrue("Activity failed to load", mSolo.waitForActivity(WeatherActivity.class, 10000));

        mSolo.clickOnView(mSolo.getView(R.id.fabButton));

        assertTrue("Window dialog not open", mSolo.waitForDialogToOpen(1000));

        mSolo.setActivityOrientation(Solo.LANDSCAPE);

        mSolo.setActivityOrientation(Solo.PORTRAIT);

        mSolo.enterText((EditText) mSolo.getView(R.id.dialogCityEditText), "London");

        mSolo.setActivityOrientation(Solo.LANDSCAPE);

        mSolo.setActivityOrientation(Solo.PORTRAIT);

        assertTrue(mSolo.searchText("London"));

        mSolo.clickOnView(mSolo.getView(R.id.dialogASyncButton));


        final RecyclerView recyclerView = (RecyclerView) mSolo.getView(R.id.recyclerView);

        Condition condition = new Condition() {
            @Override
            public boolean isSatisfied() {
                return recyclerView.getAdapter().getItemCount() > 0;
            }
        };


        assertTrue("Problem with loading the weather", mSolo.waitForCondition(condition, 10000));
    }
}
