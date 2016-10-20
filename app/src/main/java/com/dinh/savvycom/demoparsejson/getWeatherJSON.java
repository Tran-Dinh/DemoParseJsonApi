package com.dinh.savvycom.demoparsejson;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 20/10/2016.
 */

public class getWeatherJSON {


    public final static String url = "http://api.openweathermap.org/data/2.5/weather?q=VietNam&units=metric";
    public final static String KEY_WEATHER_API = "57106af7695c063624e57449a15d7ff9";

    String lon, lat;
    Context context;
    boolean check;

    public getWeatherJSON(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public JSONObject getJSON() throws IOException, JSONException {
        String line = "";
        BufferedReader bufferedReader = null;
        StringBuilder jsonBuilder = new StringBuilder(4028);


        //read file json

        try {
            URL mUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.addRequestProperty("x-api-key",KEY_WEATHER_API);

            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while ((line = bufferedReader.readLine()) != null) {
                jsonBuilder.append(line).append("\n");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bufferedReader.close();
        }

        //create object json

        JSONObject data = new JSONObject(jsonBuilder.toString());

        if (data.getInt("cod") == 200) {
            Log.d("Main", "" + data);
            return data;
        } else {
            return null;
        }
    }

    /**
     * check connect internet
     *
     * @return
     */
    public boolean checkConnection() {
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            check = true;
        } else {
            check = false;
        }

        return check;
    }
}
