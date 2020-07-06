package Mo.moclock.MoClock.MoClockSugestions;

import android.content.Context;

import java.util.Calendar;
import java.util.Objects;

import Mo.moclock.MoClock.MoAlarmClockManager;
import Mo.moclock.MoDate.MoDate;

public class MoPrioritySuggestion {

    private static final String MINUTE = "minute";

    private String time;
    private MoClockSuggestion suggestion;
    private float priority;


    public MoPrioritySuggestion(MoClockSuggestion suggestion, float priority) {
        this.suggestion = suggestion;
        this.priority = priority;
        this.time = suggestion.getSuggestedTime();
    }

    public MoClockSuggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(MoClockSuggestion suggestion) {
        this.suggestion = suggestion;
    }

    public float getPriority() {
        return priority;
    }

    public void setPriority(float priority) {
        this.priority = priority;
    }

    public String getTime() {
        return time;
    }

    /**
     * makes an alarm from this suggestion
     */
    public void createAlarm(Context context){
        MoDate date = new MoDate();
        injectDate(date);
        MoAlarmClockManager.getInstance().createAlarm(context,"",date,true,true,true);
    }

    /**
     * injects the suggestion into a mo date
     * @param d
     */
    private void injectDate(MoDate d){
        // TODO stub
        if(this.time.contains(MINUTE)){
            // then parse the time outta that
            String[] sp = this.time.split(MINUTE);
            d.add(Calendar.MINUTE,Integer.parseInt(sp[0].trim()));
        }else{
            MoDate setFor = suggestion.getSetFor();
            d.set(Calendar.MINUTE,setFor.get(Calendar.MINUTE));
            d.set(Calendar.HOUR_OF_DAY,setFor.get(Calendar.HOUR_OF_DAY));
            while(d.isBeforeCurrentTime()){
                d.add(Calendar.DATE,1);
            }
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoPrioritySuggestion that = (MoPrioritySuggestion) o;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }
}
