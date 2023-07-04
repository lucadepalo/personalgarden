package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SynergyActivity extends AppCompatActivity {

    private HashMap<Integer, String> synCropList = new HashMap<>();
    private boolean isItemSelected = false;
    private int potID;
    private String topElement = "Seleziona...";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy);

        if (getIntent() != null && getIntent().hasExtra("potID")) {
            potID = getIntent().getIntExtra("potID", -1);
        } else {
        }

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
            try {
                HashMap<String, String> postDataParams = new HashMap<>();
                postDataParams.put("fk_specie1", String.valueOf(SharedPrefManager.irrigationLine.getPotByID(potID-1).getCropID()));
                RequestHandler requestHandler = new RequestHandler();
                String json = requestHandler.sendPostRequest(urls[0], postDataParams);
                result = RequestHandler.parseJsonToHashMap(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
            /*
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
        }*/

        @Override
        protected void onPostExecute(HashMap<Integer, String> result) {
            if (result != null && !result.isEmpty()) {
                synCropList.clear();
                synCropList.putAll(result);
                populateSpinner();
            } else {
                Toast.makeText(SynergyActivity.this, "Nessuna specie disponibile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateSpinner() {
        Spinner spinner = findViewById(R.id.syn_crop_spinner);
        ArrayList<String> dataList = new ArrayList<>(synCropList.values());
        dataList.add(0, topElement);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Button buttonConfirm = findViewById(R.id.button_confirm);
        if (buttonConfirm != null) {
            buttonConfirm.setEnabled(false);
        } else {
            Toast.makeText(SynergyActivity.this, "DEBUG_MODE: ERROR_NULL_BUTTON:", Toast.LENGTH_SHORT).show();
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    String selectedCrop = parent.getItemAtPosition(position).toString();
                    Toast.makeText(SynergyActivity.this, "Hai selezionato: " + selectedCrop, Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    int cropInt = getKeyByValue(synCropList, selectedCrop);
                    returnIntent.putExtra("cropInt", cropInt);
                    setResult(Activity.RESULT_OK, returnIntent);
                    isItemSelected = true;
                    buttonConfirm.setEnabled(true);
                    findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPrefManager.irrigationLine.getPotByID(potID).setCropID(cropInt);

                            finish();
                        }
                    });
                } else {
                    isItemSelected = false;
                    buttonConfirm.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SynergyActivity.this, "Devi selezionare una specie!", Toast.LENGTH_SHORT).show();
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
