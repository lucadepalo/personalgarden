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
 * Questa classe rappresenta l'attività di registrazione dell'applicazione,
 * dove gli utenti possono creare un nuovo account.
 */
public class RegisterActivity extends AppCompatActivity {

    // Definizione degli attributi per i campi di testo
    EditText editTextUsername, editTextEmail, editTextPassword, editTextNome, editTextCognome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Controllo per verificare se l'utente è già loggato per poter iniziare l'attività di scansione dei QR
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, QRcodeActivity.class));
            return;
        }
        // Collegamento degli attributi con i campi di testo editabili nella UI
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextNome = (EditText) findViewById(R.id.editTextNome);
        editTextCognome = (EditText) findViewById(R.id.editTextCognome);

        // Listener per il pulsante di registrazione
        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //al click dell'utente si avvia la registrazione sul server
                registerUser();
            }
        });

        // Listener per il testo di login
        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //al click dell'utente si abbandona la registrazione e si passa al login
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }
    /**
     * Metodo che gestisce la registrazione dell'utente.
     */
    private void registerUser() {
        //i campi raccolti nelle caselle di testo sono ripuliti e resi final
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String nome = editTextNome.getText().toString().trim();
        final String cognome = editTextCognome.getText().toString().trim();

        //validazione degli input nei campi di testo
        //in caso di errore esso viene comunicato all'utente per essere corretto

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Per favore inserisci un username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Per favore inserisci un indirizzo email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Per favore inserisci un indirizzo email valido");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Per favore inserisci una password");
            editTextPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nome)) {
            editTextNome.setError("Per favore inserisci il tuo nome");
            editTextNome.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(cognome)) {
            editTextCognome.setError("Per favore inserisci il tuo cognome");
            editTextCognome.requestFocus();
            return;
        }

        //se tutti i campi sono validi si procede con la registrazione

        /**
         * Sottoclasse AsyncTask per gestire la registrazione sul server.
         */
        class RegisterUser extends AsyncTask<Void, Void, String> {

            // Barra di avanzamento per indicare il progresso della registrazione
            //data la velocità di esecuzione in realtà è visibile solo se qualcosa è andato storto e rimane in attesa
            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                // Creazione di un oggetto RequestHandler
                RequestHandler requestHandler = new RequestHandler();

                //Creazione dei parametri della richiesta
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("nome", nome);
                params.put("cognome", cognome);

                //Invio della richiesta e ritorno della risposta
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            //prima dell'esecuzione la progressBar viene resa visibile
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //dopo l'esecuzione la progressBar viene nascosta
                progressBar.setVisibility(View.GONE);
                try {
                    //conversione della risposta in oggetto JSON
                    JSONObject obj = new JSONObject(s);

                    //se non c'è alcun errore nella risposta
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //estrae l'oggetto user dalla risposta
                        JSONObject userJson = obj.getJSONObject("user");

                        //crea un nuovo oggetto user
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("nome"),
                                userJson.getString("cognome")
                        );

                        //memorizza l'user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //inizia la QRscanActivity
                        finish();
                        startActivity(new Intent(getApplicationContext(), QRcodeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * esegue il task asincrono
         * bisogna usare un task asincrono per svincolare le operazioni con il server
         * dalle operazioni dell'activity in modo da non alterare la corretta esecuzione
         * */
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

}