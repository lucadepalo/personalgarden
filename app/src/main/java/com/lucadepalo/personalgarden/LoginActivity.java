package com.lucadepalo.personalgarden;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Questa classe rappresenta l'attività di login dell'applicazione,
 * dove gli utenti possono accedere al loro account.
 */
public class LoginActivity extends AppCompatActivity {

    // Campi di testo per inserire username e password
    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inizializzazione dei campi di testo
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        // Listener per il pulsante di login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Avvia la procedura di login
                userLogin();
            }
        });

        // Listener per il testo di registrazione (per gli utenti che non hanno un account)
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Avvia l'attività di registrazione
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    /**
     * Questo metodo gestisce il login dell'utente presso il server.
     */
    private void userLogin() {
        // Ottiene i valori inseriti
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        // Validazione degli input
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Per favore inserisci il tuo username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Per favore inserisci la tua password");
            editTextPassword.requestFocus();
            return;
        }


        /**
         * Questa sottoclasse AsyncTask gestisce il login dell'utente presso il server.
         */
        class UserLogin extends AsyncTask<Void, Void, String> {

            // Barra di avanzamento per mostrare lo stato di login
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {
                    // Converte la risposta in un oggetto JSON
                    JSONObject obj = new JSONObject(s);

                    // Se non ci sono errori nella risposta
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        // Ottiene l'utente dalla risposta
                        JSONObject userJson = obj.getJSONObject("user");

                        // Crea un nuovo oggetto User
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("nome"),
                                userJson.getString("cognome")
                        );

                        // Salva l'utente nelle preferenze condivise
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        // Avvia l'attività di scansione QR
                        finish();
                        startActivity(new Intent(getApplicationContext(), QRcodeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Username o password non validi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                // Crea un oggetto RequestHandler per gestire la richiesta
                RequestHandler requestHandler = new RequestHandler();

                // Crea i parametri della richiesta
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                // Restituisce la risposta del server
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }
        // Se tutto è in ordine, avvia l'AsyncTask per il login
        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
