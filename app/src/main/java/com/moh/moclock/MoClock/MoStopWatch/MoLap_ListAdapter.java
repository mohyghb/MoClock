package com.moh.moclock.MoClock.MoStopWatch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import com.moh.moclock.MoAnimation.MoAnimation;

public class MoLap_ListAdapter extends ArrayAdapter<MoLap> {


    private Context context;
    private List<MoLap> laps;


    public MoLap_ListAdapter(Context context, int resource, List<MoLap> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.laps = objects;
    }

    //called when rendering the list
    public View getView(final int position, final View convertView, ViewGroup parent) {

        //get the property we are displaying
        final MoLap lap = laps.get(laps.size() - (position + 1));
        //get the inflater and inflate the XML layout for each item
        //final View view = conversation.getView(context);
        int index = laps.size() - (position + 1);
        View v = lap.display(context,index,MoStopWatch.universal.getMoLapManager().getStatus(lap));
        //TODO animation does not work as intended (fix it)
        if(!lap.isDoneAnimation()){
            // only animate the last element
//            if(index%2==0){
//                v.startAnimation(MoAnimation.get(MoAnimation.LEFT_TO_RIGHT));
//            }else{
//                v.startAnimation(MoAnimation.get(MoAnimation.RIGHT_TO_LEFT));
//            }
            v.startAnimation(MoAnimation.get(MoAnimation.HALF_TOP_TO_BOTTOM));

            lap.setDoneAnimation(true);
        }
        return v;
    }

}
