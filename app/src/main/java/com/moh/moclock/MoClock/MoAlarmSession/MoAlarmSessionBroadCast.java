package com.moh.moclock.MoClock.MoAlarmSession;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import com.moh.moclock.MoClock.MoAlarmClockManager;
import com.moh.moclock.MoClock.MoTimer.MoTimer;

public class MoAlarmSessionBroadCast extends BroadcastReceiver {

    public static List<Activity> activityList = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action == null)
            return;

        boolean closeAllActivities = true;
        switch (action) {
            case MoNotificationAlarmSession.STOP_ACTION:
                MoNotificationAlarmSession.alarmSituation();
                Intent i = new Intent(context,MoNotificationAlarmSession.class);
                context.stopService(i);
                break;
            case MoNotificationAlarmSession.SNOOZE_ACTION:
                MoNotificationAlarmSession.snoozeSituation();
                Intent i3 = new Intent(context,MoNotificationAlarmSession.class);
                context.stopService(i3);
                MoAlarmClockManager.getInstance().snoozeAlarm(
                        MoNotificationAlarmSession.moInformation.getId(),5,context);
                break;
            case MoNotificationAlarmSession.OPEN_ACTION:
                Intent fullScreenIntent = new Intent(context, MoAlarmSessionActivity.class);
                fullScreenIntent.putExtra(MoAlarmSessionActivity.EXTRA_INFO, MoNotificationAlarmSession.CHANNEL_ID_ALARM);
                fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(fullScreenIntent);
                closeAllActivities = false;
                break;
            case MoNotificationTimerSession.STOP_ACTION_ID:
                if (!MoNotificationTimerSession.enabled)
                    return;
                Intent i2 = new Intent(context,MoNotificationTimerSession.class);
                context.stopService(i2);
                break;
            case MoNotificationTimerSession.RESET_ACTION:
                if (!MoNotificationTimerSession.enabled)
                    return;
                Intent i4 = new Intent(context,MoNotificationTimerSession.class);
                context.stopService(i4);
                //reset it
                MoTimer.universalTimer.reset(context);
                break;
        }


        if (closeAllActivities) {
            closeAllActivities();
        }
    }

    public static void closeAllActivities() {
        for (Activity a : activityList) {
            a.finish();
        }
        activityList = new ArrayList<>();
    }
}
