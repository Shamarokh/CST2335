package com.example.shama.androidlabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherForecast extends AppCompatActivity {
    protected static final String URL_STRING = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    protected static final String URL_IMAGE = "http://openweathermap.org/img/w/";

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private ImageView weatherImageView;
    private TextView currentTextView, minTextView, maxTextView;
    private ProgressBar normProgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_forecast);
        //setContentView(R.layout.content_weather_forecast);

        weatherImageView = (ImageView) findViewById(R.id.weatherImageView);
        currentTextView = (TextView) findViewById(R.id.currentTextView);
        minTextView = (TextView) findViewById(R.id.minTextView);
        maxTextView = (TextView) findViewById(R.id.maxTextView);


        normProgBar = (ProgressBar) findViewById(R.id.normProgBar);
     //   normProgBar.setVisibility(View.VISIBLE);
       // normProgBar.setMax(100);

        new ForecastQuery().execute(URL_STRING);
        Log.i(ACTIVITY_NAME,"In onCreate()");
    }
    protected static Bitmap getImage(URL url) {
        Log.i(ACTIVITY_NAME, "In getImage");
        HttpURLConnection iconConn = null;
        try {
            iconConn = (HttpURLConnection) url.openConnection();
            iconConn.connect();
            int response = iconConn.getResponseCode();
            if (response == 200) {
                return BitmapFactory.decodeStream(iconConn.getInputStream());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (iconConn != null) {
                iconConn.disconnect();
            }
        }
    }
    public boolean fileExistance(String fileName) {
        Log.i(ACTIVITY_NAME, "In fileExistance");
        Log.i(ACTIVITY_NAME, getBaseContext().getFileStreamPath(fileName).toString());
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }
    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        String min;
        String max;
        String current;
        String iconName;
        Bitmap icon;

        @Override
        protected String doInBackground(String... params) {
            Log.i(ACTIVITY_NAME, "In doInBackground");
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals("temperature")) {
                        current = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        min = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        max = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    }
                    if (parser.getName().equals("weather")) {
                        iconName = parser.getAttributeValue(null, "icon");
                        String iconFile = iconName+".png";
                        if (fileExistance(iconFile)) {
                            FileInputStream inputStream = null;
                            try {
                                inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(inputStream);
                            Log.i(ACTIVITY_NAME, "Image already exists");
                        } else {
                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            icon = getImage(iconUrl);
                            FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME, "Adding new image");
                        }
                        Log.i(ACTIVITY_NAME, "file name="+iconFile);
                        publishProgress(100);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            normProgBar.setVisibility(View.VISIBLE);
            normProgBar.setProgress(values[0]);
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");


        }
        //////////////////////////////////////////////////////////////


        @Override
        protected void onPostExecute(String result) {
            String degree = Character.toString((char) 0x00B0);
            currentTextView.setText(currentTextView.getText()+current+degree+"C");
            minTextView.setText(minTextView.getText()+min+degree+"C");
            maxTextView.setText(maxTextView.getText()+max+degree+"C");
            weatherImageView.setImageBitmap(icon);
            normProgBar.setVisibility(View.INVISIBLE);
        }



    }

}
