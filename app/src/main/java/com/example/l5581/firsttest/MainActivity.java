package com.example.l5581.firsttest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String vakioUrl = "https://www.veikkaus.fi/api/v1/sport-games/draws?game-names=SPORT";
    private String loginUrl = "https://www.veikkaus.fi/api/bff/v1/sessions";
    private String loginInfoUrl = "https://www.veikkaus.fi/api/v1/players/self/account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpAsyncTask task = new HttpAsyncTask();
        task.execute(vakioUrl);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            URL myURL;
            try {
                myURL = new URL(urls[0]);

                HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
                myURLConnection.setRequestMethod("GET");
                myURLConnection.setRequestProperty("X-ESA-API-Key", "ROBOT");
                myURLConnection.setRequestProperty("Content-Type", "application/json");
                myURLConnection.setRequestProperty("Accept", "application/json");

                InputStream inputStream;
                int responseCode = myURLConnection.getResponseCode();
                if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = myURLConnection.getInputStream();
                } else {
                    inputStream = myURLConnection.getErrorStream();
                    //Try again:
                    responseCode = myURLConnection.getResponseCode();
                    if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                        inputStream = myURLConnection.getInputStream();
                    }
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(result);
        }
    }

}
