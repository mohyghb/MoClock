package com.moh.moclock.MoGoogleSearch.MoDisplay;

public interface MoDisplayable {


    /**
     * takes args and makes the class which implements it
     * displayable for the user
     * for example this can be used in the U.I
     * @param args
     * arguments that are passed to this method via other classes
     * @param <T>
     *     T is the type of the parameter that is being passed
     *     it can be any type
     *     later on you can cast it down
     */
    <T> void display(T... args) ;


}
