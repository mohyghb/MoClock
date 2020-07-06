package Mo.moclock.MoClock.MoWorldClock;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import Mo.moclock.MoInteractable.MoDelete.MoDelete;
import Mo.moclock.MoInteractable.MoDelete.MoListDeletable;
import Mo.moclock.MoRunnable.MoRunnable;

public class MoWorldClockArrayAdapter extends ArrayAdapter<MoWorldClock> {

    private Context context;
    private List<MoWorldClock> list;
    private MoRunnable onSelectedSizeChanged;
    private MoRunnable activateDeleteMode;
    private boolean shouldAnimate;
    private MoDelete moDelete;

    public MoWorldClockArrayAdapter(Context context, int resource,
                                    ArrayList<MoWorldClock> objects,MoRunnable onSelectedSizeChanged)
    {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.onSelectedSizeChanged = onSelectedSizeChanged;
    }

    //called when rendering the list
    public View getView(final int position, final View convertView, ViewGroup parent) {

        //get the property we are displaying
        final MoWorldClock alarmClock = list.get(position);
        //get the inflater and inflate the XML layout for each item
        //final View view = conversation.getView(context);

        return alarmClock.display(context,this,shouldAnimate);
    }


    public void onClickToSelect(MoWorldClock worldClock){
        if(onSelectedSizeChanged!=null)
            onSelectedSizeChanged.run(MoWorldClockManager.manager.getSelectedSize());
    }





    public MoWorldClockArrayAdapter setActivateDeleteMode(MoRunnable r){
        this.activateDeleteMode = r;
        return this;
    }

    public void activateDeleteMode(boolean r){
        shouldAnimate = true;
        activateDeleteMode.run(r);
        Handler handler = new Handler();
        handler.postDelayed(()-> {
            shouldAnimate = false;
        },200);
        notifyDataSetChanged();
    }


}
