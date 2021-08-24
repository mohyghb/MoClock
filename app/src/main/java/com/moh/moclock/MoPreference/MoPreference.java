package com.moh.moclock.MoPreference;

import androidx.preference.Preference;

import com.moh.moclock.MoString.MoString;

public class MoPreference {

    private static final int LIMIT_COUNT_CHAR_SUMMARY = 100;

    private String valueSummary;
    private String normalSummary;
    private boolean updateSummary;
    private Preference preference;


    public MoPreference(Preference p){
        this.preference = p;
    }

    public MoPreference setOnPreferenceClickListener(Preference.OnPreferenceClickListener listener){
        this.preference.setOnPreferenceClickListener(listener);
        return this;
    }


    public MoPreference setUpdateSummary(boolean updateSummary) {
        this.updateSummary = updateSummary;
        if(this.updateSummary){
            updateSummary(preference.getSharedPreferences().getString(preference.getKey(),""));
        }
        return this;
    }

    public void updateSummary(Object newValue) {
        if(newValue!= null && !newValue.toString().isEmpty()){
            // there is new value, update the description
            preference.setSummary(MoString.getLimitedCount(valueSummary !=null? valueSummary :newValue.toString(),LIMIT_COUNT_CHAR_SUMMARY));
        }else{
            preference.setSummary(MoString.getLimitedCount(normalSummary,LIMIT_COUNT_CHAR_SUMMARY));
        }
    }

    public MoPreference setNormalSummary(String normalSummary) {
        this.normalSummary = normalSummary;
        return this;
    }


    public MoPreference setValueSummary(String valueSummary) {
        this.valueSummary = valueSummary;
        return this;
    }

    /**
     * returns the key of the preference
     * @return
     */
    public String getKey(){
        return this.preference.getKey();
    }

}
