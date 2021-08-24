package com.moh.moclock.MoDate;

import android.content.Context;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class MoDate implements MoSavable, MoLoadable {

    private final String SEP_KEY = "&sfsdcsldv&";


    Calendar calendar;

    public MoDate(){
        this.calendar = Calendar.getInstance();
        // automatically sets it to zero
        this.calendar.set(Calendar.SECOND,0);

    }


    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }


    public long timeInMilli(){
        return (long)(this.calendar.get(Calendar.HOUR_OF_DAY)*MoTimeUtils.MILLI_IN_HOUR
                + this.calendar.get(Calendar.MINUTE) * MoTimeUtils.MILLI_IN_MINUTE
                + this.calendar.get(Calendar.SECOND) * MoTimeUtils.MILLI_IN_SECOND);
    }

   public float isInTimeRange(MoDate date, long tolerance){
        long tim = this.timeInMilli();
        long tim1 = date.timeInMilli();
        if(tim == tim1) {
            return 1f;
        }else{
            return (1f - (float)Math.abs(tim - tim1)/tolerance);
        }
//        } else if(tim1 > tim && tim+tolerance > tim1){
//            // then it is in the upper bound of range
//            return 1f - (float)(tim1 - tim)/tolerance;
//        } else if(tim1 < tim && tim - tolerance < tim1){
//            return 1f - (float)(tim - tim1)/tolerance;
//        }
        //return 0f;
   }

    public String getReadableTime(){
        int minute = this.calendar.get(Calendar.MINUTE);
        int hour  = this.calendar.get(Calendar.HOUR_OF_DAY);
        StringBuilder time = new StringBuilder();
        if(hour < 10){
            time.append(String.format("0%d:",hour));
        }else{
            time.append(String.format("%d:",hour));
        }

        if(minute < 10){
            time.append(String.format("0%d",minute));
        }else{
            time.append(String.format("%d",minute));
        }

        return time.toString();
    }

    // returns [hourOfDay,minute]
    public int[] getHourMinute(){
        return new int[]{calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)};
    }

    public static String getReadableTime(Calendar calendar){
        int minute = calendar.get(Calendar.MINUTE);
        int hour  = calendar.get(Calendar.HOUR_OF_DAY);
        StringBuilder time = new StringBuilder();
        if(hour < 10){
            time.append(String.format("0%d:",hour));
        }else{
            time.append(String.format("%d:",hour));
        }

        if(minute < 10){
            time.append(String.format("0%d",minute));
        }else{
            time.append(String.format("%d",minute));
        }

        return time.toString();
    }


    public static String getReadable(Calendar c){
        return getReadableDate(c) + " at " + getReadableTime(c);
    }



    public void set(int field, int value){
        this.calendar.set(field,value);
    }
    public int get(int field) {
        return this.calendar.get(field);
    }
    public void add(int field, int value){
        this.calendar.add(field,value);
    }

    /**
     * returns true if this mo date is before the current time
     * of the user
     * @return
     */
    public boolean isBeforeCurrentTime(){
        return this.calendar.before(Calendar.getInstance());
    }

    public void setTimeZone(TimeZone timeZone){
        long time = calendar.getTimeInMillis();
        calendar = new GregorianCalendar(timeZone);
        calendar.setTimeInMillis(time);
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
        this.calendar.set(Calendar.YEAR,Integer.parseInt(comps[0]));
        this.calendar.set(Calendar.MONTH,Integer.parseInt(comps[1]));
        this.calendar.set(Calendar.DATE,Integer.parseInt(comps[2]));
        this.calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(comps[3]));
        this.calendar.set(Calendar.MINUTE,Integer.parseInt(comps[4]));
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(this.calendar.get(Calendar.YEAR),
                this.calendar.get(Calendar.MONTH),
                this.calendar.get(Calendar.DAY_OF_MONTH),
                this.calendar.get(Calendar.HOUR_OF_DAY),
                this.calendar.get(Calendar.MINUTE));
    }


    // difference between this.calendar and c
    // stored this way
    // [years,months,days,hours,minutes]
    // all values are absolute values
    private int[] difference(Calendar c){
        int[] r = new int[5];
        double differenceHourMinute = Math.abs(MoTimeUtils.convertToMilli(c) - MoTimeUtils.convertToMilli(calendar));
        int[] t = MoTimeUtils.convertMilliInt((long)differenceHourMinute);

        r[0] = Math.abs(this.calendar.get(Calendar.YEAR) - c.get(Calendar.YEAR));
        r[1] = Math.abs(this.calendar.get(Calendar.MONTH) - c.get(Calendar.MONTH));
        r[2] = Math.abs(this.calendar.get(Calendar.DATE) - c.get(Calendar.DATE));
        r[3] = t[0];
        r[4] = t[1];
        return r;
    }




    public String getReadableDifference(Calendar c){
        int[] r = difference(c);
        if(r[0] != 0 || r[1] != 0 || r[2] != 0){
            // big difference
            // just print the readable c
            return MoDate.getReadable(this.calendar) ;
        }else{
            // printing the difference
            StringBuilder s = new StringBuilder();
//            if(r[2] != 0){
//                // add days
//                s.append(r[2]).append(" days");
//            }
            if(r[3] != 0){
//                if(r[2]!=0){
//                    s.append(", ");
//                }
                s.append(r[3]).append(r[3] ==1 ? " hour":" hours");
            }
            if(r[4] != 0){
                if(r[3]!=0){
                    s.append(" and ");
                }
                s.append(r[4]).append(r[4]==1?" minute":" minutes");
            }
            s.append(" from now.");
            return s.toString();
        }
    }





    public static boolean isTimeBefore(Calendar c, Calendar c1){
//        boolean b = c.getTime().getTime() < c1.getTime().getTime();
//        return b;
        int chour = c.get(Calendar.HOUR_OF_DAY);
        int c1hour = c1.get(Calendar.HOUR_OF_DAY);
        if( chour < c1hour){
            return true;
        } else if(chour > c1hour){
            return false;
        } else {
            // hours are the same check minutes
            return c.get(Calendar.MINUTE) <= c1.get(Calendar.MINUTE);
        }

    }


    public static Calendar getNextOccuring(List<Integer> days, Calendar timeCal){
        Calendar calendar = Calendar.getInstance();



        if(isTimeBefore(timeCal, calendar) &&
                calendar.get(Calendar.DAY_OF_WEEK) == timeCal.get(Calendar.DAY_OF_WEEK)){

            /**
             * a scenario in which
             * you are on  monday
             * the alarm is for 10:30
             * currently in real life the time is 15:00
             * if you activate it, it should not be for monday, it should be for the next occuring day
             * so we increase the date by one to get what we want
             */
            calendar.add(Calendar.DATE,1);
        }


        while (!days.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
            // until the next day or today does not contain
            // one of the repeating days
            // add one day to it
            calendar.add(Calendar.DATE, 1);
        }
        // then we have the current date
        // we just need to set the current time
        calendar.set(Calendar.HOUR_OF_DAY,timeCal.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,timeCal.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,0);


        if(calendar.before(Calendar.getInstance())){
            // is before do this again
            return getNextOccuring(days,calendar);
        }

        return calendar;
    }


    public static String getReadableDate(Calendar calendar){
        return String.format("%d/%d/%d",calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String getReadableDate(){
        return String.format("%d/%d/%d",calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoDate moDate = (MoDate) o;
        return this.calendar.get(Calendar.YEAR) == moDate.calendar.get(Calendar.YEAR)
                && this.calendar.get(Calendar.MONTH) == moDate.calendar.get(Calendar.MONTH)
                && this.calendar.get(Calendar.DATE) == moDate.calendar.get(Calendar.DATE)
                && this.calendar.get(Calendar.HOUR_OF_DAY) == moDate.calendar.get(Calendar.HOUR_OF_DAY)
                && this.calendar.get(Calendar.MINUTE) == moDate.calendar.get(Calendar.MINUTE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendar);
    }
}
