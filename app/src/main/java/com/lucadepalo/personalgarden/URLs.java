package com.lucadepalo.personalgarden;
//questa classe raccoglie gli URL dei microservizi che vengono invocati dall'app
public class URLs {
    //URL principale a cui va concatenato il nome del file php di cui si intende sfruttare il servizio
    private static final String ROOT_URL = "https://personalgarden.000webhostapp.com/";
    public static final String URL_REGISTER = ROOT_URL + "signup.php";
    public static final String URL_LOGIN = ROOT_URL + "login.php";
    public static final String URL_QRCODE = ROOT_URL + "qrcode.php";
    public static final String URL_CROPLIST = ROOT_URL + "croplist.php";
    public static final String URL_SUGGEST = ROOT_URL + "suggest.php";
    public static final String URL_IRRIGA = ROOT_URL + "irriga.php";
    public static final String URL_DISPONE = ROOT_URL + "dispone.php";
    public static final String URL_ASSEGNATA = ROOT_URL + "assegnata.php";
    public static final String URL_STATO = ROOT_URL + "stato.php";
}