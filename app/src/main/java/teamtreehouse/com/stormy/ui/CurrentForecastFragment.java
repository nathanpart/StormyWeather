package teamtreehouse.com.stormy.ui;

import android.graphics.drawable.Drawable;
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
    private View mView;
    private Drawable mGradient;


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
        mView = inflater.inflate(R.layout.fragment_current, container, false);


        mTimeLabel = mView.findViewById(R.id.timeLabel);
        mTemperatureLabel = mView.findViewById(R.id.temperatureLabel);
        mHumidityValue = mView.findViewById(R.id.humidityValue);
        mPrecipValue = mView.findViewById(R.id.precipValue);
        mSummaryLabel = mView.findViewById(R.id.summaryLabel);
        mIconImageView = mView.findViewById(R.id.iconImageView);

        if (savedInstanceState != null) {
            mCurrent = savedInstanceState.getParcelable("current");
        }

        if (mCurrent != null) {
            updateViews();
        } else {
            // Request data to be sent to us
            ((GetData) getActivity()).fetchData();
        }
        return mView;
    }

    private void updateViews() {

        if (mTemperatureLabel != null) {
            mTemperatureLabel.setText(mCurrent.getTemperature() + "");
            mTimeLabel.setText("At " + mCurrent.getFormattedTime() + " it will be");
            mHumidityValue.setText(mCurrent.getHumidity() + "");
            mPrecipValue.setText(mCurrent.getPrecipChance() + "%");
            mSummaryLabel.setText(mCurrent.getSummary());

            Drawable drawable = getResources().getDrawable(mCurrent.getIconId());
            mIconImageView.setImageDrawable(drawable);

            // If not a tablet, update the background gradient
            if (!getActivity().getResources().getBoolean(R.bool.is_tablet)) {
                mView.setBackgroundDrawable(mGradient);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("current", mCurrent);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDataUpdate(Forecast forecast) {
        if (getActivity() != null) {
            mCurrent = forecast.getCurrent();
            mGradient = forecast.getGradient(getActivity());
            updateViews();
        }
    }

}
