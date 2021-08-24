package com.moh.moclock.MoSensor;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.moofficial.moessentials.MoEssentials.MoLog.MoLog;

public abstract class MoMovementListener implements SensorEventListener {

    private static final int STOPPED_MOVEMENT_WITH_VIBRATION = 4;
    private static final int STOPPED_MOVEMENT_WITHOUT_VIBRATION = 2;
    private static int STOPPED_MOVEMENT = STOPPED_MOVEMENT_WITHOUT_VIBRATION;

    private static final int SLOW_MOVEMENT = 30;

    private static final int TIME_THRESHOLD = 50;
    private static final int COUNTER = 3;
    private static final int COUNTER_MOVING = 3;


    enum counterType{
        STOP,MOVING,SLOW
    }

    // private OnShakeListener mListener;

    private float lastX;
    private float lastY;
    private float lastZ;
    private long lastUpdate;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private boolean vibrateOnShake;
    private long vibrateTime;

    private float vibrateThreshold = 0;
    private Context context;
    private boolean phoneIsVibrating;


    private int stopCounter,slowCounter,movingCounter;

    /**
     *
     * @param phoneIsVibrating if this is true, then the vibration might cause the phone to move,
     *                        and this class will detect a wrong movement, therefore, we need to increase the
     *                         threshold for stopped movement if this is true
     */
    public MoMovementListener(boolean phoneIsVibrating) {
        this.lastX = 0;
        this.lastY = 0;
        this.lastZ = 0;
        this.lastUpdate = 0;
        this.vibrateOnShake = false;
        this.phoneIsVibrating = phoneIsVibrating;
        init();
    }


    private void init(){
        if(this.phoneIsVibrating){
            // increase the stopped threshold
            STOPPED_MOVEMENT = STOPPED_MOVEMENT_WITH_VIBRATION;
            MoLog.print("PHONE IS VIBRATION +" +STOPPED_MOVEMENT);
        }else{
            // set default value
            STOPPED_MOVEMENT = STOPPED_MOVEMENT_WITHOUT_VIBRATION;
            MoLog.print("DEFAULT +" +STOPPED_MOVEMENT);
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start(Context context){

        stop();
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                // success! we have an accelerometer

                // Toast.makeText(context,"suc",Toast.LENGTH_LONG).show();
                accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_ACCELEROMETER,
                        SensorManager.SENSOR_DELAY_GAME);
                vibrateThreshold = accelerometer.getMaximumRange() / 2;
            } else {
                //Toast.makeText(getBaseContext(),"Failed",Toast.LENGTH_LONG).show();
                // fai! we dont have an accelerometer!
            }
        }
    }


    public void stop(){
        // stops receiving data
        if(this.sensorManager!=null){
            sensorManager.unregisterListener(this);
        }

    }



    private void vibrate(long milli){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(v == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milli, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milli);
        }
    }

    private void checkToVibrate(){
        if(this.vibrateOnShake){
            // vibrate the device
            long DEFAULT_VIBRATION_TIME = 200;
            vibrate(this.vibrateTime==0? DEFAULT_VIBRATION_TIME :vibrateTime);
        }
    }


    public void onSensorChanged(SensorEvent event) {
        long now = System.currentTimeMillis();


        if ((now - lastUpdate) > TIME_THRESHOLD) {
            long diffTime = (now - lastUpdate);
            //     lastUpdate = curTime;

            float x = Math.abs(event.values[0]);
            float y = Math.abs(event.values[1]);
            float z = Math.abs(event.values[2]);

            float speed = Math.abs(x+y+z - this.lastX - this.lastY- this.lastZ) / diffTime * 10000;
           // System.out.println("speed = " + speed);
            if(speed < STOPPED_MOVEMENT){
                incCounter(counterType.STOP);
            }else if(speed < SLOW_MOVEMENT){
                incCounter(counterType.SLOW);
            }else{
                incCounter(counterType.MOVING);
            }
            // call them to make this work
            this.callListeners();
            lastUpdate = now;
            this.lastX = x;
            this.lastY = y;
            this.lastZ = z;
        }

    }

    private void callListeners(){
        if(stopCounter > COUNTER){
            reset();
            onStoppedMoving();
        }else if(slowCounter > COUNTER){
            reset();
            onSlowMovement();
        }else if(movingCounter > COUNTER_MOVING){
            reset();
            onMoving();
        }
    }

    private void reset(){
        stopCounter = 0;
        slowCounter = 0;
        movingCounter = 0;
    }

    private void incCounter(counterType c){
        switch (c){
            case MOVING:
                movingCounter++;
                slowCounter = 0;
                stopCounter = 0;
                break;
            case SLOW:
                movingCounter = 0;
                slowCounter ++;
                stopCounter = 0;
                break;
            case STOP:
                movingCounter = 0;
                slowCounter = 0;
                stopCounter ++;
                break;
        }
    }


    /**
     * when this class detects a movement
     */
    public abstract void onStoppedMoving();
    public abstract void onSlowMovement();
    public abstract void onMoving();




}
