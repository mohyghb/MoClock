package Mo.moclock.MoClock.MoClockSugestions;

import android.content.Context;

import java.util.Objects;

import Mo.moclock.MoClock.MoAlarmClock;
import Mo.moclock.MoDate.MoDate;
import Mo.moclock.MoDate.MoTimeDifference;
import Mo.moclock.MoIO.MoFile;
import Mo.moclock.MoIO.MoLoadable;
import Mo.moclock.MoIO.MoSavable;

public class MoClockSuggestion implements MoSavable, MoLoadable {


    private static final String SEP_KEY = "moc&lo&cksuggest&ion&";

    // 10 minutes tolerance
    private static final Long TOLERANCE = 1000L * 60 * 10;
    // ONE HOUR difference makes it so that we don't suggest the exact time
    // but suggest the difference
    private static final Long MAX_DIFF_SET_TIME = 1000L * 60 * 59;



    private MoDate creationMilli;
    private MoDate setFor;



    public MoClockSuggestion(MoAlarmClock c){
        this.creationMilli = new MoDate();
        this.setFor = c.getDate();
    }

    public MoClockSuggestion(String data,Context c){
        this.creationMilli = new MoDate();
        this.setFor = new MoDate();
        this.load(data,c);
    }



    /**
     * returns true if this suggestion would be a good suggestion to have on the
     * suggestion list, based on the time they are currently setting their clock
     * @param l is system.currentTimeInMilli
     * @return
     */
    public float isInRange(MoDate l){
        return this.creationMilli.isInTimeRange(l,TOLERANCE);
    }


    public MoDate getCreationMilli() {
        return creationMilli;
    }

    public MoDate getSetFor() {
        return setFor;
    }



    public String getSuggestedTime(){
        float diff = this.creationMilli.isInTimeRange(this.setFor,MAX_DIFF_SET_TIME);
        if(diff > 0){
            // then it is within the range of setting the difference
            return new MoTimeDifference(this.creationMilli,this.setFor).getReadableDiff() + " from now";
        }else{
            return this.setFor.getReadableTime();
        }
    }


    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        String[] com = MoFile.loadable(SEP_KEY,data);
        this.creationMilli.load(com[0],context);
        this.setFor.load(com[1],context);
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(SEP_KEY,creationMilli.getData(), setFor.getData());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoClockSuggestion that = (MoClockSuggestion) o;
        return Objects.equals(creationMilli, that.creationMilli) &&
                Objects.equals(setFor, that.setFor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationMilli, setFor);
    }
}
