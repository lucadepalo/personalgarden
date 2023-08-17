package com.lucadepalo.personalgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
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
    public static Container container = new Container(0);
    public static IrrigationLine irrigationLine = new IrrigationLine(0);


    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
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

    public void qrRegistration(Node node) { //relazione "controlla"
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AIRR, node.getAirr());
        editor.putString(KEY_SUT, node.getSut());
        editor.apply();
    }

    public void setLineInContainer(Container container, IrrigationLine irrigationLine) { //relazione irriga
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FK_CONTENITORE, container.getNumContainer());
        editor.putInt(FK_LINEA, irrigationLine.getNumLine());
        editor.apply();
    }

    public void setPotInLine(IrrigationLine irrigationLine, PlantPot plantPot) { //relazione "dispone"
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FK_LINEA, irrigationLine.getNumLine());
        editor.putInt(FK_POSTO, plantPot.getNumPlace());
        editor.apply();
    }


    public void setCropInPot(PlantPot plantPot) { //relazione "assegnata"
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FK_POSTO, plantPot.getNumPlace());
        editor.putInt(FK_SPECIE, plantPot.getCropID());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public boolean isCoupled() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(KEY_AIRR, null) != null) && (sharedPreferences.getString(KEY_SUT, null) != null);
    }

    //this method will give the logged in user
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

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

    public boolean haveTipsBeenShown() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_TIPS_SHOWN, false);
    }

    public void markTipsAsShown() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_TIPS_SHOWN, true);
        editor.apply();
    }

    public void setFK(String fk) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FK_NODO_IOT, fk);
        editor.apply();
    }

    public String getFK() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FK_NODO_IOT, FK_NODO_IOT);
    }
}
