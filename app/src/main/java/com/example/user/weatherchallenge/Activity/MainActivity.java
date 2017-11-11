package com.example.user.weatherchallenge.Activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.user.weatherchallenge.Model.Weather;
import com.example.user.weatherchallenge.R;
import com.example.user.weatherchallenge.Util.JsonParser;
import com.example.user.weatherchallenge.Webservice.WeatherClient;


import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {
    private EditText editTextCityName;
    private Button btnByCityName;
    private TextView textViewResult;
    private ImageView image;
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView hum;
    private TextView windSpeed;
    private TextView press;
    private TextView windDeg;
    static final int READ_BLOCK_SIZE = 100;
    private final String APP_ID = "1360d27636438ce9a9e2f57188e57106";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Initialize Views
        editTextCityName = findViewById(R.id.cityname);
        btnByCityName = findViewById(R.id.bycityname);
        image = findViewById(R.id.Icon);
        cityText = findViewById(R.id.cityText);
        condDescr = findViewById(R.id.condDescr);
        temp = findViewById(R.id.temp);
        hum = findViewById(R.id.hum);
        press = findViewById(R.id.press);
        windSpeed = findViewById(R.id.windSpeed);
        windDeg = findViewById(R.id.windDeg);

        //Write to and internal Read and Write to an internal file to retrieve last City name search
        String saveCityName = Read(editTextCityName);
        editTextCityName.setText(saveCityName);


        btnByCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //Concatenate City String and US to set in URL to be executed with AsyncTack
                String[] s = new String[]{editTextCityName.getText().toString().trim() + ",US" + "&appid=" + APP_ID};
                Write(editTextCityName.getText().toString().trim());
                Task task = new Task();
                task.execute(s);

            }
        });
    }


    //AsyncTask to execute the fetching and setting of data
    private class Task extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherClient()).getWeatherData(params[0]));
            //This try block evokes the JSON Parser agent to work with the model classes
            try {
                weather = JsonParser.getWeather(data);

                // This fetches  the image icon
                weather.iconData = ((new WeatherClient()).getImage(weather.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            //Sets the image icon
            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                image.setImageBitmap(img);
            }
            //populate the view with the corresponding Model data

            cityText.setText(weather.city.getCity() + "," + "US");
            condDescr.setText(weather.getCondition() + "(" + weather.getDescr() + ")");
            temp.setText("" + Math.round((weather.temp.getTemp() - 273.15)) + " deg C");
            hum.setText("" + weather.getHumidity() + "%");
            press.setText("" + weather.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "");

        }
    }

    //this method writes the city name to a "mytextfile.txt
    public void Write(String city) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(city);
            outputWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this method determines if the file exists or is empty if not it will read the the contents of the file
    public String Read(View v) {
        //reading text from file
        String s = "";
        File f = getFileStreamPath("mytextfile.txt");
        if (f.length() == 0) {
            return "";
        } else {
            try {
                FileInputStream fileIn = openFileInput("mytextfile.txt");
                InputStreamReader InputRead = new InputStreamReader(fileIn);

                char[] inputBuffer = new char[READ_BLOCK_SIZE];

                int charRead;

                while ((charRead = InputRead.read(inputBuffer)) > 0) {
                    // char to string conversion
                    String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                    s += readstring;
                }
                InputRead.close();



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }


}
/*
      //  ArrayList<HashMap<Integer, String>> location = new ArrayList<>();
       // location = loadJSONFromAsset();
    }

    public ArrayList<HashMap<Integer, String>> loadJSONFromAsset() {
        ArrayList<HashMap<Integer, String>> cityList = new ArrayList<>();
        String json = null;
        String jsonC = null;
        try {
            InputStream is = getAssets().open("city.list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonC = json.replaceAll("[\\t\\n\\r]+", "");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(jsonC.toString());
            JSONArray m_jArry = obj.getJSONArray(jsonC);

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String city_name = new String();
                Integer city_id = 0;
                city_id = jo_inside.getInt("id");
                city_name = jo_inside.getString("name");

                HashMap<Integer, String> city = new HashMap<>();
                city.put(city_id, city_name);

                //Add your values in your `ArrayList` as below:
                cityList.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityList;
    }
}*/
