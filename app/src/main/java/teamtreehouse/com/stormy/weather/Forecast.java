package teamtreehouse.com.stormy.weather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import teamtreehouse.com.stormy.R;

/**
 * Created by benjakuben on 2/5/15.
 * Update by partridgenathan on 3/23/18 to implement parcleble interface and to fetch the
 * proper gradient to show.
 */

public class Forecast implements Parcelable {
    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;


    public Forecast() {
        // Public default constructor
    }

    protected Forecast(Parcel in) {
        mCurrent = in.readParcelable(Current.class.getClassLoader());
        mHourlyForecast = in.createTypedArray(Hour.CREATOR);
        mDailyForecast = in.createTypedArray(Day.CREATOR);
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel in) {
            return new Forecast(in);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public static int getIconId(String iconString) {
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
        int iconId = R.drawable.clear_day;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;

    }

    // Get gradient drawable from resources depending on current temperature
    public Drawable getGradient(Context context) {
        int temp = mCurrent.getTemperature();

        // scale the temperature into 7 that are 20deg apart
        int scaledTemp = (temp + 20) / 20;   // Each gradient covers 20deg range staring at -20
        if (scaledTemp > 6) scaledTemp = 6;  // Top temp handle every thing above
        if (scaledTemp < 0) scaledTemp = 0;  // Bottom temp handles everything below

        return context.getResources().obtainTypedArray(R.array.gradient_list).getDrawable(scaledTemp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mCurrent, flags);
        dest.writeTypedArray(mHourlyForecast, flags);
        dest.writeTypedArray(mDailyForecast, flags);
    }
}
