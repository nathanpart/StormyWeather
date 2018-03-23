package teamtreehouse.com.stormy.ui;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.weather.Current;
import teamtreehouse.com.stormy.weather.Forecast;

public class TabletFragment extends Fragment implements DataUpdate {
    private static final String HOURLY_FRAGMENT = "hourly_forecast_fragment";
    private static final String DAILY_FRAGMENT = "daily_forecast_fragment";

    private DataUpdate mHourlyUpdate;
    private DataUpdate mDailyUpdate;

    private TextView mTimeLabel;
    private TextView mTemperatureLabel;
    private TextView mHumidityValue;
    private TextView mPrecipValue;
    private TextView mSummaryLabel;
    private ImageView mIconImageView;

    private Current mCurrent;

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

        mTimeLabel = view.findViewById(R.id.timeLabel);
        mTemperatureLabel = view.findViewById(R.id.temperatureLabel);
        mHumidityValue = view.findViewById(R.id.humidityValue);
        mPrecipValue = view.findViewById(R.id.precipValue);
        mSummaryLabel = view.findViewById(R.id.summaryLabel);
        mIconImageView = view.findViewById(R.id.iconImageView);

        if (savedInstanceState != null) {
            mCurrent = savedInstanceState.getParcelable("current");
        }

        if (mCurrent != null) {
            updateViews();
        }

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

    private void updateViews() {
        mTemperatureLabel.setText(mCurrent.getTemperature() + "");
        mTimeLabel.setText("At " + mCurrent.getFormattedTime() + " it will be");
        mHumidityValue.setText(mCurrent.getHumidity() + "");
        mPrecipValue.setText(mCurrent.getPrecipChance() + "%");
        mSummaryLabel.setText(mCurrent.getSummary());

        Drawable drawable = getResources().getDrawable(mCurrent.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("current", mCurrent);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDataUpdate(Forecast forecast) {
        mCurrent = forecast.getCurrent();
        updateViews();
        mHourlyUpdate.onDataUpdate(forecast);
        mDailyUpdate.onDataUpdate(forecast);
    }
}
