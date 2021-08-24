package com.moh.moclock.MoRunnable;

public interface MoRunnable {


    /**
     * you can pass any arguments into this runnable
     * @param args
     * @param <T>
     */
    <T> void run(T ... args);

}
