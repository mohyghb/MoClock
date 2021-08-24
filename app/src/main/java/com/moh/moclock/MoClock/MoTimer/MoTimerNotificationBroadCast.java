package com.moh.moclock.MoClock.MoTimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moh.moclock.MainActivity;
import com.moh.moclock.MoSection.MoSectionManager;

public class MoTimerNotificationBroadCast extends BroadcastReceiver {


    /**
     * when the user presses a notification action button, this class
     * gets activated
     * @param context
     * @param intent
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action == null)
            return;

        // don't do anything if timer has not been created
        if (!MoTimer.universalTimer.isCreated())
            return;

        switch (action){
            case MoTimer.CANCEL_ACTION:
                MoTimer.universalTimer.cancel(context,false,false);
                break;
            case MoTimer.PAUSE_ACTION:
                MoTimer.universalTimer.pause(true);
                MoTimer.universalTimer.updateNotification(context,false);
                break;
            case MoTimer.OPEN_ACTION:
                MoSectionManager.getInstance().setSection(MoSectionManager.TIMER_SECTION);
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
        }


        //System.out.println(action);

    }
}
