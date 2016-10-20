package com.dinh.savvycom.demoparsejson;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static com.dinh.savvycom.demoparsejson.WeatherIcon.setWeatherIcon;

/**
 * Created by admin on 20/10/2016.
 */

public class AsyncWeather extends AsyncTask<String, Void, JSONObject> {

    //Call back interface

    public AsyncResponse delegate = null;

    public AsyncWeather(AsyncResponse asyncResponse) {

        //Assigning call back interfacethrough constructor

        delegate = asyncResponse;
    }

    /**
     *  method getJSON in doInBackground
     * @param params
     * @return
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject jsonWeather = null;
        try {
            jsonWeather = new getWeatherJSON(params[0], params[1]).getJSON();
        } catch (Exception e) {
            Log.d("Error", "Cannot process JSON results", e);
        }
        return jsonWeather;
    }

    /**
     * create object json to String in onPostExcute
     * @param jsonObject
     */

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        if (jsonObject != null) {
            try {
                JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonObject.getJSONObject("main");
                DateFormat df = DateFormat.getDateInstance();

                String city = jsonObject.getString("name").toUpperCase() + jsonObject.getJSONObject("sys").getString("country");
                String description = details.getString("description").toUpperCase(Locale.US);
                String temperature = String.format("%.2f", main.getDouble("temp")) + "°";
                String humidity = main.getString("humidity") + "%";
                String pressure = main.getString("pressure") + " hPa";
                String updatedOn = df.format(new Date(jsonObject.getLong("dt") * 1000));
                String iconText = setWeatherIcon(details.getInt("id"),
                        jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                        jsonObject.getJSONObject("sys").getLong("sunset") * 1000);

                delegate.processFinish(city, description, temperature, humidity, pressure, updatedOn, iconText, "" + (jsonObject.getJSONObject("sys").getLong("sunrise") * 1000));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * Create interface AsyncRespone
 * interface do method processFisnish stransittion parameter for activity
 */

interface AsyncResponse {

    void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8);
}


