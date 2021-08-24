package com.moh.moclock.MoGoogleSearch.MoGoogle;

public class MoHtmlHelper {

    public static final String TYPE_SEPARATOR = "<h2 class=\"bNg8Rb\">";
    public static final String TYPE_UNTIL = "<";

    public static final String APOS = "&#39;";
    public static final String QUOTE = "&quot;";


    public static String[] splitTypes(String webResults){
        return webResults.split(TYPE_SEPARATOR);
    }






}
