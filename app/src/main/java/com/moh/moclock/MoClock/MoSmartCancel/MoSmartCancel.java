package com.moh.moclock.MoClock.MoSmartCancel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Random;

import com.moh.moclock.R;

public abstract class MoSmartCancel {


    public static final int OBJECT_DETECTION_CANCEL = 0;
    public static final int TAP_CANCEL = 1;

    public static final int SUCCESS = 100;
    public static final int UNSUCCESSFUL = -100;
    public static final int NEXT_TEST = -1;
    public static final int RANDOM = -2;

    public static final String RESULT_ID = "res";
    public static final String SMART_ALARM = "smid";


    private boolean completed;

    public MoSmartCancel(){}



    public abstract void show(Activity a);




    public static void setResultAndFinish(Activity a, int result){
        Intent i = new Intent();
        i.putExtra(RESULT_ID,result);
        a.setResult(Activity.RESULT_OK, i);
        a.finish();
    }

    public static void startActivityForResult(Activity a, Intent i, int resultId){
        a.startActivityForResult(i,resultId);
    }


    public static MoSmartCancel readPreference(Activity activity) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean active = s.getBoolean(activity.getString(R.string.smart_alarm_cancel_switch),false);
        if(active){
            String list = s.getString(activity.getString(R.string.smart_alarm_cancel),"-2");
            int value = Integer.parseInt(list);
            switch (value){
                case MoSmartCancel.OBJECT_DETECTION_CANCEL:
                    return new MoObjectDetectionAlarmCancel();
                case MoSmartCancel.TAP_CANCEL:
                    return new MoTapCancelAlarm();
                case MoSmartCancel.RANDOM:
                    // return randomly
                    Random r = new Random();
                    int v = r.nextInt(100);
                    return v>50?new MoTapCancelAlarm():new MoObjectDetectionAlarmCancel();
            }
        }
        return null;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
