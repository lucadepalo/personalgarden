package com.lucadepalo.personalgarden;

import java.util.HashMap;

/**
 * Questa classe rappresenta l'entità CONTENITORE.
 */
public class Container {
    private static int numContainer; // ID statico del contenitore
    private HashMap<Integer, IrrigationLine> lines; // Map delle linee di irrigazione associati ai loro ID

    /**
     * Costruttore della classe Container.
     * Inizializza l'ID del contenitore e crea una nuova hashmap per le linee.
     *
     * @param numContainer L'ID da assegnare al contenitore.
     */
    public Container(int numContainer) {
        this.numContainer = numContainer;
        this.lines = new HashMap<>();
    }

    /**
     * Restituisce l'ID del contenitore.
     *
     * @return ID del contenitore.
     */
    public static int getNumContainer() {
        return numContainer;
    }

    /**
     * Aggiunge una nuova linea di irrigazione al contenitore.
     *
     * @param irrigationLine La linea di irrigazione da aggiungere.
     */
    public void addIrrigationLine(IrrigationLine irrigationLine) {
        this.lines.put(numContainer, irrigationLine);
    }

    /**
     * Ottiene una linea di irrigazione dal contenitore in base al suo ID. (per possibili sviluppi futuri)
     *
     * @param numContainer L'ID della linea di irrigazione da recuperare.
     * @return La linea di irrigazione corrispondente all'ID.
     */
    public IrrigationLine getIrrigationLine(int numContainer) {
        return this.lines.get(numContainer);
    }

    /**
     * Restituisce tutte le linee di irrigazione presenti nel contenitore. (per possibili sviluppi futuri)
     *
     * @return Una hashmap contenente tutte le linee di irrigazione.
     */
    public HashMap<Integer, IrrigationLine> getAllIrrigationLines() {
        return this.lines;
    }
}
