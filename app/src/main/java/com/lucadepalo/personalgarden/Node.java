package com.lucadepalo.personalgarden;

/**
 * Questa classe rappresenta l'entità NODO_IOT.
 * AIRR indica l'ID dell' Attuatore per l'elettrovalvola di IRRigazione.
 * SUT indica l'ID del Sensore di Umidità del Terreno.
 */
public class Node {
    private String airr; // ID dell' Attuatore
    private String sut;  // Identificativo del Sensore

    /**
     * Costruttore della classe Node.
     * Inizializza gli ID dell' Attuatore e del Sensore.
     *
     * @param airr Valore iniziale per l'ID dell' Attuatore.
     * @param sut Valore iniziale per l'identificativo del Sensore.
     *            i valori verranno passati in input chiamando il costruttore in QRcodeActivity
     */
    public Node (String airr, String sut) {
        this.airr = airr;
        this.sut = sut;
    }

    /**
     * Restituisce l'ID dell' Attuatore.
     *
     * @return L'ID dell' Attuatore.
     */
    public String getAirr() {
        return airr;
    }

    /**
     * Restituisce l'ID del Sensore.
     *
     * @return L'ID del Sensore.
     */
    public String getSut() {
        return sut;
    }
}
