package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SetCropActivity extends AppCompatActivity {

    private HashMap<Integer, String> cropList = new HashMap<>();
    protected int cropInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_crop);

        new GetAllCropListTask().execute(URLs.URL_CROPLIST);
    }

    private class GetAllCropListTask extends AsyncTask<String, Void, HashMap<Integer, String>> {
        @Override
        protected HashMap<Integer, String> doInBackground(String... urls) {
            HashMap<Integer, String> result = null;
            try {
                HashMap<String, String> postDataParams = new HashMap<>();
                RequestHandler requestHandler = new RequestHandler();
                String json = requestHandler.sendPostRequest(urls[0], postDataParams);
                result = RequestHandler.parseJsonToHashMap(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, String> result) {
            if (result != null && !result.isEmpty()) {
                cropList.clear();
                cropList.putAll(result);
                populateSpinner();
            } else {
                Toast.makeText(SetCropActivity.this, "Nessuna specie disponibile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateSpinner() {
        Spinner spinner = findViewById(R.id.crop_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(cropList.values()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedCrop = parent.getItemAtPosition(position).toString();
                    Toast.makeText(SetCropActivity.this, "Hai selezionato: " + selectedCrop, Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent();
                    cropInt = getKeyByValue(cropList, selectedCrop);
                    returnIntent.putExtra("cropInt", cropInt);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SetCropActivity.this, "Devi selezionare una specie!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (HashMap.Entry<T, E> entry : map.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
