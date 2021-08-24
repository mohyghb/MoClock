package com.moh.moclock.MoClock.MoTimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import com.moh.moclock.MainActivity;
import com.moh.moclock.MoClock.MoAlarmSession.MoInitAlarmSession;
import com.moh.moclock.MoDate.MoTimeUtils;
import com.moh.moclock.MoNotification.MoNotificationChannel;
import com.moh.moclock.MoRunnable.MoRunnable;
import com.moh.moclock.MoUI.MoTextInput;
import com.moh.moclock.R;

public class MoTimer extends Service {


    public static MoTimer universalTimer = new MoTimer();


    private static String SEP_KEY = "Rfsiufc";
    private static String FILE_NAME_TIMER = "Wsdvidv";

    private final static int FORE_GROUND_SERVICE_ID = 135;
    private final static String CHANNEL_ID = "noasdi";
    private final static String NAME = "MoTimer";
    private final static String DESCRIPTION = "";

    public static String INTENT_TIMER = "itasd";
    public static final String CANCEL_ACTION = "cancelAction";
    public static final String PAUSE_ACTION = "pauseAction";
    public static final String OPEN_ACTION = "openAction";
    private static int REQUEST_CODE = 2;

    private long endTimeMilli;
    private long currentTimeMilli;
    private boolean isPaused;
    private boolean updateTextViews = true;

    /**
     * is running refers to any point where there is a timer
     * in this class and it is not done yet
     */
    private boolean isCreated;
    private CountDownTimer countDownTimer;

    //[hr,min,sec]
    private TextInputEditText[] timerTextInputs;
    // [start,cancel,pause]
    private Button[] buttons;
    private ProgressBar progressBar;
    private Context parentContext;
    private MoRunnable changeLayout;

    public MoTimer(){
        this.isCreated = false;
    }

    public MoTimer(MoRunnable changeLayout,Context context,long endTimeMilli, ProgressBar pb,Button[] bts, TextInputEditText ... tvs){
        this.endTimeMilli = endTimeMilli;
        this.currentTimeMilli = endTimeMilli;
        this.isCreated = true;
        this.timerTextInputs = tvs;
        this.parentContext = context;
        this.changeLayout = changeLayout;
        this.buttons = bts;
        this.setProgressBar(pb);
    }


    private void printTime(){
        if (!updateTextViews)
            return;
        String[] texts = MoTimeUtils.convertMilli(MoTimer.universalTimer.currentTimeMilli);
        timerTextInputs[0].setText(texts[0]);
        timerTextInputs[1].setText(texts[1]);
        timerTextInputs[2].setText(texts[2]);
    }

    public void startTimer(){
        MoTextInput.disable(MoTimer.universalTimer.timerTextInputs);
        printTime();
        MoTimer.universalTimer.setCountDownTimer(new CountDownTimer(MoTimer.universalTimer.getCurrentTimeMilli(),1) {
            @Override
            public void onTick(long l) {
                MoTimer.universalTimer.setCurrentTimeMilli(l);
                //MoTimer.universalTimer.updateCurrentMilli( -1);
                progressBar.setProgress((int)(l));
                printTime();

            }

            @Override
            public void onFinish() {
                MoTimer.this.onFinish();
                MoInitAlarmSession.startTimer(parentContext);
                System.out.println("-------------------done-----------------------");

            }
        }.start());
        this.handlePause();
    }

    /**
     * if it was paused from notification then it needs to be paused here as well
     */
    private void handlePause(){
        if(MoTimer.universalTimer.isPaused){
            MoTimer.universalTimer.countDownTimer.cancel();
        }
    }

    @Override
    public void onCreate() {
        MoNotificationChannel.createNotificationChannel(NAME,DESCRIPTION,this,CHANNEL_ID);
        startForeground(FORE_GROUND_SERVICE_ID, notification(this,false));
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MoTimer.universalTimer.setCountDownTimer(new CountDownTimer(MoTimer.universalTimer.getCurrentTimeMilli(),1000) {
            @Override
            public void onTick(long l) {
                // updating notification
                updateNotification(MoTimer.this,true);
            }

            @Override
            public void onFinish() {
                MoTimer.universalTimer.cancel(MoTimer.universalTimer.parentContext,false,false);
                MoInitAlarmSession.startTimer(MoTimer.this);
                System.out.println("-------------------done notification-----------------------");
                // make an alarm that is done
            }
        }.start());
        handlePause();

        return START_STICKY;
    }

    /* creates a live notification for this timer
     */

    /**
     *
     * @param update
     * wether or not it should decrement the timer
     * ** the initial foreground service does not need to be updated at the beg
     * @return
     */
    private Notification notification(Context context, boolean update){

        // updating the notification without continouing the timer(when the timer is paused case)
        if(update)
            MoTimer.universalTimer.updateCurrentMilli(-1000);


        String[] texts = MoTimeUtils.convertMilli(MoTimer.universalTimer.getCurrentTimeMilli());

        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.timer_notification_small);
        notificationLayout.setTextViewText(R.id.hour_notification_small, texts[0]);
        notificationLayout.setTextViewText(R.id.minute_notification_small, texts[1]);
        notificationLayout.setTextViewText(R.id.second_notification_small, texts[2]);

