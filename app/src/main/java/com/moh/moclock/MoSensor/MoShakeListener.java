package com.moh.moclock.MoSensor;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.preference.PreferenceManager;

import com.moh.moclock.R;

public abstract class MoShakeListener implements SensorEventListener {

//    private static final int SHAKE_THRESHOLD = 780;
//    private static final int TIME_BETWEEN_SHAKE = 120;
    private static final int FORCE_THRESHOLD = 830;
//    private static final int FORCE_THRESHOLD = 3000;
    private static final int SENSITIVITY_FORCE = 200;
    private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 200;
    private static final int SHAKE_COUNT = 4;
    private static long DEFAULT_VIBRATION_TIME = 200;

    // private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;
    private float lastX;
    private float lastY;
    private float lastZ;
    private long lastUpdate;

    private int sensitivity;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private boolean vibrateOnShake;
    private long vibrateTime;

    private float vibrateThreshold = 0;
    private Context context;

    private long mLastShake;
    private long mLastForce;


    public MoShakeListener() {
        this.lastX = 0;
        this.lastY = 0;
        this.lastZ = 0;
        this.lastUpdate = 0;
        this.vibrateOnShake = false;
        this.sensitivity = FORCE_THRESHOLD;
    }

    public MoShakeListener(boolean v){
        this.lastX = 0;
        this.lastY = 0;
        this.lastZ = 0;
        this.lastUpdate = 0;
        this.vibrateOnShake = v;
        this.sensitivity = FORCE_THRESHOLD;
    }

    public MoShakeListener(boolean v, long vt){
        this.lastX = 0;
        this.lastY = 0;
        this.lastZ = 0;
        this.lastUpdate = 0;
        this.vibrateOnShake = v;
        this.vibrateTime = vt;
        this.sensitivity = FORCE_THRESHOLD;
    }

    /**
     *
     * @param scale
     * is a number between 0 and 100 and we
     */
    private void setSensitivity(double scale){
//        this.sensitivity = FORCE_THRESHOLD;
        if(scale == 50){
            // then threash hold is the same
        }else{
            if(scale > 50){
                // (more sensitive) subtract to sensitivty
                this.sensitivity-= scale/100*SENSITIVITY_FORCE;
            }else{
                // (less sensitive )add from sensitivity
                // multiply it by 2 bc of testing
                this.sensitivity+= 2*(100-scale)/100*SENSITIVITY_FORCE;
            }
        }
//        System.out.println("senstivity=========================== " + sensitivity);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void setUpSensitivity(Context context){
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        int sens = s.getInt(context.getString(R.string.shake_sensitivity),50);
        setSensitivity(sens);
    }

    public void start(Context context){

        stop();
        setUpSensitivity(context);
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

    public void checkToVibrate(){
        if(this.vibrateOnShake){
            // vibrate the device
            vibrate(this.vibrateTime==0?DEFAULT_VIBRATION_TIME:vibrateTime);
        }
    }


    public void onSensorChanged(SensorEvent event) {
        //System.out.println("--------------MoShakeListener----------------");
        long now = System.currentTimeMillis();
        // only allow one update every 100ms.


        //&& mShakeTimestamp + SHAKE_SLOP_TIME_MS <= curTime
//        if ((curTime - lastUpdate) > TIME_BETWEEN_SHAKE ) {



            if ((now - mLastShake) < SHAKE_TIMEOUT) {
                mShakeCount = 0;
                return;
            }

            if ((now - lastUpdate) > TIME_THRESHOLD) {
                long diffTime = (now - lastUpdate);
                 //     lastUpdate = curTime;

                float x = Math.abs(event.values[0]);
                float y = Math.abs(event.values[1]);
                float z = Math.abs(event.values[2]);

                float speed = Math.abs(x+y+z - this.lastX - this.lastY- this.lastZ) / diffTime * 10000;
                //float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
                if (speed > FORCE_THRESHOLD) {

                    if ((++mShakeCount >= SHAKE_COUNT) ) {
                        mShakeCount = 0;
                        mLastShake = now;
                        //this.checkToVibrate();
                        // on shake detected
                        this.onShakeDetected();
                    }
                    mLastForce = now;
                }else{
                    mShakeCount = 0;
                }
                lastUpdate = now;
                this.lastX = x;
                this.lastY = y;
                this.lastZ = z;
//                mLastX = values[SensorManager.DATA_X];
//                mLastY = values[SensorManager.DATA_Y];
//                mLastZ = values[SensorManager.DATA_Z];
            }



//            if (speed >= SHAKE_THRESHOLD) {
//                mShakeCount++;
//                if(mShakeCount >= SHAKE_COUNT){
//                    // shake detected
//                    shake++;
//                    System.out.println(""+shake);
//                    mShakeCount = 0;
//                    // vibrate
//                    this.checkToVibrate();
//                    // on shake detected
//                    this.onShakeDetected();
////                    Log.d("sensor", "shake detected w/ speed: " + speed);
//                }
//                mShakeTimestamp = curTime;
//                //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
//            }else{
//                mShakeCount = 0;
//            }

       // }
    }

    /**
     * when this class detects a shake
     */
    public abstract void onShakeDetected();



}
