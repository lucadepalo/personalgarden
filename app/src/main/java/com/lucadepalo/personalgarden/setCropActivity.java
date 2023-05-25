package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.HashMap;

public class setCropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_crop);
    }
/*
    private void getCrops() {

        class setCrops extends AsyncTask<Void, Void, String> {
            final int numLine =
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put
            }
        }
    }


    private void registerLine() {
        final String LINE = numLine;
        final String PLACE = numPlace;

        class RegisterCodes extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("qrSUT", SUT);
                params.put("qrAIRR", AIRR);

                return requestHandler.sendPostRequest(URLs.URL_QRCODE, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Node node = new Node(AIRR, SUT);
                SharedPrefManager.getInstance(getApplicationContext()).qrRegistration(node);
            }
        }
        RegisterCodes rc = new RegisterCodes();
        rc.execute();
    }

 */
}