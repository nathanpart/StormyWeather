package teamtreehouse.com.stormy.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.weather.Current;
import teamtreehouse.com.stormy.weather.Forecast;

public class CurrentForecastFragment extends Fragment implements DataUpdate {
    private TextView mTimeLabel;
    private TextView mTemperatureLabel;
    private TextView mHumidityValue;
    private TextView mPrecipValue;
    private TextView mSummaryLabel;
    private ImageView mIconImageView;
    private Current mCurrent;


    public CurrentForecastFragment() {
        // Required empty public constructor
    }

    public static CurrentForecastFragment newInstance() {
        CurrentForecastFragment fragment = new CurrentForecastFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current, container, false);


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
    }

}
