package com.example.googlemapusingmanual.Utils;

import android.os.AsyncTask;

import com.example.googlemapusingmanual.Tabs.mapTab;

import org.json.JSONException;

import java.io.IOException;

public class NetworkTask extends AsyncTask<Void, Void, Void> {

    mapTab maptab;
    int rainState;
    public NetworkTask(mapTab tab){
        this.maptab = tab;
    }

    @Override
    protected Void doInBackground(Void... params) {

        WeatherAPI weather = new WeatherAPI();
        try {
            this.rainState = weather.func(this.maptab);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.maptab.rainState = this.rainState;

        return null;
    }
}