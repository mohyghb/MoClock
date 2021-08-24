package com.moh.moclock.MoClock.MoSnooze;

import android.content.Context;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;

public class MoSnoozeInterval implements MoSavable, MoLoadable {





    // number of minutes to set the
    int waitTime;
    int repeats;

    public MoSnoozeInterval(int wt, int r){
        this.repeats = r;
        this.waitTime = wt;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(waitTime,repeats);
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        String[] comps = MoFile.loadable(data);
        this.waitTime = Integer.parseInt(comps[0]);
        this.repeats= Integer.parseInt(comps[1]);
    }
}
