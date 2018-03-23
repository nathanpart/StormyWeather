package teamtreehouse.com.stormy.ui;


import teamtreehouse.com.stormy.weather.Forecast;

public interface DataUpdate {
    void onDataUpdate(Forecast forecast);
}
