package com.moh.moclock.MoDate;

public class MoTimeDifference {

    private static final String NO_DIFF = "";

    private int hourDiff;
    private int minuteDiff;


    public MoTimeDifference(MoDate d,MoDate d1){
        long milli = Math.abs(d.timeInMilli() - d1.timeInMilli());
        int[] res = MoTimeUtils.convertMilliInt(milli);
        this.hourDiff = res[0];
        this.minuteDiff = res[1];
    }

    public int getHourDiff() {
        return hourDiff;
    }

    public int getMinuteDiff() {
        return minuteDiff;
    }

    public String getReadableDiff(){
        StringBuilder sb = new StringBuilder();
        if(hourDiff!=0){
            sb.append(this.hourDiff).append(" ").append(hourDiff==1?"hour":"hours");
        }
        if(sb.length()!=0){
            // for example space after 2 hours
            sb.append(" ");
        }
        if(minuteDiff!=0){
            sb.append(this.minuteDiff).append(" ").append(minuteDiff==1?"minute":"minutes");
        }
        return sb.length()==0? NO_DIFF :sb.toString();
    }


}
