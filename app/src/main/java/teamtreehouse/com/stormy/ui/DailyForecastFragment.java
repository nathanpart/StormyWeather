package teamtreehouse.com.stormy.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.adapters.DayAdapter;
import teamtreehouse.com.stormy.weather.Day;
import teamtreehouse.com.stormy.weather.Forecast;

// The daily forecast List View
public class DailyForecastFragment extends Fragment implements DataUpdate {

    private Day[] mDays;

    private ListView mListView;
    private TextView mEmptyTextView;
    private DayAdapter mAdapter;
    private View mView;

    public DailyForecastFragment() {
        // Required empty public constructor
    }

    public static DailyForecastFragment newInstance() {
        DailyForecastFragment fragment = new DailyForecastFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_daily_forecast, container, false);

        if (savedInstanceState != null) {
            mDays = (Day[]) savedInstanceState.getParcelableArray("day");
        }

        mListView = mView.findViewById(android.R.id.list);
        mEmptyTextView = mView.findViewById(android.R.id.empty);

        mAdapter = new DayAdapter(getActivity(), mDays);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyTextView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String highTemp = mDays[position].getTemperatureMax() + "";
                String message = String.format("On %s the high will be %s and it will be %s",
                        dayOfTheWeek,
                        highTemp,
                        conditions);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });

        ((GetData) getActivity()).fetchData();
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray("day", mDays);
        super.onSaveInstanceState(outState);
    }

    // Handle new data by sending it the adapter
    @Override
    public void onDataUpdate(Forecast forecast) {
        mDays = forecast.getDailyForecast();
        if (mView != null) mView.setBackgroundDrawable(forecast.getGradient(getActivity()));
        if (mAdapter != null) mAdapter.updateData(mDays);
    }
}
