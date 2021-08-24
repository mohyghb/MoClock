package com.moh.moclock.MoClock.MoStopWatch;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.moh.moclock.MoDate.MoTimeUtils;
import com.moh.moclock.MoInflatorView.MoInflaterView;
import com.moh.moclock.MoInflatorView.MoViewDisplayable;
import com.moh.moclock.MoUI.MoTextView;
import com.moh.moclock.R;

public class MoLap implements MoViewDisplayable {

    private long timeInMilli;
    // the difference between this and the last molap
    private long lastDiff;

    private boolean doneAnimation;


    public MoLap(long tim,long ld){
        this.timeInMilli = tim;
        this.lastDiff = ld;
        this.doneAnimation = false;
    }

    public long getTimeInMilli() {
        return timeInMilli;
    }

    public void setTimeInMilli(long timeInMilli) {
        this.timeInMilli = timeInMilli;
    }

    public long getLastDiff() {
        return lastDiff;
    }

    public void setLastDiff(long lastDiff) {
        this.lastDiff = lastDiff;
    }

    public boolean isDoneAnimation() {
        return doneAnimation;
    }

    public void setDoneAnimation(boolean doneAnimation) {
        this.doneAnimation = doneAnimation;
    }

    /**
     * takes args and makes the class which implements it
     * displayable for the user
     * for example this can be used in the U.I
     *
     * @param args arguments that are passed to this method via other classes
     */
    @Override
    public View display(Object... args) {
        Context c = (Context) args[0];

        int index = (int) args[1] + 1;
        int status = (int) args[2];

        View view = MoInflaterView.inflate(R.layout.lap_item,c);

        TextView indexTv = view.findViewById(R.id.index_lap);
        TextView timeLap = view.findViewById(R.id.time_lap);
        TextView diffTimeLap = view.findViewById(R.id.diff_time_lap);


        indexTv.setText(index<10?"0"+index:index+"");
        diffTimeLap.setText(MoTimeUtils.convertToStopWatchFormat(timeInMilli));
        timeLap.setText(MoTimeUtils.convertToStopWatchFormat(lastDiff));

        switch (status){
            case MoLapManager.HIGHEST_STATUS:
                MoTextView.syncColor(c.getColor(R.color.error_color),indexTv,timeLap,diffTimeLap);
                break;
            case MoLapManager.LOWEST_STATUS:
                MoTextView.syncColor(c.getColor(R.color.resumeButton),indexTv,timeLap,diffTimeLap);
                break;
            case MoLapManager.NONE_STATUS:
                MoTextView.syncColor(c.getColor(R.color.color_text_disabled),indexTv,timeLap,diffTimeLap);
                break;
        }

        return view;
    }


}
