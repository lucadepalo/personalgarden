package com.lucadepalo.personalgarden;

import java.util.HashMap;

public class PlantPot {

    private int numPlace, cropID;
    private HashMap<Integer, String> cropList;

    public PlantPot(int numPlace, int cropID) {
        this.numPlace = numPlace;
        this.cropID = cropID;
    }

    public int getNumPLace() {
        return numPlace;
    }

    public int getCropID() {
        return cropID;
    }

    public HashMap<Integer, String> getCropList() {
        String json = RequestHandler.sendGetRequest(URLs.URL_CROPLIST);
        HashMap<Integer, String> cropList = RequestHandler.parseJsonToHashMap(json);
        return cropList;
    }
}
