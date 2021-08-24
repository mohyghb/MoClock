package com.moh.moclock.MoClock;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import com.moh.moclock.MoRunnable.MoRunnable;

public class MoAlarmListView extends ArrayAdapter<MoAlarmClock> {

    private Context context;
    private List<MoAlarmClock> alarms;
    private Runnable update;

    private Runnable deleteModeUI;
    private MoRunnable turnSelectAll;
    private MoRunnable turnDelete;
    private MoRunnable changeTitleRun;
    private boolean deleteAnimation;



    public MoAlarmListView(Context context, int resource,
                           ArrayList<MoAlarmClock> objects,
                           Runnable r, Runnable dmui, MoRunnable tsa, MoRunnable td,MoRunnable ct)
    {
        super(context, resource, objects);
        this.context = context;
        this.alarms = objects;
        this.update = r;
        this.deleteModeUI = dmui;
        this.turnSelectAll = tsa;
        this.turnDelete = td;
        this.changeTitleRun = ct;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.update.run();
    }

    //called when rendering the list
    public View getView(final int position, final View convertView, ViewGroup parent) {

        //get the property we are displaying
        final MoAlarmClock alarmClock = alarms.get(position);
        //get the inflater and inflate the XML layout for each item
        //final View view = conversation.getView(context);
        // third element is whether they should show animation for delete mode or not
        return alarmClock.display(context,
                this,
                deleteAnimation
        );
    }


    public void activateDeleteMode(){
        MoAlarmClock.isInDeleteMode = true;
        deleteAnimation = true;
        this.deleteModeUI.run();
        Handler handler = new Handler();
        handler.postDelayed(()-> {
            deleteAnimation = false;
        },200);
        notifyDataSetChanged();
    }


    public void deActivateDeleteMode(){
        MoAlarmClock.isInDeleteMode = false;
        for(MoAlarmClock a: alarms){
            a.setSelected(false);
        }
        notifyDataSetChanged();
    }


    private void changeSelectAll(){
        if(MoAlarmClock.isInDeleteMode){
            if(allIsSelected()){
                this.turnSelectAll.run(true);
            }else{
                this.turnSelectAll.run(false);
            }
        }
    }

    void onSelect(){
        changeSelectAll();
        showDelete();
        changeTitle();
    }

    private void changeTitle(){
        this.changeTitleRun.run(MoAlarmClockManager.getInstance().getSelectedCount());
    }


    private boolean allIsSelected(){
        for(MoAlarmClock c: alarms){
            if(!c.isSelected()){
                return false;
            }
        }
        return true;
    }


    private void showDelete(){
        if(MoAlarmClockManager.getInstance().getSelectedCount() > 0){
            // show it
            turnDelete.run(true);
        }else{
            turnDelete.run(false);
        }
    }



    public void selectDeselectAll(boolean select){
        for(MoAlarmClock a: alarms){
            a.setSelected(select);
        }
        onSelect();
        notifyDataSetChanged();
    }




    public void setDeleteModeUI(Runnable r){
        this.deleteModeUI = r;
    }




}
