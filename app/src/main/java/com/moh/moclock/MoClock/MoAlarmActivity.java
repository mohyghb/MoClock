package com.moh.moclock.MoClock;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import com.moh.moclock.R;

public class MoAlarmActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock fullWakeLock;
    private PowerManager.WakeLock partialWakeLock;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        statusBar();
        activateWakeLocks();
        window();
        //playSound(this,getAlarmUri());

        setContentView(R.layout.activity_mo_alarm);
        backgroundAnimation();


        Button button = findViewById(R.id.stop_alarm_button);
        button.setOnClickListener(v -> stopAlarm());



        MoAlarmClockManager.getInstance().activateNextAlarm(this.getBaseContext());


    }


    private void backgroundAnimation() {
        //ConstraintLayout constraintLayout = findViewById(R.id.alarm_activity_layout);
//        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
//        animationDrawable.setEnterFadeDuration(2000);
//        animationDrawable.setExitFadeDuration(4000);
//        animationDrawable.startService();
    }

    private  void playSound(Context context, Uri alert){
        mMediaPlayer = new MediaPlayer();
        try{
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0){
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        }catch (IOException e){
            Log.i("MoAlarmReceiver", "No audio files are Found!");
        }
    }
    //
//
//
    @SuppressLint("InvalidWakeLockTag")
    protected void createWakeLocks(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Loneworker - PARTIAL WAKE LOCK");
    }

    // Called implicitly when device is about to sleep or application is backgrounded
    protected void onPause(){
        super.onPause();
        partialWakeLock.acquire();

    }

    // Called implicitly when device is about to wake up or foregrounded
    protected void onResume(){
        super.onResume();
        if(fullWakeLock.isHeld()){
            fullWakeLock.release();
        }
        if(partialWakeLock.isHeld()){
            partialWakeLock.release();
        }

    }

    // Called whenever we need to wake up the device
    public void wakeDevice() {
        if ((fullWakeLock != null) &&           // we have a WakeLock
                (fullWakeLock.isHeld() == false)) {  // but we don't hold it
            fullWakeLock.acquire();
        }


        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
    }
    //
    private Uri getAlarmUri(){
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null){
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null){
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return  alert;
    }
    //
    protected  void onStop(){
        super.onStop();
        if(partialWakeLock.isHeld()){
            partialWakeLock.release();
        }
        if(fullWakeLock.isHeld()){
            fullWakeLock.release();
        }

    }
    //
//
//
    private void statusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    //
    private void activateWakeLocks(){
        try{
            this.createWakeLocks();
            this.wakeDevice();
        }catch(Exception e){}
    }


    private void window() {
        try{
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,

                    WindowManager.LayoutParams.FLAG_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }catch(Exception e){

        }
    }


    private void stopAlarm() {
//        mMediaPlayer.stop();
        finish();
    }
}
