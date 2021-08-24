package com.moh.moclock.MoClock.MoAlarmSession;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import java.util.LinkedList;
import java.util.Queue;

import com.moh.moclock.MoClock.MoAlarmClock;
import com.moh.moclock.MoClock.MoAlarmClockManager;
import com.moh.moclock.MoClock.MoEmptyAlarmException;

public class MoInitAlarmSession {

    public enum Type{
        CLOCK,
        TIMER,
    }


    public static Queue<MoInformation> list = new LinkedList<>();

    public static void start(Context context,String title,String lb,String rb,Type type,int id){

        MoInformation moInformation = new MoInformation(title,"subtitle",type,id);
        moInformation.setLeftButton(lb);
        moInformation.setRightButton(rb);
        list.add(moInformation);
        switch (type){
            case TIMER:
                moInformation.changeTitleIfEmpty("Timer");
                Intent i = new Intent(context,MoNotificationTimerSession.class);
                ContextCompat.startForegroundService(context,i);
                break;
            case CLOCK:
                moInformation.changeTitleIfEmpty("Alarm");
                Intent i2 = new Intent(context,MoNotificationAlarmSession.class);
                ContextCompat.startForegroundService(context,i2);
                break;
        }

    }



    public static void startAlarm(Context context,int id) throws MoEmptyAlarmException {
        MoAlarmClock c = MoAlarmClockManager.getInstance().getAlarm(id);
        MoInitAlarmSession.start(context,c.getTitle(),"Long click to snooze",
                "Long click to stop",MoInitAlarmSession.Type.CLOCK,id);
    }

    public static void startTimer(Context context){
        MoInitAlarmSession.start(context,"Timer",
                "Long click to reset","Long click to stop", Type.TIMER,1);
    }

}
