package Mo.moclock.MoClock.MoAlarmSession;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import Mo.moclock.MainActivity;
import Mo.moclock.MoClock.MoAlarmClockManager;
import Mo.moclock.MoClock.MoEmptyAlarmException;
import Mo.moclock.MoClock.MoSmartCancel.MoObjectDetectionAlarmCancel;
import Mo.moclock.MoClock.MoSmartCancel.MoSmartCancel;
import Mo.moclock.MoClock.MoSmartCancel.MoTapCancelAlarm;
import Mo.moclock.MoClock.MoTimer.MoTimer;

import Mo.moclock.MoHotWordDetection.MoHotWordDetector;
import Mo.moclock.MoLog.MoLog;
import Mo.moclock.MoMusic.MoVolume;
import Mo.moclock.MoSensor.MoMovementListener;
import Mo.moclock.MoSensor.MoShakeListener;
import Mo.moclock.MoVibration.MoVibration;
import Mo.moclock.R;

public class MoAlarmSessionActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{

    public static final String EXTRA_INFO = "emoinfo";

    private MoInformation moInformation;
    private TextView title;
    private TextView subTitle;
    private TextClock textClock;
    ConstraintLayout constraintLayout;
    MoInitAlarmSession.Type moType;
    private GestureDetectorCompat detectorCompat;
    private MoSmartCancel smartCancel;
    private TextView snoozeText;
    private TextView stopText;
    private boolean snoozed;



    MoMovementListener moMovementListener;


