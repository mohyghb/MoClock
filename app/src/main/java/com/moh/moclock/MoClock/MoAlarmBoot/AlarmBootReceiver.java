package com.moh.moclock.MoClock.MoAlarmBoot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.getAction())) {
            // It is better to reset alarms using Background IntentService
            Intent i = new Intent(context, AlarmBootService.class);
            AlarmBootService.enqueueWork(context,i);
        }

    }
}
