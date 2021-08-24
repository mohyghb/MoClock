package com.moh.moclock.MoClock.MoAlarmSession;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.moh.moclock.MoDate.MoTimeUtils;
import com.moh.moclock.MoId.MoId;
import com.moh.moclock.MoMusic.MoMusicPlayer;
import com.moh.moclock.MoMusic.MoVolume;
import com.moh.moclock.MoNotification.MoNotificationChannel;
import com.moh.moclock.MoUri.MoUri;
import com.moh.moclock.R;

public class MoNotificationTimerSession extends Service {



    public final static String CHANNEL_ID_TIMER = "tscid";

    public final static String NAME = "Timer Alarm";
    public final static String DESCRIPTION = "Fires a pop up for timer when it finishes";

    public final static int REQUEST_CODE = 130;
    public static int FORE_GROUND_SERVICE_ID = 10081;
    public static boolean enabled = true;


    public final static String STOP_ACTION = "Stop";
    public final static String STOP_ACTION_ID = "Stop2";
    public final static String RESET_ACTION = "Reset";

    private final float AMOUNT_VOLUME = 0.01f;
    private final long DURATION_INCREASE = 22000;
    private final long INCREASE_EVERY = 2000;
    //10 minutes
    private final long DURATION_TIMER = 60* 1000 * 10;

//    private MediaPlayer mMediaPlayer;
    private CountDownTimer volumeTimer;
    public static CountDownTimer timer;
    private MoMusicPlayer moMusicPlayer;
    //private float currentVolume = 0.8f;
    public static MoInformation moInformation;
    private long milliseconds;
    private int volCount = 0;




    public static void cancelTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }





    @Override
    public void onCreate() {
        if (MoInitAlarmSession.list.isEmpty())
            return;
        moInformation = MoInitAlarmSession.list.remove();
        int imp = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            imp = NotificationManager.IMPORTANCE_HIGH;
        }else{
            imp = NotificationCompat.PRIORITY_HIGH;
        }
        MoNotificationChannel.createNotificationChannel(NAME,DESCRIPTION,this,CHANNEL_ID_TIMER,imp);
        // we need to assign a different id each time to show the
        // heads up notification (otherwise the system wouldn't do it)
        FORE_GROUND_SERVICE_ID = MoId.getRandomInt();
        startForeground(FORE_GROUND_SERVICE_ID, notification(this,false));
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        enabled = true;
        timer = new CountDownTimer(DURATION_TIMER,1000) {
            @Override
            public void onTick(long l) {
                // updating notification
                updateNotification(MoNotificationTimerSession.this,true);
                milliseconds-=1000;
            }

            @Override
            public void onFinish() {
                // make an alarm that is done
            }
        }.start();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        enabled = false;
        cancelNotification();
        if(timer!=null){
            timer.cancel();
        }
        moMusicPlayer.onDestroy(this);
        if(volumeTimer!=null){
            volumeTimer.cancel();
        }
        super.onDestroy();
        MoAlarmWakeLock.releaseCpuLock();


    }

    public void updateNotification(Context context, boolean update){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(FORE_GROUND_SERVICE_ID, notification(context,update));
    }

    public void cancelNotification(){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.cancel(FORE_GROUND_SERVICE_ID);
        //notificationManager.cancelAll();
    }


    public Notification notification(Context context, boolean update){


        if(moMusicPlayer == null) {
             playSound(context);
        }

        // updating the notification without continouing the timer(when the timer is paused case)
        if(!update){
            // if we add intent multiple times, each time start activity is called
            // so we only do it for the first time
            Intent fullScreenIntent = new Intent(context, MoAlarmSessionActivity.class);
            fullScreenIntent.putExtra(MoAlarmSessionActivity.EXTRA_INFO,CHANNEL_ID_TIMER);
            fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                    fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            String[] texts = MoTimeUtils.convertMilli(milliseconds);

            RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.timer_notification_small);
            notificationLayout.setTextViewText(R.id.hour_notification_small, "- "+texts[0]);
            notificationLayout.setTextViewText(R.id.minute_notification_small, texts[1]);
            notificationLayout.setTextViewText(R.id.second_notification_small, texts[2]);


            Notification customNotification;


            int importance;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                importance = NotificationManager.IMPORTANCE_HIGH;
                // importance = NotificationManager.IMPORTANCE_DEFAULT;
            }else{
                importance = NotificationCompat.PRIORITY_HIGH;
            }

            customNotification = new NotificationCompat.Builder(context, CHANNEL_ID_TIMER)
                    .setSmallIcon(R.drawable.ic_hourglass_full_black_14dp)
                    .setContent(notificationLayout)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setColorized(true)
                    .setOnlyAlertOnce(true)
                    .addAction(R.drawable.ic_add_black_24,
                            STOP_ACTION
                            ,getAction(context,STOP_ACTION_ID))
                    .addAction(R.drawable.ic_add_black_24,
                            RESET_ACTION
                            ,getAction(context,RESET_ACTION))
                    .setColor(context.getColor(R.color.notification_stopwatch_background))
                    .setPriority(importance)
                    .setFullScreenIntent(fullScreenPendingIntent,true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setOngoing(true)
                    .setSound(null)
                    .build();

            return customNotification;
        }






        String[] texts = MoTimeUtils.convertMilli(milliseconds);

        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.timer_notification_small);
        notificationLayout.setTextViewText(R.id.hour_notification_small, "- "+texts[0]);
        notificationLayout.setTextViewText(R.id.minute_notification_small, texts[1]);
        notificationLayout.setTextViewText(R.id.second_notification_small, texts[2]);


        Notification customNotification;
        int importance;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
           // importance = NotificationManager.IMPORTANCE_DEFAULT;
        }else{
            importance = NotificationCompat.PRIORITY_HIGH;
        }


        customNotification = new NotificationCompat.Builder(context, CHANNEL_ID_TIMER)
                .setSmallIcon(R.drawable.ic_hourglass_full_black_14dp)
                .setContent(notificationLayout)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setColorized(true)
                .addAction(R.drawable.ic_add_black_24,
                        STOP_ACTION
                        ,getAction(context,STOP_ACTION_ID))
                .addAction(R.drawable.ic_add_black_24,
                        RESET_ACTION
                        ,getAction(context,RESET_ACTION))
                .setColor(context.getColor(R.color.notification_stopwatch_background))
                .setPriority(importance)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .build();


        return customNotification;
    }


    public static PendingIntent getAction(Context context, String action){
        Intent pendingIntent = new Intent(context, MoAlarmSessionBroadCast.class);
        pendingIntent.setAction(action);
        return PendingIntent.getBroadcast(context,REQUEST_CODE,pendingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
    }





    private Uri getAlarmUri(){
        // TODO test
        // checking to see if user has set a custom alert for this
        Uri customAlert = MoUri.get(this,R.string.timer_music);
        if(customAlert!=null){
            return customAlert;
        }
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null){
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null){
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return  alert;
    }



    private  void playSound(Context context){

        moMusicPlayer = new MoMusicPlayer();
        moMusicPlayer.playStopOthers(context,getAlarmUri());

        this.volumeTimer = new CountDownTimer(DURATION_INCREASE,INCREASE_EVERY) {
            @Override
            public void onTick(long l) {
                if(volCount!=0){
                    increaseVol();
                }
                volCount++;
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


    private void increaseVol(){
        MoVolume.increaseVolume(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
