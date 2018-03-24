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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (!isTablet) {
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

        mRefreshImageView.setOnClickListener(v -> getSupportLoaderManager()
                .restartLoader(0, null, this));

       getSupportLoaderManager().initLoader(0, null, this);

        Log.d(TAG, "Main UI code is running!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DIRECTION, mCurrentDirection);
        super.onSaveInstanceState(outState);
    }

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
                alertUserAboutError();
            } else {
                Toast.makeText(this, getString(R.string.network_unavailable_message),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            mDataUpdate.onDataUpdate(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Forecast> loader) {

    }

    // Callback for fragments to request that the loader data be sent to them
    @Override
    public void fetchData() {
        getSupportLoaderManager().initLoader(0, null, this);
    }
}














