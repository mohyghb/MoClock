package com.moh.moclock.MoClock.MoStopWatch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.TimerTask;

import com.moh.moclock.MoDate.MoTimeUtils;
import com.moh.moclock.MoNotification.MoNotificationChannel;
import com.moh.moclock.R;

public class MoStopWatchService extends Service {

    public final static String CHANNEL_ID = "stop_watch_channel_id";
    public final static String NAME = "MoStopwatch";
    public final static String DESCRIPTION = "";
    public final static int REQUEST_CODE = 1203;
    public final static int FORE_GROUND_SERVICE_ID = 12203;


    public final static String OPEN_ACTION = "oa";
    public final static String LAP_ACTION = "la";
    public final static String STOP_ACTION = "sa";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        MoNotificationChannel.createNotificationChannel(NAME,DESCRIPTION,this,CHANNEL_ID);
        startForeground(FORE_GROUND_SERVICE_ID, notification(this,false));
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(MoStopWatch.universal.isRunning()){
            MoStopWatch.universal.start(new TimerTask() {
                @Override
                public void run() {
                    updateNotification(MoStopWatchService.this,true);
                }
            });
        }else{
            updateNotification(this,false);
        }
        return START_STICKY;
    }


    public static void updateNotification(Context context, boolean update){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(FORE_GROUND_SERVICE_ID, notification(context,update));
    }

    public static Notification notification(Context context, boolean update){
//        Intent resultIntent = new Intent(context, MainActivity.class);
//
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        //resultIntent.putExtra(MainActivity.SECTION_CODE,MainActivity.TIMER_CODE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
//                0);


        // updating the notification without continouing the timer(when the timer is paused case)
//        if(update)
//            MoStopWatch.universal.incrementStopWatch();


        String[] texts = MoTimeUtils.convertMilli(MoStopWatch.universal.getMilliSecondsElapsed());
//
        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(),
                R.layout.stop_watch_notification_small);

        notificationLayout.setTextViewText(R.id.hour_stopwatch_notification_small, texts[1]);
        notificationLayout.setTextViewText(R.id.minute_stopwatch_notification_small, texts[2]);

        String laps = MoStopWatch.universal.getLapCounter();
        notificationLayout.setViewVisibility(R.id.lap_counter_notification,laps==null?View.GONE: View.VISIBLE);
        notificationLayout.setTextViewText(R.id.lap_counter_notification,laps);
        //notificationLayout.setTextViewText(R.id.second_stopwatch_notification_small, texts[3]);

        Notification customNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_timer_black_24dp)
                .setContent(notificationLayout)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .addAction(R.drawable.ic_add_black_24,
                        MoStopWatch.universal.getStopString(),
                        getAction(context,STOP_ACTION))
                .addAction(R.drawable.ic_add_black_24,
                        MoStopWatch.universal.getLapString()
                        ,getAction(context,LAP_ACTION))
                .setColorized(true)
                .setAutoCancel(true)
                .setColor(context.getColor(R.color.notification_stopwatch_background))
                .setContentIntent(getAction(context,OPEN_ACTION))
                .build();

        return customNotification;
    }


    public static PendingIntent getAction(Context context, String action){
        Intent pendingIntent = new Intent(context, MoStopWatchBroadcastReceiver.class);
        pendingIntent.setAction(action);
        return PendingIntent.getBroadcast(context,REQUEST_CODE,pendingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
