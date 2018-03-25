package teamtreehouse.com.stormy.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.loaders.WeatherLoader;
import teamtreehouse.com.stormy.weather.Forecast;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Forecast>, GetData {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    public static final String FORECAST_FRAGMENT = "forecast_fragment";
    public static final String DIRECTION = "DIRECTION";

    private ImageView mRefreshImageView;
    private ProgressBar mProgressBar;
    private DataUpdate mDataUpdate;

    private double mLatitude;
    private double mLongitude;

    private boolean mCurrentDirection;

    // Layout parent for main activity
    private RelativeLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLayout = findViewById(R.id.main);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);

        // If we are smaller than tablet size
        if (!isTablet) {
            // If device has been rotated isLandscape will not equal mCurrentDirection
            boolean isLandscape = getResources().getBoolean(R.bool.is_landscape);
            mCurrentDirection = (savedInstanceState == null) ? isLandscape : savedInstanceState.getBoolean(DIRECTION);

            PagerFragment savedFragment = (PagerFragment) getSupportFragmentManager()
                    .findFragmentByTag(FORECAST_FRAGMENT);
            if (savedFragment == null) {
                PagerFragment fragment = PagerFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.placeholder, fragment, FORECAST_FRAGMENT);
                fragmentTransaction.commit();
                mDataUpdate = fragment;
                mCurrentDirection = isLandscape;
            } else {
                mDataUpdate = savedFragment;

                // If a rotation has occurred, we reload the pager fragment, as it needs to
                // rebuild itself to take into consideration the new orientation
                if (mCurrentDirection != isLandscape) {
                    PagerFragment fragment = PagerFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.placeholder, fragment, FORECAST_FRAGMENT);
                    fragmentTransaction.commit();
                    mDataUpdate = fragment;
                    mCurrentDirection = isLandscape;
                }
            }

        // If tablet use the fragment that handles its layout
        } else {
            TabletFragment savedFragment = (TabletFragment) getSupportFragmentManager()
                    .findFragmentByTag(FORECAST_FRAGMENT);
            if (savedFragment == null) {
                TabletFragment fragment = TabletFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.placeholder, fragment, FORECAST_FRAGMENT);
                fragmentTransaction.commit();
                mDataUpdate = fragment;
            } else {
                mDataUpdate = savedFragment;
            }
        }

        mRefreshImageView = findViewById(R.id.refreshImageView);
        mProgressBar = findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.INVISIBLE);

        mLatitude = 37.8267;
        mLongitude = -122.423;

        // Refresh button just has loader restart and get new data.
        mRefreshImageView.setOnClickListener(v -> getSupportLoaderManager()
                .restartLoader(0, null, this));

       // Start loader
       getSupportLoaderManager().initLoader(0, null, this);

        Log.d(TAG, "Main UI code is running!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DIRECTION, mCurrentDirection);
        super.onSaveInstanceState(outState);
    }

    // Progress on/off switch - true on, false off
    private void setRefreshIndicator(boolean showProgressBar) {
        if (showProgressBar) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @Override
    public Loader<Forecast> onCreateLoader(int id, Bundle args) {
        setRefreshIndicator(true);
        return new WeatherLoader(this, mLatitude, mLongitude);
    }

    @Override
    public void onLoadFinished(Loader<Forecast> loader, Forecast data) {
        setRefreshIndicator(false);
        if (data == null) {
            if (loader instanceof WeatherLoader && ((WeatherLoader) loader).isNetworkUp()) {
                // If no data and network is up, then we had a communication problem
                alertUserAboutError();
            } else {
                // This case if network is just unavailable (e.g. airplane mode)
                Toast.makeText(this, getString(R.string.network_unavailable_message),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            // Set the background gradient for the current temperature and pass data to
            // hte fragments.
            mMainLayout.setBackgroundDrawable(data.getGradient(this));
            mDataUpdate.onDataUpdate(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Forecast> loader) {

    }

    // Callback for fragments to request that data be sent to them. Simple, we just let the loader
    // do its thing as it caches the weather data.
    @Override
    public void fetchData() {
        getSupportLoaderManager().initLoader(0, null, this);
    }
}














