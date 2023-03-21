package com.lucadepalo.smartirrigator;

/*
import java.net.InetAddress;
import java.net.UnknownHostException;
 */
public class URLs {

    /*
    private static final String url = "lucadepalo.dynamic-dns.net";

    private static final InetAddress address;

    static {
        try {
            address = InetAddress.getByName(url);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String ip = address.getHostAddress();

    private static final String ROOT_URL = "http://" + ip + "/Api.php?apicall=";
     */
    private static final String ROOT_URL = "http://172.29.56.203/Api.php?apicall=";
    //private static final String ROOT_URL = "http://192.168.1.149/Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";

    public static final String URL_QRCODE = ROOT_URL + "qrcode";

    /*
    public URLs() throws UnknownHostException {
    }
    */
}