package com.moh.moclock.MoSharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

public class MoSharedPref {

    private static Map<String,?> prefs = new HashMap<>();


    public static void loadAll(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        prefs = sp.getAll();
    }

    public static Object get(String key){
        return prefs.get(key);
    }

    /**
     * returns the shared pref of any type
     * by making it the type parameter dynamic
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T get(String key, T defaultValue){
        if(!prefs.containsKey(key)){
            return defaultValue;
        }
        return (T)prefs.get(key);
    }



}
