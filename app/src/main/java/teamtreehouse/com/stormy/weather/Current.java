package teamtreehouse.com.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by benjakuben on 12/8/14.
 *
 * Update by partridgenathan on 3/23/18 to implement parcleble interface
 */
public class Current implements Parcelable{
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;


    public Current() {
        // public default constructor
    }

    protected Current(Parcel in) {
        mIcon = in.readString();
        mTime = in.readLong();
        mTemperature = in.readDouble();
        mHumidity = in.readDouble();
        mPrecipChance = in.readDouble();
        mSummary = in.readString();
        mTimeZone = in.readString();
    }

    public static final Creator<Current> CREATOR = new Creator<Current>() {
        @Override
        public Current createFromParcel(Parcel in) {
            return new Current(in);
        }

        @Override
        public Current[] newArray(int size) {
            return new Current[size];
        }
    };

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public long getTime() {
        return mTime;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int)Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        double precipPercentage = mPrecipChance * 100;
        return (int)Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIcon);
        dest.writeLong(mTime);
        dest.writeDouble(mTemperature);
        dest.writeDouble(mHumidity);
        dest.writeDouble(mPrecipChance);
        dest.writeString(mSummary);
        dest.writeString(mTimeZone);
    }
}
