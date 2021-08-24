package com.moh.moclock.MoUri;

import android.content.Context;
import android.net.Uri;

import androidx.preference.PreferenceManager;

public class MoUri {

    public static Uri get(Context c,int sharedPrefString){
        String customUri = PreferenceManager.getDefaultSharedPreferences(c)
                .getString(c.getString(sharedPrefString),"");
        if(!customUri.isEmpty()){
            try{
                // check to see if the uri is working before returning it
                Uri u = Uri.parse(customUri);
                return u;
            }catch(Exception ignore){}
        }
        return null;
    }

}
