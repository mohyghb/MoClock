package com.moh.moclock.MoInflatorView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class MoInflaterView {


    public static View inflate(int layout, Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        return inflater.inflate(layout,null);
    }

}
