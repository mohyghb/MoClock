package Mo.moclock.MoClock.MoStopWatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;

import java.util.TimerTask;

import Mo.moclock.MainActivity;
import Mo.moclock.MoClock.MoTimer.MoTimer;
import Mo.moclock.MoSection.MoSectionManager;

public class MoStopWatchBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action == null)
            return;

        switch (action){
            case MoStopWatchService.STOP_ACTION:
                MoStopWatch.universal.stop(new TimerTask() {
                    @Override
                    public void run() {
                        MoStopWatchService.updateNotification(context,true);
                    }
                });
                MoStopWatchService.updateNotification(context,false);
                break;
            case MoStopWatchService.LAP_ACTION:
                boolean r = MoStopWatch.universal.lap();
                if(r) {
                    MoStopWatch.universal.reset();
                    Intent intent1 = new Intent(context,MoStopWatchService.class);
                    context.stopService(intent1);
                    //System.out.println("reseting from service");
                }else{
                    MoStopWatchService.updateNotification(context,false);
                }
                break;
            case MoStopWatchService.OPEN_ACTION:
                MoSectionManager.getInstance().setSection(MoSectionManager.STOP_WATCH_SECTION);
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
        }

    }
}
