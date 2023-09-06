package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

/**
 * Questa classe rappresenta l'attività di scansione del codice QR nell'applicazione.
 */
public class QRcodeActivity extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    TextView messageText;
    ImageView gifImageView;
    String temp = "", qrSUT = "", qrAIRR = "", prefS = "SUT", prefA = "AIRR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Se l'utente ha già accoppiato i codici, avvia l'attività della griglia
        if (SharedPrefManager.getInstance(this).isCoupled()) {
            finish();
            startActivity(new Intent(this, GridActivity.class));
            return;
        }

        // Inizializzazione dei componenti dell'interfaccia utente
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        gifImageView = findViewById(R.id.gifImageView);

        // Imposta un listener sul pulsante di scansione
        scanBtn.setOnClickListener(this);

        // Carica l'immagine GIF nella vista dell'immagine
        Glide.with(this).load(R.drawable.scan).into(gifImageView);
    }

    @Override
    public void onClick(View v) {
        // Inizializza lo scanner QR
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Inquadra il codice QR");
        intentIntegrator.setOrientationLocked(true);
        // Controlla che il codice scansionato sia un QR e non un altro formato come codici a barre
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Operazione annullata", Toast.LENGTH_SHORT).show();
            } else {
                messageText.setText(intentResult.getContents());
                temp = messageText.getText().toString().trim();

                /** Gestione dei codici QR scansionati
                 *  Questo fa in modo che l'app si accorga di quale codice viene scansionato
                 *  automaticamente in modo da rendere ininfluente l'ordine che l'utente sceglie
                 */
                if (qrSUT.isEmpty() && temp.startsWith(prefS)) {
                    qrSUT = temp;
                } else if (qrAIRR.isEmpty() && temp.startsWith(prefA)) {
                    qrAIRR = temp;
                }

                // Se entrambi i codici QR sono stati scansionati, registra i codici
                if (!qrSUT.isEmpty() && !qrAIRR.isEmpty()) {
                    registerCodes();
                    startActivity(new Intent(getApplicationContext(), GridActivity.class));
                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Questo metodo gestisce la registrazione dei codici QR scansionati.
     */
    private void registerCodes() {
        final String SUT = qrSUT;
        final String AIRR = qrAIRR;

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
                SharedPrefManager.getInstance(getApplicationContext()).setFK(AIRR);
                SharedPrefManager.getInstance(getApplicationContext()).qrRegistration(node);
            }
        }
        RegisterCodes rc = new RegisterCodes();
        rc.execute();
    }
}
