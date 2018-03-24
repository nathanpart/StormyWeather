package teamtreehouse.com.stormy.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.adapters.HourAdapter;
import teamtreehouse.com.stormy.weather.Forecast;
import teamtreehouse.com.stormy.weather.Hour;

public class HourlyForecastFragment extends Fragment implements DataUpdate {

    private Hour[] mHours;

    private RecyclerView mRecyclerView;
    private HourAdapter mAdapter;


    public HourlyForecastFragment() {
        // Required empty public constructor
    }

    public static HourlyForecastFragment newInstance() {
        HourlyForecastFragment fragment = new HourlyForecastFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hourly_forecast, container, false);

        if (savedInstanceState != null) {
            mHours = (Hour[]) savedInstanceState.getParcelableArray("hours");
        }

        mRecyclerView = view.findViewById(R.id.reyclerView);

        mAdapter = new HourAdapter(getActivity(), mHours);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        ((GetData) getActivity()).fetchData();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray("hours", mHours);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDataUpdate(Forecast forecast) {
        mHours = forecast.getHourlyForecast();
        if (mAdapter != null) mAdapter.updateData(mHours);
    }
}
