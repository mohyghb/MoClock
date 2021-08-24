package com.moh.moclock.MoClock.MoTimer.MoTimerPresetPackage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;



public class MoTimerPresetAdapter extends ArrayAdapter<MoTimerPreset> {

    private Context context;
    private List<MoTimerPreset> alarms;


    public MoTimerPresetAdapter(Context context, int resource, ArrayList<MoTimerPreset> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.alarms = objects;
    }

    //called when rendering the list
    public View getView(final int position, final View convertView, ViewGroup parent) {

        //get the property we are displaying
        final MoTimerPreset alarmClock = alarms.get(position);
        //get the inflater and inflate the XML layout for each item
        //final View view = conversation.getView(context);

        return alarmClock.display(context);
    }




}
