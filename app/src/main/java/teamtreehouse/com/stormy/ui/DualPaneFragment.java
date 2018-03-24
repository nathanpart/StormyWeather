package teamtreehouse.com.stormy.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.weather.Forecast;

public class DualPaneFragment extends Fragment implements DataUpdate {
    private static final String HOURLY_FRAGMENT = "hourly_forecast_fragment";
    private static final String DAILY_FRAGMENT = "daily_forecast_fragment";

    private DataUpdate mHourlyUpdate;
    private DataUpdate mDailyUpdate;
    private DataUpdate mCurrentUpdate;

    public DualPaneFragment() {
        // Required empty public constructor
    }

    public static DualPaneFragment newInstance() {
        DualPaneFragment fragment = new DualPaneFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tablet, container, false);

        FragmentManager fragmentManager = getChildFragmentManager();

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

    @Override
    public void onDataUpdate(Forecast forecast) {
        if (mHourlyUpdate != null) mHourlyUpdate.onDataUpdate(forecast);
        if (mDailyUpdate  != null) mDailyUpdate.onDataUpdate(forecast);
    }
}
