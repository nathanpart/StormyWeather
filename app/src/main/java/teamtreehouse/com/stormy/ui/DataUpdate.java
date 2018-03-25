package teamtreehouse.com.stormy.ui;


import teamtreehouse.com.stormy.weather.Forecast;

// Interface that fragments implement to receive new data from the loader
public interface DataUpdate {
    void onDataUpdate(Forecast forecast);
}
