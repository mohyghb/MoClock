package com.moh.moclock.MoVibration;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;


public class MoVibration implements MoLoadable {

    public static Vibrator v;

    private final String SEP_KEY = "&afsvdviol&";

    private MoVibrationTypes type;

    private boolean isActive;

    public MoVibration(MoVibrationTypes t, boolean isActive){
        this.type = t;
        this.isActive = isActive;
    }

    public static void vibrate(Context context,long milli){
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(v == null)
            return;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long[] timing = new long[]{1000,1500,1000};
            v.vibrate(VibrationEffect.createWaveform(timing, 1));
        } else {
            //deprecated in API 26
            v.vibrate(milli);
        }
    }

    public static void vibrateOnce(Context context,long milli){
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

    public static void cancel(){
        if(v!=null){
            v.cancel();
        }
    }

    /**
     * cancels the vibration if b is true
     * @param b
     */
    public static void mute(Context c,boolean b,long milli){
        if(b){
            cancel();
        }else{
            vibrateOnce(c,milli);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public MoVibrationTypes getType() {
        return type;
    }

    public void setType(MoVibrationTypes type) {
        this.type = type;
    }


    @NonNull
    @Override
    public String toString() {
        return MoFile.getData(this.type,this.isActive);
    }


    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        String[] comps = MoFile.loadable(data);
        this.isActive = Boolean.parseBoolean(comps[1]);
        /// change type to integer
    }
}
