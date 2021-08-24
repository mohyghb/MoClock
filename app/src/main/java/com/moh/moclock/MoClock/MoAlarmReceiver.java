package com.moh.moclock.MoClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moh.moclock.MoClock.MoAlarmSession.MoAlarmWakeLock;
import com.moh.moclock.MoClock.MoAlarmSession.MoInitAlarmSession;


public class MoAlarmReceiver extends BroadcastReceiver {

    String channelId = "";

    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        MoAlarmWakeLock.acquireCpuWakeLock(context,1000000);
        int id = intent.getIntExtra(MoAlarmClockManager.SET_ID,-1);
        channelId = id + "kl";
        try {
            MoAlarmClockManager.getInstance().cancelAlarm(id,context);
        } catch (MoEmptyAlarmException e) {
            // we don't want it to ring if there is no alarm with such id
            // make notification
            return;
        }

        //MoAlarmWakeLock.acquireCpuWakeLock(context,1000000);
        // activate the alarm (ring service)
        try {
            MoInitAlarmSession.startAlarm(context,id);
        } catch (MoEmptyAlarmException e) {
            //e.printStackTrace();
        }

//        createNotificationChannel(context);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_access_alarms_black_24dp)
//                .setContentTitle("missed alarm")
//                .setContentText("alarm rang but there was no matching id (so it did not give you sound)")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(12, builder.build());

    }

}
