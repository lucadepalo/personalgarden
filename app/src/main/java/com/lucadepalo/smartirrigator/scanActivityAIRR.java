package com.lucadepalo.smartirrigator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class scanActivityAIRR extends AppCompatActivity implements View.OnClickListener{
    Button scanBtn;
    TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_airr);

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
        intentIntegrator.setPrompt("Inquadra il codice QR dell'attuatore");
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
                //messageFormat.setText(intentResult.getFormatName());
                startActivity(new Intent(getApplicationContext(), scanActivitySUT.class));
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}