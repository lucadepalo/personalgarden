package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SynergyActivity extends AppCompatActivity {

    private HashMap<Integer, String> synCropList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy);

        new GetSynergyCropListTask().execute(URLs.URL_SUGGEST);

        Spinner spinner = findViewById(R.id.syn_crop_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(synCropList.values()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    class GetSynergyCropListTask extends AsyncTask<String, Void, HashMap<Integer, String>> {
        @Override
        protected HashMap<Integer, String> doInBackground(String... urls) {
            HashMap<Integer, String> result = null;
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            //params.put("fk_specie1", fk_specie1);

            try {
                String json = RequestHandler.sendGetRequest(urls[0]);
                result = RequestHandler.parseJsonToHashMap(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, String> result) {
            if (result != null) {
                synCropList = result;
            } else {
                Toast.makeText(getBaseContext(), "Errore nell'elaborazione della richiesta", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
