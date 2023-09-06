package com.lucadepalo.personalgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * La classe SharedPrefManager gestisce le operazioni relative alle preferenze condivise dell'applicazione.
 * Utilizza il pattern Singleton per garantire che esista una sola istanza di questa classe.
 * Fornisce metodi per salvare e recuperare informazioni sull'utente, sui codici QR scansionati,
 * sulla disposizione delle piante e altre impostazioni dell'app.
 */
public class SharedPrefManager {

    // Costanti usate come chiavi nelle SharedPreferences
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_NOME = "keynome";
    private static final String KEY_COGNOME = "keycognome";
    private static final String KEY_ID = "keyid";
    private static final String KEY_AIRR = "keyairr";
    private static final String KEY_SUT = "keysut";
    private static final String FK_CONTENITORE = "fk_contenitore";
    private static final String FK_LINEA = "fk_linea";
    private static final String FK_POSTO = "fk_posto";
    private static final String FK_SPECIE = "fk_specie";
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String KEY_TIPS_SHOWN = "keyTipsShown";
    private static String FK_NODO_IOT = "";

    /**
     * Variabili membro statiche per contenitori e linee di irrigazione: in
     * questo caso sono prefissate per lo scopo di progetto (giardino personale)
     */
    public static Container container = new Container(0);
    public static IrrigationLine irrigationLine = new IrrigationLine(0);

    /**
     * Costruttore privato per prevenire l'istanziazione diretta e rispettare il singleton.
     */
    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    /**
     * Metodo per ottenere l'istanza corrente di SharedPrefManager.
     * @param context contesto dell'applicazione
     * @return istanza di SharedPrefManager
     */
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    /**
     * Salva le informazioni dell'utente nelle preferenze condivise dopo il login.
     * @param user oggetto User contenente le informazioni dell'utente
     */
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NOME, user.getNome());
        editor.putString(KEY_COGNOME, user.getCognome());
        editor.apply();
    }

    /**
     * Registra le informazioni dei codici QR nelle preferenze condivise.
     * Questo metodo rappresenta la relazione "controlla" tra l'utente e i nodi.
     * @param node oggetto Node che rappresenta il nodo QR
     */
    public void qrRegistration(Node node) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AIRR, node.getAirr());
        editor.putString(KEY_SUT, node.getSut());
        editor.apply();
    }

    /**
     * Metodo per impostare la linea di irrigazione in un contenitore.
     * Questo metodo rappresenta la relazione "irriga" tra il contenitore e la linea di irrigazione.
     */
    public void setLineInContainer(Container container, IrrigationLine irrigationLine) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FK_CONTENITORE, container.getNumContainer());
        editor.putInt(FK_LINEA, irrigationLine.getNumLine());
        editor.apply();
    }

    /**
     * Metodo per impostare un vaso di piante in una linea di irrigazione.
     * Questo metodo rappresenta la relazione "dispone" tra la linea di irrigazione e il vaso di piante.
     */
    public void setPotInLine(IrrigationLine irrigationLine, PlantPot plantPot) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FK_LINEA, irrigationLine.getNumLine());
        editor.putInt(FK_POSTO, plantPot.getNumPlace());
        editor.apply();
    }

    /**
     * Metodo per impostare una coltura in un vaso di piante.
     * Questo metodo rappresenta la relazione "assegnata" tra il vaso di piante e la coltura.
     */
    public void setCropInPot(PlantPot plantPot) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FK_POSTO, plantPot.getNumPlace());
        editor.putInt(FK_SPECIE, plantPot.getCropID());
        editor.apply();
    }

    /**
     * Controlla se l'utente è già loggato.
     * @return true se l'utente è loggato, false altrimenti
     */
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    /**
     * Controlla se i codici AIRR e SUT sono già accoppiati.
     * @return true se i codici sono accoppiati, false altrimenti
     */
    public boolean isCoupled() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(KEY_AIRR, null) != null) && (sharedPreferences.getString(KEY_SUT, null) != null);
    }

    /**
     * Ottiene l'utente loggato dalle preferenze condivise.
     * @return un oggetto User rappresentante l'utente loggato
     */
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_NOME, null),
                sharedPreferences.getString(KEY_COGNOME, null)
        );
    }

    /**
     * Effettua il logout, cancella le preferenze condivise e avvia l'attività di login.
     */
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

    /**
     * Verifica se i suggerimenti sono stati mostrati all'utente.
     * @return true se i suggerimenti sono stati mostrati, false altrimenti
     */
    public boolean haveTipsBeenShown() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_TIPS_SHOWN, false);
    }

    /**
     * Marca i suggerimenti come mostrati.
     */
    public void markTipsAsShown() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_TIPS_SHOWN, true);
        editor.apply();
    }

    /**
     * Imposta la chiave esterna per il nodo IoT.
     * @param fk la chiave esterna
     */
    public void setFK(String fk) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FK_NODO_IOT, fk);
        editor.apply();
    }

    /**
     * Ottiene la chiave esterna per il nodo IoT.
     * @return la chiave esterna
     */
    public String getFK() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FK_NODO_IOT, FK_NODO_IOT);
    }
}
