package com.lucadepalo.smartirrigator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class QRcodeActivity extends AppCompatActivity implements View.OnClickListener{
    Button scanBtn;
    TextView messageText;
    String qrSUT = "", qrAIRR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        //messageFormat = findViewById(R.id.textFormat);

        // adding listener to the button
        scanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Inquadra il codice QR");
        intentIntegrator.setOrientationLocked(false);
        //this ensures that the user can scan only qr codes
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Operazione annullata", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                messageText.setText(intentResult.getContents());

                if (qrSUT.isEmpty()) {
                    qrSUT = messageText.getText().toString().trim();


                } else if (qrAIRR.isEmpty()) {
                    qrAIRR = messageText.getText().toString().trim();
                    if(!qrSUT.isEmpty()&&!qrAIRR.isEmpty()){
                        registerCodes();
                    }

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        /*if(!qrSUT.isEmpty()&&!qrAIRR.isEmpty()){
            findViewById(R.id.scanBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerCodes();
                }
            });
            //startActivity(new Intent(getApplicationContext(), NextActivity.class));
        }*/
    }

    private void registerCodes(){
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
            }
        }
        RegisterCodes rc = new RegisterCodes();
        rc.execute();
    }
}