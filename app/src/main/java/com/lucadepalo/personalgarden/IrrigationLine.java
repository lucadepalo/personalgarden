package com.lucadepalo.personalgarden;

import java.io.Serializable;
import java.util.HashMap;

public class IrrigationLine {

    private int numLine;
    private HashMap<Integer, PlantPot> line;

    public IrrigationLine(int numLine) {
        this.numLine = numLine;
        this.line = new HashMap<>();
    }

    public int getNumLine() {
        return numLine;
    }

    public void addPot(int numPlace, PlantPot pot) {
        this.line.put(numPlace, pot);
        pot.setNumPlace(numPlace);
    }

    public PlantPot getPotByPlace(int numPlace) {
        return this.line.get(numPlace);
    }

    public PlantPot getPotByID(int potID) {
        for(PlantPot pot : line.values()) {
            if(pot.getPotID() == potID) {
                return pot;
            }
        }
        return null;
    }
    public HashMap<Integer, PlantPot> getAllPots() {
        return this.line;
    }

    public int getPlaceByPot(PlantPot plantPot){
        return (int) getKeyByValue(line, plantPot);
    }

    public void movePotToPlace(int fromPlace, int toPlace) {
        PlantPot plantPot = this.line.get((fromPlace));
        if (plantPot == null) {
            throw new IllegalArgumentException("Nessun vaso presente nel posto indicato");
        } else if (this.line.get(toPlace) == null) {
            this.line.put(toPlace, plantPot);
            deletePotInPlace(fromPlace);
        } else {
            swapPots(fromPlace, toPlace);
        }
    }

    public void deleteThisPot(PlantPot plantPot) {
        if (this.line.containsValue(plantPot)) {
            this.line.remove((getKeyByValue(this.line, plantPot)));
        } else {
            throw new IllegalArgumentException("Vaso non presente in questa linea");
        }
    }

    public void deletePotInPlace(int numPlace) {
        this.line.remove(numPlace);
    }
    private void swapPots(int numPlace1, int numPlace2) {
        PlantPot plantPot1 = this.line.get(numPlace1);
        PlantPot plantPot2 = this.line.get(numPlace2);

        if (plantPot1 == null || plantPot2 == null) {
            throw new IllegalArgumentException("Ci devono essere due vasi per poterli scambiare");
        }

        this.line.put(numPlace1, plantPot2);
        this.line.put(numPlace2, plantPot1);

        plantPot1.setNumPlace(numPlace2);
        plantPot2.setNumPlace(numPlace1);
    }

    private static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (HashMap.Entry<T, E> entry : map.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
