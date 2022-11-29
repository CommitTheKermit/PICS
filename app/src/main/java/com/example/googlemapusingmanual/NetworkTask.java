package com.example.googlemapusingmanual;

import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
            this.rainState = weather.func(11,20,this.maptab);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.maptab.rainState = this.rainState;

        return null;
    }
}