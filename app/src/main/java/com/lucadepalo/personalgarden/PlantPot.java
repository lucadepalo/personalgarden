package com.lucadepalo.personalgarden;

import androidx.appcompat.widget.AppCompatImageView;
import android.content.Context;

/**
 * Questa classe rappresenta il singolo vaso da inserire nella linea di irrigazione
 * e popolare con una specie selezionata fra quelle nel DB.
 *
 * La classe estende AppCompatImageView, in modo da ereditare le caratteristiche
 * utili a visualizzare l'immagine corrispondente alla pianta contenuta.
 */
public class PlantPot extends AppCompatImageView {
    private int numPlace;  // Numero che indica la posizione del vaso
    private int cropID = -1; // ID della specie inserita nel vaso. Il valore predefinito è -1, che indica che non c'è ancora nessuna pianta inserita.
    private int potID;     // ID univoco del vaso

    /**
     * Costruttore della classe PlantPot.
     * Inizializza un nuovo vaso per piante senza specificare attributi aggiuntivi.
     *
     * @param context Il contesto in cui l'AppCompatImageView viene visualizzato.
     */
    public PlantPot(Context context) {
        super(context);
    }

    /**
     * Imposta il numero che indica la posizione del vaso.
     *
     * @param numPlace Numero della posizione del vaso.
     */
    public void setNumPlace(int numPlace) {
        this.numPlace = numPlace;
    }

    /**
     * Restituisce il numero che indica la posizione del vaso.
     *
     * @return Numero della posizione del vaso.
     */
    public int getNumPlace() {
        return numPlace;
    }

    /**
     * Imposta l'ID della specie presente nel vaso.
     *
     * @param cropID ID della specie.
     */
    public void setCropID(int cropID) {
        this.cropID = cropID;
    }

    /**
     * Restituisce l'ID della specie presente nel vaso.
     *
     * @return ID della specie.
     */
    public int getCropID() {
        return cropID;
    }

    /**
     * Imposta l'ID univoco del vaso.
     *
     * @param potID ID univoco del vaso.
     */
    public void setPotID(int potID) {
        this.potID = potID;
    }

    /**
     * Restituisce l'ID univoco del vaso.
     *
     * @return ID univoco del vaso.
     */
    public int getPotID() {
        return potID;
    }
}
