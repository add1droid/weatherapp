package com.example.user.weatherchallenge.Util;
import com.example.user.weatherchallenge.Model.Weather;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 11/11/2017.
 */

public class JsonParser {



        public static Weather getWeather(String data) throws JSONException {
            Weather weather = new Weather();


            // We create out JSONObject from the data
            JSONObject jObj = new JSONObject(data);

            // We start extracting the info


            JSONObject coordObj = getObject("coord", jObj);
            weather.city.setLat(getFloat("lat", coordObj));
            weather.city.setLon(getFloat("lon", coordObj));

            JSONObject sysObj = getObject("sys", jObj);
            weather.city.setSunrise(getInt("sunrise", sysObj));
            weather.city.setSunset(getInt("sunset", sysObj));
            weather.city.setCity(getString("name", jObj));
            //weather.location = loc;

            // We get weather info (This is an array)
            JSONArray jArr = jObj.getJSONArray("weather");

            // We use only the first value
            JSONObject JSONWeather = jArr.getJSONObject(0);
            weather.setId(getInt("id", JSONWeather));
            weather.setDescr(getString("description", JSONWeather));
            weather.setCondition(getString("main", JSONWeather));
            weather.setIcon(getString("icon", JSONWeather));

            JSONObject mainObj = getObject("main", jObj);
            weather.setHumidity(getInt("humidity", mainObj));
            weather.setPressure(getInt("pressure", mainObj));
            weather.temp.setMaxTemp(getFloat("temp_max", mainObj));
            weather.temp.setMinTemp(getFloat("temp_min", mainObj));
            weather.temp.setTemp(getFloat("temp", mainObj));

            // Wind
            JSONObject wObj = getObject("wind", jObj);
            weather.wind.setSpeed(getFloat("speed", wObj));
            weather.wind.setDeg(getFloat("deg", wObj));

            // Clouds
            JSONObject cObj = getObject("clouds", jObj);
            weather.clouds.perc = (getInt("all", cObj));




            return weather;
        }


        private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
            JSONObject subObj = jObj.getJSONObject(tagName);
            return subObj;
        }

        private static String getString(String tagName, JSONObject jObj) throws JSONException {
            return jObj.getString(tagName);
        }

        private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
            return (float) jObj.getDouble(tagName);
        }

        private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
            return jObj.getInt(tagName);
        }

    }


