package com.moh.moclock.MoUI;

import android.widget.TextView;

public class MoTextView {


    public static void syncColor(int color, TextView... tvs){
        for(TextView t: tvs){
            t.setTextColor(color);
        }
    }

}
