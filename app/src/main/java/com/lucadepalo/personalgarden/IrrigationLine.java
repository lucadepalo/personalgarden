package com.lucadepalo.personalgarden;

import java.util.HashMap;

public class IrrigationLine {

    private int numLine;
    private HashMap<Integer, PlantPot> pots;

    public IrrigationLine(int numLine) {
        this.numLine = numLine;
        this.pots = new HashMap<>();
    }

    public int getNumLine() {
        return numLine;
    }

    public void addPot(int numPlace, PlantPot pot) {
        this.pots.put(numPlace, pot);
    }

    public PlantPot getPot(int numPlace) {
        return this.pots.get(numPlace);
    }

    public HashMap<Integer, PlantPot> getAllPots() {
        return this.pots;
    }
}