        Notification customNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_hourglass_empty_black_24dp)
                .setContent(notificationLayout)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .addAction(R.drawable.ic_add_black_24,
                        context.getString(R.string.cancel),
                        getAction(context,CANCEL_ACTION))
                .addAction(R.drawable.ic_add_black_24,
                         MoTimer.universalTimer.getPauseButtonText()
                        ,getAction(context,PAUSE_ACTION))
                .setColorized(true)
                .setAutoCancel(true)
                .setColor(context.getColor(R.color.notification_timer_background))
                .setContentIntent(getAction(context,OPEN_ACTION))
                .setOnlyAlertOnce(true)
                .setSound(null)
                .build();

        return customNotification;
    }


    /**
     *
     * @param update
     */
    public void updateNotification(Context context, boolean update){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(FORE_GROUND_SERVICE_ID, notification(context,update));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void changeButtonLayout(boolean start,boolean pause,boolean cancel) {
        if (changeLayout != null){
            changeLayout.run(start,pause,cancel);
        }
    }



    public long getEndTimeMilli() {
        return endTimeMilli;
    }

    public void setEndTimeMilli(long endTimeMilli) {
        this.endTimeMilli = endTimeMilli;
    }

    public long getCurrentTimeMilli() {
        return currentTimeMilli;
    }

    public void updateCurrentMilli(long v){
        this.currentTimeMilli += v;
    }

    public void setCurrentTimeMilli(long currentTimeMilli) {
        this.currentTimeMilli = currentTimeMilli;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }


    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        if(this.countDownTimer != null){
            this.countDownTimer.cancel();
        }
        this.countDownTimer = countDownTimer;
    }

    public MoTimer setUpdateTextViews(boolean updateTextViews) {
        this.updateTextViews = updateTextViews;
        return this;
    }

    public void onFinish(){
        progressBar.setProgress(0);
        MoTimer.universalTimer.cancel(parentContext,false,false);
        MoTextInput.enable(MoTimer.universalTimer.timerTextInputs);
        MoTimer.universalTimer.changeButtonLayout(true,false,false);
    }

    public void update(){
        progressBar.setProgress(0);
        MoTextInput.enable(MoTimer.universalTimer.timerTextInputs);
        MoTimer.universalTimer.changeButtonLayout(true,false,false);
    }


    /**
     *
     * @param action
     * @return
     */

    private PendingIntent getAction(Context context,String action){
        Intent cancelIntent = new Intent(context,MoTimerNotificationBroadCast.class);
        cancelIntent.setAction(action);
        return PendingIntent.getBroadcast(context,REQUEST_CODE,cancelIntent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void cancel(Context context,boolean updateTvs,boolean isRunning){
        if(MoTimer.universalTimer.isCreated) {
            ///
            MoTimer.universalTimer.setCreated(isRunning);
            ///
            Intent intent1 = new Intent(context, MoTimer.class);
            context.stopService(intent1);
            MoTimer.universalTimer.countDownTimer.cancel();
            if (updateTvs && isRunning) {
                // when you come back to the app from notification
                this.startTimer();
            }
        }

    }

    /**
     * cancel in main
     */
    public void cancel(Context context){
        this.countDownTimer.cancel();
        this.cancel(context,false, false);
        MoTextInput.enable(this.timerTextInputs);
        this.progressBar.setProgress(0);
    }



    public void pause(boolean isNotification){
        MoTimer.universalTimer.isPaused = !MoTimer.universalTimer.isPaused;
        if(isNotification){
            // notification does not need to startService the whole time over
            if(isPaused){
                MoTimer.universalTimer.countDownTimer.cancel();
            } else{
                MoTimer.universalTimer.countDownTimer.start();
            }
        } else {
            // when it is not in notification
            // the timer starts again from the last time in app
            if(isPaused){
                MoTimer.universalTimer.countDownTimer.cancel();
            } else{
                MoTimer.universalTimer.startTimer();
            }
        }

    }

    public String getPauseButtonText(){
        return this.isPaused? "Resume":"Pause";
    }

    public boolean showingResume(){
        return this.isPaused;
    }


    public void startService(Context context){
        if(MoTimer.universalTimer.isCreated){
            Intent intent = new Intent(context,MoTimer.class);
            ContextCompat.startForegroundService(context,intent);
        }
    }


    public void setTimerTextInputs(TextInputEditText ... timerTextInputs) {
        this.timerTextInputs = timerTextInputs;
    }

    /**
     *
     * @return
     */


    public void reset(Context context) {
        this.currentTimeMilli = endTimeMilli;
        setCreated(true);
        startTimer();
        if(!MainActivity.isInApp){
            startService(context);
        }else{
            changeButtonLayout(false,true,true);
        }
    }





    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
        if(this.progressBar != null){
            this.progressBar.setMax((int)(endTimeMilli));
            if(this.isCreated){
                this.progressBar.setProgress((int)currentTimeMilli);
            }
        }
    }


    public void setButtons(Button ... buttons){
        this.buttons = buttons;
    }

}
