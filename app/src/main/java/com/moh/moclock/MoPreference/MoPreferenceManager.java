package com.moh.moclock.MoPreference;

import android.content.SharedPreferences;

import java.util.HashMap;

public class MoPreferenceManager {


    private HashMap<String,MoPreference> map = new HashMap<>();



    public MoPreferenceManager add(MoPreference pref){
        map.put(pref.getKey(),pref);
        return this;
    }


    @SuppressWarnings("ConstantConditions")
    public void update(SharedPreferences sp, String key){
        if(map.containsKey(key)){
            map.get(key).updateSummary(sp.getAll().get(key));
        }
    }


}
