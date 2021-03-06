package teamtreehouse.com.stormy.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.weather.Forecast;

// Tablet layout - current weather at top with hourly and daily forecast lists in two columns below
// using three child fragment panes
public class TabletFragment extends Fragment implements DataUpdate {
    private static final String CURRENT_FRAGMENT = "current_forecast_fragment";
    private static final String HOURLY_FRAGMENT = "hourly_forecast_fragment";
    private static final String DAILY_FRAGMENT = "daily_forecast_fragment";

    private DataUpdate mHourlyUpdate;
    private DataUpdate mDailyUpdate;
    private DataUpdate mCurrentUpdate;

    public TabletFragment() {
        // Required empty public constructor
    }

    public static TabletFragment newInstance() {
        TabletFragment fragment = new TabletFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tablet, container, false);

        FragmentManager fragmentManager = getChildFragmentManager();

        // Current weather forecast fragment
        CurrentForecastFragment savedCurrentFragment = (CurrentForecastFragment) fragmentManager
                .findFragmentByTag(CURRENT_FRAGMENT);
        if (savedCurrentFragment == null) {
            CurrentForecastFragment currentForecastFragment = CurrentForecastFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.topPlaceholder, currentForecastFragment, CURRENT_FRAGMENT)
                    .commit();
            mCurrentUpdate = currentForecastFragment;
        } else {
            mCurrentUpdate = savedCurrentFragment;
        }

        // Hourly forecast fragment
        HourlyForecastFragment savedHourlyFragment = (HourlyForecastFragment) fragmentManager
                .findFragmentByTag(HOURLY_FRAGMENT);
        if (savedHourlyFragment == null) {
            HourlyForecastFragment hourlyForecastFragment = HourlyForecastFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.leftPlaceholder, hourlyForecastFragment, HOURLY_FRAGMENT)
                    .commit();
            mHourlyUpdate = hourlyForecastFragment;
        } else {
            mHourlyUpdate = savedHourlyFragment;
        }

        // Daily forecast fragment
        DailyForecastFragment savedDailyFragment = (DailyForecastFragment) fragmentManager
                .findFragmentByTag(DAILY_FRAGMENT);
        if (savedDailyFragment == null) {
            DailyForecastFragment dailyForecastFragment = DailyForecastFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.rightPlaceholder, dailyForecastFragment, DAILY_FRAGMENT)
                    .commit();
            mDailyUpdate = dailyForecastFragment;
        } else {
            mDailyUpdate = savedDailyFragment;
        }

        return view;
    }

    // Pass the new data to the children
    @Override
    public void onDataUpdate(Forecast forecast) {
        mCurrentUpdate.onDataUpdate(forecast);
        mHourlyUpdate.onDataUpdate(forecast);
        mDailyUpdate.onDataUpdate(forecast);
    }
}
