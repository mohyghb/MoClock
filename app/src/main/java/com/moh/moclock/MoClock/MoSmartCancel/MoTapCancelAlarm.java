package com.moh.moclock.MoClock.MoSmartCancel;

import android.app.Activity;
import android.content.Intent;

import java.util.Random;

import com.moh.moclock.MoTapActivity;

public class MoTapCancelAlarm extends MoSmartCancel {



    @Override
    public void show(Activity a) {
        MoSmartCancel.startActivityForResult(a,new Intent(a, MoTapActivity.class),MoSmartCancel.TAP_CANCEL);
    }


    public static int getRandom(int max){
        Random r = new Random();
        return r.nextInt(max);
    }


}
