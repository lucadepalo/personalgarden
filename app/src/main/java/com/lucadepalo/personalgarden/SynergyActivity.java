package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
/**
 * SynergyActivity permette agli utenti di selezionare una specie di pianta da un elenco
 * e associarla a un determinato vaso nel sistema. L'elenco presenta solo le specie in sinergia
 * con la specie selezionata nel posto precedente.
 */
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
            Toast.makeText(getBaseContext(), "ERROR INTENT POT ID", Toast.LENGTH_SHORT);
        }

        new GetSynergyCropListTask().execute(URLs.URL_SUGGEST);
        Spinner spinner = findViewById(R.id.syn_crop_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(synCropList.values()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * AsyncTask che richiede la lista delle specie di piante consociabili dal server inviando
     * come parametro la chiave esterna della pianta di cui si vuole trovare la lista di sinergie.
     */
    class GetSynergyCropListTask extends AsyncTask<String, Void, HashMap<Integer, String>> {
        @Override
        protected HashMap<Integer, String> doInBackground(String... urls) {
            HashMap<Integer, String> result = null;
            try {
                HashMap<String, String> postDataParams = new HashMap<>();
                postDataParams.put("fk_specie1", String.valueOf(SharedPrefManager.irrigationLine.getPotByID(potID - 1).getCropID()));
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
                synCropList.clear();
                synCropList.putAll(result);
                populateSpinner();
            } else {
                Toast.makeText(SynergyActivity.this, "Nessuna specie disponibile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Popola lo spinner con l'elenco delle specie ricevute dal server.
     * Questo metodo ordina l'elenco delle specie in ordine alfabetico
     * e imposta l'elemento selezionato inizialmente come "Seleziona...".
     */
    private void populateSpinner() {
        Spinner spinner = findViewById(R.id.syn_crop_spinner);
        ArrayList<String> dataList = new ArrayList<>(synCropList.values());
        Collections.sort(dataList);
        dataList.add(0, topElement);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, dataList);
        spinner.setAdapter(adapter);

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
                    int cropInt = getKeyByValue(synCropList, selectedCrop);
                    isItemSelected = true;
                    buttonConfirm.setEnabled(true);
                    findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPrefManager.irrigationLine.getPotByID(potID).setCropID(cropInt);
                            String imageName = selectedCrop.toLowerCase().replace(" ", "_");
                            int imageResId = getBaseContext().getResources().getIdentifier(imageName, "drawable", getBaseContext().getPackageName());
                            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageResId);
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 100, 100, true);
                            SharedPrefManager.irrigationLine.getPotByID(potID).setImageBitmap(resizedBitmap);
                            if (originalBitmap != resizedBitmap)
                                originalBitmap.recycle();
                            assegnata(SharedPrefManager.irrigationLine.getPotByID(potID),cropInt);
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

    /**
     * Restituisce la chiave di una determinata entry in una HashMap data la sua valore.
     * @param map la HashMap da cercare
     * @param value il valore dell'entry da cercare
     * @return la chiave dell'entry, oppure null se il valore non viene trovato
     */
    public static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (HashMap.Entry<T, E> entry : map.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Associa una specie di pianta a un vaso nel sistema. Questo metodo
     * invia una richiesta al server per associare un ID di specie specificato
     * a un vaso specificato nel sistema.
     *
     * @param plantPot il vaso a cui associare la specie
     * @param fk_specie l'ID della specie da associare
     */
    private void assegnata(PlantPot plantPot, int fk_specie) {
        final String FK_POSTO = ((Integer) plantPot.getNumPlace()).toString();
        final String FK_SPECIE = ((Integer) fk_specie).toString();
        class RelazioneAssegnata extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("fk_posto", FK_POSTO);
                params.put("fk_specie", FK_SPECIE);

                return requestHandler.sendPostRequest(URLs.URL_ASSEGNATA, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                SharedPrefManager.getInstance(getApplicationContext()).setPotInLine(SharedPrefManager.irrigationLine, plantPot);
            }
        }
        RelazioneAssegnata rd = new RelazioneAssegnata();
        rd.execute();
    }
}