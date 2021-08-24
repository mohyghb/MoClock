package com.moh.moclock.MoInflatorView;

import android.view.View;

public interface MoViewDisplayable {


    /**
     * takes args and makes the class which implements it
     * displayable for the user
     * for example this can be used in the U.I
     * @param args
     * arguments that are passed to this method via other classes
     */
     View display(Object ... args) ;


}
