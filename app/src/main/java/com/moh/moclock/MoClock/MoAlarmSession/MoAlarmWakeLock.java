package com.moh.moclock.MoClock.MoAlarmSession;

import android.content.Context;
import android.os.PowerManager;

public class MoAlarmWakeLock {

    private static final String ALARM_SESSION_TAG = "MyApp::TagName";

    private static PowerManager.WakeLock sCpuWakeLock;

    public static PowerManager.WakeLock createPartialWakeLock(Context context) {
//        KeyguardManager key = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock lock = key.newKeyguardLock(ALARM_SESSION_TAG);
//        lock.disableKeyguard();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        pm.newWakeLock(
////                PowerManager.PARTIAL_WAKE_LOCK |
////                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
////                        PowerManager.ON_AFTER_RELEASE, "MoClock:Alarm_Clock");
        return pm.newWakeLock((PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE), ALARM_SESSION_TAG);
    }

    public static void acquireCpuWakeLock(Context context,long timeout) {
        if (sCpuWakeLock != null) {
            return;
        }
        sCpuWakeLock = createPartialWakeLock(context);
        sCpuWakeLock.acquire(10*1000*60);
    }

    public static void acquireScreenCpuWakeLock(Context context,long timeout) {
        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            sCpuWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, ALARM_SESSION_TAG);
        }
        sCpuWakeLock.acquire();
    }

    public static void releaseCpuLock() {
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }


    public static void turnScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //@SuppressWarnings("deprecation")
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, ALARM_SESSION_TAG);
        wakeLock.acquire();
        wakeLock.release();
    }
}
