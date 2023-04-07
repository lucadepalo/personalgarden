package com.lucadepalo.personalgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

public class QRcodeActivity extends AppCompatActivity implements View.OnClickListener{
    Button scanBtn;
    TextView messageText;
    String temp = "", qrSUT = "", qrAIRR = "", prefS = "SUT", prefA = "AIRR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        if (SharedPrefManager.getInstance(this).isCoupled()) {
            finish();
            startActivity(new Intent(this, DragDropActivity.class));
            return;
        }

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
        // if the intentResult is null then toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Operazione annullata", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null this sets the content and format of scan message
                messageText.setText(intentResult.getContents());
                temp = messageText.getText().toString().trim();
                // if the SUT qr code hasn't already been scanned AND if the code scanned starts with SUT
                if (qrSUT.isEmpty() && temp.startsWith(prefS)) {
                    qrSUT = temp;
                    // if the AIRR qr code hasn't already been scanned AND if the code scanned starts with AIRR
                } else if (qrAIRR.isEmpty() && temp.startsWith(prefA)) {
                    qrAIRR = temp;
                }
                if(!qrSUT.isEmpty()&&!qrAIRR.isEmpty()){
                    registerCodes();
                    //this calls the next activity that needs to run after everything's done in this
                    startActivity(new Intent(getApplicationContext(), DragDropActivity.class));
                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                Node node = new Node(AIRR, SUT);
                SharedPrefManager.getInstance(getApplicationContext()).qrRegistration(node);
            }
        }
        RegisterCodes rc = new RegisterCodes();
        rc.execute();
    }
}