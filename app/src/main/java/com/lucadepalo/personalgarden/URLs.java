package com.lucadepalo.personalgarden;

/**
 * Questa classe contiene gli URL dei microservizi che vengono invocati dall'app.
 * Tutti gli URL sono costruiti a partire da un URL principale e sono seguiti dal nome del file PHP
 * corrispondente al servizio desiderato.
 */
public class URLs {
    /** URL principale del servizio. Viene utilizzato come base per costruire gli altri URL.
     *  Viene usato il protocollo https per una maggior sicurezza.
     *  Il sistema si avvale di un servizio di web hosting in modo da garantire un servizio continuo.
     */
    private static final String ROOT_URL = "https://personalgarden.000webhostapp.com/";

    // URL per il servizio di registrazione dell'utente nell'entità 'AZIENDA'.
    public static final String URL_REGISTER = ROOT_URL + "signup.php";

    // URL per il servizio di login utente.
    public static final String URL_LOGIN = ROOT_URL + "login.php";

    // URL per il servizio relativo all'associazione dei QR code del sensore e dell'attuatore nella relazione 'controlla'.
    public static final String URL_QRCODE = ROOT_URL + "qrcode.php";

    // URL per ottenere l'elenco delle colture dall'entità  'SPECIE'.
    public static final String URL_CROPLIST = ROOT_URL + "croplist.php";

    // URL per ottenere l'elenco delle colture sinergiche dalla relazione 'sinergia' in base a quella selezionata.
    public static final String URL_SUGGEST = ROOT_URL + "suggest.php";

    // URL per memorizzare la linea nel contenitore nella relazione 'irriga'.
    public static final String URL_IRRIGA = ROOT_URL + "irriga.php";

    // URL per memorizzare i posti nella linea nella relazione 'dispone'.
    public static final String URL_DISPONE = ROOT_URL + "dispone.php";

    // URL per memorizzare le specie nei posti nella relazione 'assegnata'.
    public static final String URL_ASSEGNATA = ROOT_URL + "assegnata.php";

    // URL per impostare lo stato corrente del sistema nell'entità 'STATO'.
    public static final String URL_STATO = ROOT_URL + "stato.php";
}
