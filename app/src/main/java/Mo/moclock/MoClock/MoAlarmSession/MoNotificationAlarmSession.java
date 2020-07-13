package Mo.moclock.MoClock.MoAlarmSession;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import Mo.moclock.MoDate.MoDate;
import Mo.moclock.MoMusic.MoMusicPlayer;
import Mo.moclock.MoMusic.MoVolume;
import Mo.moclock.MoNotification.MoNotificationChannel;
import Mo.moclock.MoUri.MoUri;
import Mo.moclock.MoVibration.MoVibration;
import Mo.moclock.R;

public class MoNotificationAlarmSession extends Service {

    public final static String CHANNEL_ID_ALARM = "ascid";

    public final static String NAME = "Alarm";
    public final static String DESCRIPTION = "Alarm session";
    public final static String ALARM_STOPPED = "Alarm stopped";

    public final static int REQUEST_CODE = 12;
    public final static int FORE_GROUND_SERVICE_ID = 10;

    public final static int SITUATION_ALARM_STOP = 0;
    public final static int SITUATION_ALARM_SNOOZE = 1;


    public final static String STOP_ACTION = "Stop";
    public final static String SNOOZE_ACTION = "Snooze";
    public final static String OPEN_ACTION = "open";
    public final static String NULL_ACTION = "null";

    private final float AMOUNT_VOLUME = 0.02f;
    private final long DURATION_INCREASE = 20000;
    private final long INCREASE_EVERY = 3000;


    private CountDownTimer volumeTimer;
    public static MoInformation moInformation;
    private static MoMusicPlayer moMusicPlayer;
    // what situation are we in when the user presses destroy
    private static int destroySituation;
    private int volCount = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (MoInitAlarmSession.list.isEmpty())
            return;
        moInformation = MoInitAlarmSession.list.remove();
        int imp = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // imp = NotificationManager.IMPORTANCE_HIGH;
            imp = NotificationManager.IMPORTANCE_MAX;
        } else {
            imp = NotificationCompat.PRIORITY_HIGH;
        }
        MoNotificationChannel.createNotificationChannel(NAME, DESCRIPTION, this, CHANNEL_ID_ALARM, imp);
        startForeground(FORE_GROUND_SERVICE_ID, notification(this, false));
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (volumeTimer != null) {
            volumeTimer.cancel();
        }
        moMusicPlayer.onDestroy(this);
        MoVibration.cancel();
        MoVibration.vibrateOnce(this,200);
        MoAlarmWakeLock.releaseCpuLock();
        if(destroySituation  == SITUATION_ALARM_STOP) {
            // only show this if the alarm was stopped
            Toast.makeText(this,ALARM_STOPPED,Toast.LENGTH_SHORT).show();
        }
        resetSituation();
    }




    public Notification notification(Context context, boolean update) {
        Intent fullScreenIntent = new Intent(context, MoAlarmSessionActivity.class);
        fullScreenIntent.putExtra(MoAlarmSessionActivity.EXTRA_INFO, CHANNEL_ID_ALARM);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_ONE_SHOT);
        playSound(context);
        vibrate();
        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(),
                R.layout.timer_notification_small);
        MoDate date = new MoDate();
        notificationLayout.setTextViewText(R.id.hour_notification_small, date.getReadableTime());
        notificationLayout.setTextViewText(R.id.timer_notification_title, moInformation.getTitle());
        notificationLayout.setTextViewText(R.id.minute_notification_small, "");
        notificationLayout.setTextViewText(R.id.second_notification_small, "");
        notificationLayout.setTextViewText(R.id.timer_dot_2, "");
        notificationLayout.setTextViewText(R.id.timer_dot_1, "");
        Notification customNotification;
        int importance;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        } else {
            importance = NotificationCompat.PRIORITY_HIGH;
        }
        customNotification = new NotificationCompat.Builder(context, CHANNEL_ID_ALARM)
                .setSmallIcon(R.drawable.ic_access_alarms_black_24dp)
                .setContent(notificationLayout)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setColorized(true)
                .setColor(context.getColor(R.color.notification_stopwatch_background))
                .setPriority(importance)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setContentIntent(fullScreenPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .build();
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        boolean addSnooze = moInformation.getClock().getSnooze().isActive();
        boolean addStop = !s.getBoolean(context.getString(R.string.smart_alarm_cancel_switch),false);
        String stopAction = addStop?STOP_ACTION:OPEN_ACTION;
        String snoozeAction = addSnooze?SNOOZE_ACTION:NULL_ACTION;
        customNotification.actions = new Notification.Action[]{
                new Notification.Action(R.drawable.ic_add_black_24, STOP_ACTION, getAction(context, stopAction)),
                new Notification.Action(R.drawable.ic_add_black_24, SNOOZE_ACTION, getAction(context, snoozeAction))
        };
        return customNotification;
    }


    public static PendingIntent getAction(Context context, String action) {
        if(action.equals(NULL_ACTION)){
            return null;
        }
        Intent pendingIntent = new Intent(context, MoAlarmSessionBroadCast.class);
        pendingIntent.setAction(action);
        return PendingIntent.getBroadcast(context, REQUEST_CODE, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private Uri getAlarmUri() {
        // TODO TEST
        // checking to see if user has set a custom alert for this
        Uri customAlert = MoUri.get(this,R.string.alarm_music);
        if(customAlert!=null){
            return customAlert;
        }
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    private void vibrate() {
        if (!moInformation.getClock().getVibration().isActive()) {
            return;
        }
        MoVibration.vibrate(this, 2000);
    }


    private void playSound(Context context) {
        if (!moInformation.getClock().hasMusic()) {
            // if they said no music just return
            return;
        }
        moMusicPlayer = new MoMusicPlayer();
        // making the initial volume zero and then increasing it every INCREASE_EVERY
        MoVolume.setVolume(0, this);
        moMusicPlayer.playStopOthers(context,getAlarmUri());
        this.volumeTimer = new CountDownTimer(DURATION_INCREASE, INCREASE_EVERY) {
                    @Override
                    public void onTick(long l) {
                        if(volCount!=0) {
                            increaseVol();
                        }
                        volCount++;
                    }
                    @Override
                    public void onFinish() {}
        }.start();
    }

    // mute the media player if m
    public static void mute(boolean m){
        if(moMusicPlayer==null)
            return;
        moMusicPlayer.mute(m);
    }



    private void increaseVol(){
        MoVolume.increaseVolume(this);
    }


    /**
     * these following methods are used to
     * see whether user snoozed or stopped the alarm
     * from activity or from the notification
     */

    public static void resetSituation(){
        destroySituation = -1;
    }
    public static void snoozeSituation(){
        destroySituation = SITUATION_ALARM_SNOOZE;
    }
    public static void alarmSituation(){
        destroySituation = SITUATION_ALARM_STOP;
    }


}
