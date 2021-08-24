package com.moh.moclock.MoGoogleSearch.Html;



//import org.jsoup.Connection;
//import org.jsoup.Jsoup;

//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;


public class MoHtml {




    public static Connection.Response doc;




    public static String getHtml(String url) throws IOException {
        //return "";
        doc = Jsoup.connect(url).execute();
        return doc.body();
    }



}
