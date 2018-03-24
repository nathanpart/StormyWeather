package teamtreehouse.com.stormy.ui;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.weather.Forecast;

public class PagerFragment extends Fragment implements DataUpdate {

    private Forecast mForecast;
    private CurrentForecastFragment mCurrentForecastFragment;
    private HourlyForecastFragment mHourlyForecastFragment;
    private DailyForecastFragment mDailyForecastFragment;
    private DualPaneFragment mDualPaneFragment;

    public PagerFragment() {
        // Required empty public constructor
    }

    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        boolean isLandscape = getActivity().getResources().getBoolean(R.bool.is_landscape);
        int numberOfPages = isLandscape ? 2 : 3;

        mCurrentForecastFragment = CurrentForecastFragment.newInstance();
        mHourlyForecastFragment = HourlyForecastFragment.newInstance();
        mDailyForecastFragment = DailyForecastFragment.newInstance();
        mDualPaneFragment = DualPaneFragment.newInstance();

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position % 3) {
                    case 0:
                        return mCurrentForecastFragment;

                    case 1:
                        return isLandscape ? mDualPaneFragment : mHourlyForecastFragment;

                    case 2:
                        // This should only be selected in portrait mode
                        return mDailyForecastFragment;
                }
                // We should never get here
                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:
                        return "Current Forecast";

                    case 1:
                        return isLandscape ? "Extended Forecast" : "Hourly Forecast";

                    case 2:
                        return "7 Day Forecast";
                }
                // We should not get here
                return "";
            }

            @Override
            public int getCount() {
                return numberOfPages;
            }
        });

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDataUpdate(Forecast forecast) {
        if (mCurrentForecastFragment != null) mCurrentForecastFragment.onDataUpdate(forecast);
        if (getActivity().getResources().getBoolean(R.bool.is_landscape)) {
            if (mDualPaneFragment != null) mDualPaneFragment.onDataUpdate(forecast);
        } else {
            if (mHourlyForecastFragment != null) mHourlyForecastFragment.onDataUpdate(forecast);
            if (mDailyForecastFragment != null) mDailyForecastFragment.onDataUpdate(forecast);
        }
    }

}
