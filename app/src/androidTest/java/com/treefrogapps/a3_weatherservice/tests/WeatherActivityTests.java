package com.treefrogapps.a3_weatherservice.tests;

import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

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

    public void testRunValidCities(){

        String[] citiesToTest = new String[]
                {"London", "New York", "Milan", "Mexico City", "Edinburgh", "Madrid", "Riga", "Moscow"};

        mSolo.assertCurrentActivity("Wrong Activity", WeatherActivity.class);

        assertTrue("Activity failed to load", mSolo.waitForActivity(WeatherActivity.class, 10000));


        for (final String city : citiesToTest){

            mSolo.clickOnView(mSolo.getView(R.id.fabButton));

            assertTrue("Window dialog not open", mSolo.waitForDialogToOpen(1000));

            mSolo.setActivityOrientation(Solo.LANDSCAPE);

            mSolo.enterText((EditText) mSolo.getView(R.id.dialogCityEditText), city);

            mSolo.setActivityOrientation(Solo.PORTRAIT);

            assertTrue(mSolo.searchText(city));

            mSolo.clickOnView(mSolo.getView(R.id.dialogASyncButton));


            final RecyclerView recyclerView = (RecyclerView) mSolo.getView(R.id.recyclerView);
            final TextView textView = (TextView) mSolo.getView(R.id.currentTextViewCityName);

            Condition condition = new Condition() {
                @Override
                public boolean isSatisfied() {
                    return recyclerView.getAdapter().getItemCount() > 0 &&
                            textView.getText().toString().equals(city);
                }
            };

            assertTrue("Problem with loading the weather for " + city, mSolo.waitForCondition(condition, 10000));
        }


    }
}
