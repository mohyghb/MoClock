package Mo.moclock.MoSnooze;

import android.content.Context;

import Mo.moclock.MoIO.MoFile;
import Mo.moclock.MoIO.MoLoadable;
import Mo.moclock.MoIO.MoSavable;

public class MoSnoozeInterval implements MoSavable, MoLoadable {


    private final String SEP_KEY="&ksifj&";


    private int waitTime;
    private int repeats;

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
        return MoFile.getData(SEP_KEY,waitTime,repeats);
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        String[] comps = MoFile.loadable(SEP_KEY,data);
        this.waitTime = Integer.parseInt(comps[0]);
        this.repeats= Integer.parseInt(comps[1]);
    }
}
