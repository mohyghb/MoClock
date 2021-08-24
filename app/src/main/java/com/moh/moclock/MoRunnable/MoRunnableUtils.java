package com.moh.moclock.MoRunnable;

public class MoRunnableUtils {

    /**
     * runs the runnable if it is not null
     * @param r runnable to be ran
     */
    public static void runIfNotNull(Runnable r){
        if(r!=null){
            r.run();
        }
    }

}
