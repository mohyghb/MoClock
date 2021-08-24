package com.moh.moclock.MoId;

import android.content.Context;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;

import java.util.Random;

public class MoId implements MoSavable, MoLoadable {


    private int id;

    public MoId(){
        this.id = getRandomId();
    }


    public MoId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSId(){
        return id+"";
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getRandomId()
    {
        return randInt(12,1003122340);
    }

    public static int getRandomInt() {
        return (int)(Math.random() * 100000000);
    }


    public static int randInt(int min, int max) {

        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        this.id = Integer.parseInt(data);
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return this.id+"";
    }
}
