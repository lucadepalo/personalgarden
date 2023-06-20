package com.lucadepalo.personalgarden;

import java.util.HashMap;

public class PlantPot {

    private int numPlace, cropID;
    //private String crop;


    public PlantPot() {
    }

    public void setNumPlace(int numPlace) {
        this.numPlace = numPlace;
    }

    public int getNumPLace() {
        return numPlace;
    }

    /*
    public void setCrop(String crop){
        this.crop = crop;
    }

    public String getCrop() {
        return crop;
    }
    */

    public void setCropID(int cropID) {
        this.cropID = cropID;
    }

    public int getCropID() {
        return cropID;
    }


}
