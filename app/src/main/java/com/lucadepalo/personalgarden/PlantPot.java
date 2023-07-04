package com.lucadepalo.personalgarden;

import androidx.appcompat.widget.AppCompatImageView;
import android.content.Context;

import java.io.Serializable;

public class PlantPot extends AppCompatImageView {
    private int numPlace;
    private int cropID;
    private int potID;

    public PlantPot(Context context) {
        super(context);
    }

    public void setNumPlace(int numPlace) {
        this.numPlace = numPlace;
    }

    public int getNumPlace() {
        return numPlace;
    }

    public void setCropID(int cropID) {
        this.cropID = cropID;
    }

    public int getCropID() {
        return cropID;
    }

    public void setPotID(int potID) {
        this.potID = potID;
    }

    public int getPotID() {
        return potID;
    }
}
