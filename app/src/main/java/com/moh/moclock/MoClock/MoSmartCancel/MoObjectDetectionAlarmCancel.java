package com.moh.moclock.MoClock.MoSmartCancel;

import android.app.Activity;
import android.content.Intent;

import java.util.Random;
import java.util.Vector;

import com.moh.moclock.Tensorflow.DetectorActivity;

public class MoObjectDetectionAlarmCancel extends MoSmartCancel {


    private static final String NULL_OBJECT  = "???";



    public MoObjectDetectionAlarmCancel(){

    }




    @Override
    public void show(Activity a) {
        // start the activity
        MoSmartCancel.startActivityForResult(a,new Intent(a, DetectorActivity.class),
                MoSmartCancel.OBJECT_DETECTION_CANCEL);
    }

    /**
     *
     * @return
     * a random object to be detected
     */
    public static String getARandomObject(Vector<String> objects){
        Random r = new Random();
        String obj = objects.get(r.nextInt(objects.size() - 1));
        while(obj.equals(NULL_OBJECT)){
            obj = objects.get(r.nextInt(objects.size() - 1));
        }
        return obj;
    }

    public static String getARandomObject(String[] objects){
        Random r = new Random();
        String obj = objects[r.nextInt(objects.length - 1)];
        while(obj.equals(NULL_OBJECT)){
            obj = objects[r.nextInt(objects.length - 1)];
        }
        return obj;
    }


    public static String getARandomObject(String[] objects,String old){
        Random r = new Random();
        String obj = objects[r.nextInt(objects.length - 1)];
        while(obj.equals(old)){
            obj = objects[r.nextInt(objects.length - 1)];
        }
        return obj;
    }

}
