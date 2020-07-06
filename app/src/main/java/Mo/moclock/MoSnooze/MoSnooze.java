package Mo.moclock.MoSnooze;

import android.content.Context;

import Mo.moclock.MoIO.MoFile;
import Mo.moclock.MoIO.MoLoadable;
import Mo.moclock.MoIO.MoSavable;

public class MoSnooze implements MoSavable, MoLoadable {

    private final String SEP_KEY = "&wfoih&";

    private MoSnoozeInterval interval;
    private boolean isActive;

    public MoSnooze(){}

    public MoSnooze(int wt, int r, boolean active){
        this.interval = new MoSnoozeInterval(wt,r);
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public MoSnoozeInterval getInterval() {
        return interval;
    }

    public void setInterval(MoSnoozeInterval interval) {
        this.interval = interval;
    }


    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(SEP_KEY,this.isActive,this.interval.getData());
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
        this.isActive = Boolean.parseBoolean(comps[0]);
        this.interval.load(comps[1],context);
    }
}
