package com.moh.moclock.MoAnimation;

import android.app.Activity;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.HashMap;

import com.moh.moclock.MoId.MoId;
import com.moh.moclock.R;

public class MoAnimation {

    public static int LEFT_TO_RIGHT = R.anim.left_to_right;
    public static int RIGHT_TO_LEFT = R.anim.right_to_left;
    public static int BOTTOM_TO_TOP = R.anim.bottom_to_top;
    public static int TOP_TO_BOTTOM= R.anim.top_to_bottom;
    public static int FADE_IN = R.anim.fade_in;
    public static int FADE_OUT = R.anim.fade_out;
    public static int MOVE_LEFT = R.anim.move_left;
    public static int MOVE_RIGHT = R.anim.move_right;
    public static int MOVE_DOWN = R.anim.move_down;
    public static int TOP_TO_BOTTOM_FADE = R.anim.top_to_bottom_fade;
    public static int MOVE_UP = R.anim.move_up;
    public static int HALF_TOP_TO_BOTTOM = R.anim.half_top_to_bottom;
    public static int APPEAR = R.anim.appear;
    public static int DISAPPEAR = R.anim.disappear;
    public static int MOVE_DOWN_FADE_OUT = R.anim.move_down_fade_out;
    public static int BOTTOM_TO_TOP_FADE_IN = R.anim.bottom_to_top_fade_in;
    public static int MOVE_RIGHT_FADE_OUT = R.anim.move_right_fade_out;
    public static int MOVE_LEFT_FADE_OUT = R.anim.move_left_fade_out;
    public static int MOVE_UP_FADE_OUT = R.anim.move_up_fade_out;


    private static int ANIMATION_LIST[] = {
            LEFT_TO_RIGHT,
            RIGHT_TO_LEFT,
            FADE_IN,
            FADE_OUT,
            BOTTOM_TO_TOP,
            TOP_TO_BOTTOM,
            MOVE_LEFT,
            MOVE_RIGHT,
            MOVE_DOWN,
            TOP_TO_BOTTOM_FADE,
            MOVE_UP,
            HALF_TOP_TO_BOTTOM,
            APPEAR,
            DISAPPEAR,
            MOVE_DOWN_FADE_OUT,
            BOTTOM_TO_TOP_FADE_IN,
            MOVE_RIGHT_FADE_OUT,
            MOVE_LEFT_FADE_OUT,
            MOVE_UP_FADE_OUT
    };


    public static SparseArray<Animation> animations = new SparseArray<>();

    // only perform animation once, data is collected using ids
    //public static SparseArray<Boolean> log = new SparseArray<>();
    public static HashMap<String,Boolean> log = new HashMap<>();



    public static Animation get(int key){
        return animations.get(key);
    }


    public static void initAllAnimations(Activity a){
        for(int id: ANIMATION_LIST){
            animations.append(id,AnimationUtils.loadAnimation(a, id));
        }
    }


    public static void animate(View v, int visibility, int animation){
        if(v.getVisibility() != visibility){
            animate(v,animation);
            v.setVisibility(visibility);
        }
    }

    public static void animate(View v, int visibility, int animation,boolean shouldAnimate){
        if(v.getVisibility() != visibility && shouldAnimate){
            animate(v,animation);
        }
        v.setVisibility(visibility);
    }

    public static void animate(View v, int visibility, int animation,MoId id,boolean shouldAnimate){
        if(v.getVisibility() != visibility && shouldAnimate){
            animate(v,animation,id);
        }
        v.setVisibility(visibility);
    }


    public static void animate(View v, int visibility, int animation,MoId id){
        if(v.getVisibility() != visibility){
            animate(v,animation,id);
            v.setVisibility(visibility);
        }

    }


    // only animates if there is nothing in the log
    private static void animate(View v, int animation){
        String key = v.getId()+ "" + v.getX() +"" + v.getY() + "" + v.getZ() ;
        Boolean shouldAnimate = log.get(key);
        if(shouldAnimate == null){
            v.startAnimation(get(animation));
            // we dont do it again
            log.put(key,true);
        }
    }


    // only animates if there is nothing in the log
    private static void animate(View v, int animation, MoId moId){
        String key = v.getId()+ moId.getSId() ;
        Boolean shouldAnimate = log.get(key);
        if(shouldAnimate == null){
            v.startAnimation(get(animation));
            // we dont do it again
            log.put(key,true);
        }
    }

    // animates as much as you want
    // does not consider if this view has been animated before or not
    public static void animateNoTag(View v, int visibility,int animation){
        if(v.getVisibility() == visibility){
            // dont animate because the view is already has that visibility
            return;
        }
        v.startAnimation(get(animation));
        v.setVisibility(visibility);
    }



    // only accounts for delay when the view wants to become visible
    // else calls the overloaded version of itself
    public static void animateNoTag(View v, int visibility,int animation,int delay){
        if(visibility == View.VISIBLE){
            final android.os.Handler handler = new Handler();
            handler.postDelayed(() -> animateNoTag(v,visibility,animation), delay);
        }else{
            animateNoTag(v,visibility,animation);
        }
    }


    public static void clearLog(){
        log.clear();
    }


}