    private MoHotWordDetector moHotWordDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_alarm_session);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN|
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON|
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION|
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        );
        hideSystemUI();
        init();
        this.initLayoutAnimations();
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private void init(){
        switch (Objects.requireNonNull(getIntent().getStringExtra(EXTRA_INFO))){
            case MoNotificationAlarmSession.CHANNEL_ID_ALARM:
                this.moInformation = MoNotificationAlarmSession.moInformation;
                break;
            case MoNotificationTimerSession.CHANNEL_ID_TIMER:
                this.moInformation = MoNotificationTimerSession.moInformation;
                break;
        }
        if(moInformation == null) {
            Toast.makeText(this, "The information was lost error please report this", Toast.LENGTH_LONG).show();
            return;
        }

        this.title = findViewById(R.id.title_alarm);
        this.stopText = findViewById(R.id.stop_alarm);
        this.snoozeText = findViewById(R.id.snooze_button);

        stopText.setText(moInformation.getRightButton());
        snoozeText.setText(moInformation.getLeftButton());
        this.textClock = findViewById(R.id.text_clock);
        this.title.setText(moInformation.getTitle());
        this.moType = moInformation.getType();



        this.detectorCompat = new GestureDetectorCompat(this,this);
        detectorCompat.setOnDoubleTapListener(this);


        // also if smart cancel is not null start the activity
        if(MoInitAlarmSession.Type.CLOCK == this.moType){
            // only do it if it is clock
            smartCancel = MoSmartCancel.readPreference(this);
        }

        //starting movement listener
        activateMovementListener();

        // start listening for voice cancellation
        initSmartVoiceCancel();


        // cancel the timer of notification timer
        MoNotificationTimerSession.cancelTimer();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(moHotWordDetector!=null)
            moHotWordDetector.onResume();
    }

    private void initSmartVoiceCancel(){
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        boolean active = s.getBoolean(getString(R.string.smart_voice_cancel),false);
        if(active){
            // only init if it is active in settings
            // 0.4 sensitivity
            this.moHotWordDetector = new MoHotWordDetector(this,MoHotWordDetector.HOT_WORD_STOP,0.75f) {
                @Override
                public void onWakeWordDetected() {
                    // cancel alarm?
                    //maybe replace with stopAlarmConsider
                    stopAlarm();
                    //stopAlarmConsiderSmart();
                }
            };
            moHotWordDetector.onResume();
        }

    }

    // activates it only if the user wants it
    // also add a delay so that the alarm rings if they want
    private void activateMovementListener(){
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        boolean active = s.getBoolean(getString(R.string.smart_mute),false);
        if(active){
            this.moMovementListener = new MoMovementListener(this.moInformation.getClock().getVibration().isActive()) {
                @Override
                public void onStoppedMoving() {
                    MoNotificationAlarmSession.mute(false);
                }

                @Override
                public void onSlowMovement() {
                    //MoNotificationAlarmSession.mute(true);
                }

                @Override
                public void onMoving() {
                    MoNotificationAlarmSession.mute(true);
                }
            };
            int delaySeconds = s.getInt(getString(R.string.smart_mute_delay),0);
            Handler handler = new Handler();
            // delay the task for that amount
            handler.postDelayed(() -> moMovementListener.start(MoAlarmSessionActivity.this),
                    delaySeconds*1000);

        }
    }



    private void snoozeAlarm() {
        if(moInformation.getClock().getSnooze().isActive()){
            snoozed = true;
            stopAlarm();
            MoAlarmClockManager.getInstance().snoozeAlarm(moInformation.getId(),5,this);
        }else{
            Toast.makeText(this, "Snooze is not available", Toast.LENGTH_SHORT).show();
            stopAlarmConsiderSmart();
        }
    }

    private void resetTimer(){
        this.stopAlarm();
        MainActivity.isInApp = false;
        MoTimer.universalTimer.reset(this);
        //MoTimer.universalTimer.startService(this);
    }


    @Override
    protected void onDestroy() {
        if(moHotWordDetector!=null){
            moHotWordDetector.onDestroy();
            moHotWordDetector = null;
        }
        super.onDestroy();
        MoAlarmWakeLock.releaseCpuLock();
    }


    @Override
    public void onBackPressed() {
        // dont close the activity on back pressed
    }

    private void initLayoutAnimations(){
        this.constraintLayout = findViewById(R.id.alarm_session_cons_layout);
        startAnimation();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) || keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            //Do something
            boolean b = handleVolumeDownUp();
            if(b)
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean handleVolumeDownUp() {
        // only continue if this is an alarm
        // volume buttons during timer should inc/dec volume
        if(this.moType != MoInitAlarmSession.Type.CLOCK)
            return false;

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        String index = s.getString(getString(R.string.volume_button),"-3");
        switch (index){
            case "-3":
                // do nothing
                return true;
            case "1":
                // snooze alarm
                snoozeAlarm();
                return true;
            case "2":
                // control volumes
                return false;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void startAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
    }


    private void stopAlarm() {
        if(moInformation != null) {
            Intent i;
            switch (moInformation.getType()) {
                case CLOCK:
                    // different situations where the service needs to know about it
                    if(snoozed){
                        MoNotificationAlarmSession.snoozeSituation();
                    }else{
                        MoNotificationAlarmSession.alarmSituation();
                    }
                    i = new Intent(this, MoNotificationAlarmSession.class);
                    break;
                case TIMER:
                    i = new Intent(this, MoNotificationTimerSession.class);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + moInformation.getType());
            }
            stopService(i);
        }
        finishAffinity();
        finishAndRemoveTask();
    }

    private void stopAlarmConsiderSmart() {
        if (smartCancel != null) {
            // show smart cancel if there is any
            smartCancel.show(this);
        } else {
            stopAlarm();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case MoSmartCancel.TAP_CANCEL :
                if (resultCode == Activity.RESULT_OK) {
                    if((data != null ? data.getIntExtra(MoSmartCancel.RESULT_ID, 0) : 0) == MoSmartCancel.NEXT_TEST){
                        // start next test
                        smartCancel = new MoObjectDetectionAlarmCancel();
                        smartCancel.show(this);
                    }else{
                        // tap cancel was successful
                        stopAlarm();
                    }

                }
                break;
            case MoSmartCancel.OBJECT_DETECTION_CANCEL :
                if((data != null ? data.getIntExtra(MoSmartCancel.RESULT_ID, 0) : 0) == MoSmartCancel.NEXT_TEST){
                    // start next test
                    smartCancel = new MoTapCancelAlarm();
                    smartCancel.show(this);
                }else{
                    // tap cancel was successful
                    stopAlarm();
                }
                break;
        }

    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        // we snooze the alarm for another time that they already have picked
        switch (moType){
            case CLOCK:
                this.snoozeAlarm();
                break;
            case TIMER:
                this.resetTimer();
                break;
        }

        //System.out.println("---------------double tap------------------");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        stopAlarmConsiderSmart();
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }


}
