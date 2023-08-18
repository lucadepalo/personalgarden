package com.lucadepalo.personalgarden;

public class URLs {
    //private static final String ROOT_URL = "http://lucadepalo.dynamic-dns.net/Api.php?apicall="; //DYNAMIC LOCAL DNS FOR MICROSERVICES
    //private static final String ROOT_URL = "https://personalgarden.000webhostapp.com/"; //WEB HOSTING FOR MICROSERVICES
    private static final String ROOT_URL = "http://192.168.1.149/"; //LOCAL IP ADDRESS FOR MICROSERVICES
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