package com.moh.moclock.MoColor;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatDelegate;

public class MoColor {


    // day and night values
    public static int[] color_text_on_highlight =
            new int[]{Color.parseColor("#4caf50"),Color.parseColor("#a7ffeb")};

    public static int[] color_text_disabled =
            new int[]{Color.parseColor("#F28E8E8E"),Color.parseColor("#F28E8E8E")};


    public static int[] color_text_on_normal =
            new int[]{Color.parseColor("#E9212427"),Color.parseColor("#F2FAFAFA")};

    public static int getColor(int[] id,Context context){
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            return id[1];
        }else{
            int s = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (s){
                case Configuration.UI_MODE_NIGHT_YES:
                    return id[1];
                    case Configuration.UI_MODE_NIGHT_NO:
                        return id[0];
            }

            return id[0];
        }
    }

}
