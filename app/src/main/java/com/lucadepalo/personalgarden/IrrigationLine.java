package com.lucadepalo.personalgarden;

import java.util.HashMap;

/**
 * Questa classe rappresenta una linea di irrigazione.
 * Una linea di irrigazione contiene diversi posti (vasi).
 */
public class IrrigationLine {

    private int numLine; // Numero identificativo della linea di irrigazione
    private HashMap<Integer, PlantPot> line; // Mappa dei vasi presenti nella linea, associati al loro numero di posizione

    /**
     * Costruttore della classe IrrigationLine.
     * Inizializza una nuova linea di irrigazione con il numero fornito.
     *
     * @param numLine Numero identificativo della linea di irrigazione.
     */
    public IrrigationLine(int numLine) {
        this.numLine = numLine;
        this.line = new HashMap<>();
    }

    /**
     * Restituisce il numero identificativo della linea di irrigazione.
     *
     * @return Numero identificativo della linea.
     */
    public int getNumLine() {
        return numLine;
    }

    /**
     * Aggiunge un vaso alla linea di irrigazione nella posizione specificata.
     *
     * @param numPlace Posizione del vaso nella linea.
     * @param pot Il vaso da aggiungere.
     */
    public void addPot(int numPlace, PlantPot pot) {
        this.line.put(numPlace, pot);
        pot.setNumPlace(numPlace);
    }

    /**
     * Restituisce il vaso in base alla sua posizione nella linea.
     *
     * @param numPlace Posizione del vaso.
     * @return Il vaso nella posizione specificata.
     */
    public PlantPot getPotByPlace(int numPlace) {
        return this.line.get(numPlace);
    }

    /**
     * Restituisce il vaso in base al suo ID.
     *
     * @param potID ID del vaso.
     * @return Il vaso con l'ID specificato.
     */
    public PlantPot getPotByID(int potID) {
        for(PlantPot pot : line.values()) {
            if(pot.getPotID() == potID) {
                return pot;
            }
        }
        return null;
    }

    /**
     * Restituisce tutti i vasi presenti nella linea di irrigazione.
     *
     * @return Un hashmap dei vasi associati al loro numero di posizione.
     */
    public HashMap<Integer, PlantPot> getAllPots() {
        return this.line;
    }

    /**
     * Restituisce la posizione di un vaso specifico nella linea.
     *
     * @param plantPot Il vaso di cui si vuole conoscere la posizione.
     * @return La posizione del vaso.
     */
    public int getPlaceByPot(PlantPot plantPot){
        return (int) getKeyByValue(line, plantPot);
    }

    /**
     * Sposta un vaso da una posizione all'altra nella linea.
     *
     * @param fromPlace Posizione di partenza del vaso.
     * @param toPlace Posizione di destinazione del vaso.
     */
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

    /**
     * Rimuove un vaso specifico dalla linea di irrigazione.
     *
     * @param plantPot Il vaso da rimuovere.
     */
    public void deleteThisPot(PlantPot plantPot) {
        if (this.line.containsValue(plantPot)) {
            this.line.remove((getKeyByValue(this.line, plantPot)));
        } else {
            throw new IllegalArgumentException("Vaso non presente in questa linea");
        }
    }

    /**
     * Rimuove un vaso dalla linea in base alla sua posizione.
     *
     * @param numPlace La posizione del vaso da rimuovere.
     */
    public void deletePotInPlace(int numPlace) {
        this.line.remove(numPlace);
    }

    /**
     * Scambia di posizione due vasi nella linea.
     *
     * @param numPlace1 Posizione del primo vaso.
     * @param numPlace2 Posizione del secondo vaso.
     */
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

    /**
     * Restituisce la chiave associata a un valore specifico in una mappa.
     *
     * @param map La mappa in cui cercare.
     * @param value Il valore di cui si vuole conoscere la chiave.
     * @return La chiave associata al valore.
     */
    private static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (HashMap.Entry<T, E> entry : map.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
