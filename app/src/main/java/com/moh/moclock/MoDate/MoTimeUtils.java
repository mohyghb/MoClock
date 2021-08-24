package com.moh.moclock.MoDate;

import java.util.Calendar;

public class MoTimeUtils {



    public static final double MILLI_IN_SECOND = 1000;
    public static final double MILLI_IN_MINUTE = 60000;
    public static final double MILLI_IN_HOUR = 3.6e+6;





    public static long getTimeInMilli(String hour, String minute, String seconds){
        double h = 0 ,m = 0 ,s = 0;
        if(!hour.isEmpty())
            h = Double.parseDouble(hour);
        if(!minute.isEmpty())
            m = Double.parseDouble(minute);
        if(!seconds.isEmpty())
            s = Double.parseDouble(seconds);

        long milli = 0;

        milli+= (MILLI_IN_SECOND * s);
        milli+= (MILLI_IN_MINUTE * m);
        milli+= (MILLI_IN_HOUR * h);

        return milli;
    }


//    public static int getSecondFromMilli(long milli){
//        return (int)(milli/MILLI_IN_SECOND);
//    }
//
//    public static int getMinuteFromMilli(long milli){
//        return (int)(milli/MILLI_IN_MINUTE);
//    }
//
//    public static int getHourFromMilli(long milli){
//        return (int)(milli/MILLI_IN_HOUR);
//    }




    /**
     * returns a time format
     * [hour,minute,seconds,milliseconds]
     * @param milliseconds
     * @return
     */
    public static String[] convertMilli(long milliseconds){
        milliseconds = Math.abs(milliseconds);
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60))) % 60;
        int hours   = (int) ((milliseconds / (1000*60*60)));
        int millisec = (int) milliseconds % 1000;
        String h = hours < 10 ? "0"+hours: hours+"";
        String m = minutes < 10 ? "0"+minutes: minutes+"";
        String s = seconds < 10 ? "0"+seconds: seconds+"";
        String ms = millisec < 10? "00"+millisec: millisec < 100?"0"+millisec:millisec + "";

        return new String[]{h,m,s,ms};
    }


    public static String convertToReadableFormat(long milliseconds){
        String[] parts = convertMilli(milliseconds);
        return parts[0] + ":" + parts[1] + ":" + parts[2];
    }





    public static int[] convertMilliInt(long milliseconds){
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60))) % 60;
        int hours   = (int) ((milliseconds / (1000*60*60)));
        int millisec = (int) milliseconds % 1000;
        return new int[]{hours,minutes,seconds,millisec};
    }


    public static String convertToStopWatchFormat(long milliseconds){
        String[] times = convertMilli(milliseconds);
        return String.format("%s:%s.%s",times[1],times[2],times[3].substring(0,2));
    }


    /**
     *
     * @param c
     * converts the hour and minutes of c to milliseconds
     */
    public static double convertToMilli(Calendar c){
        return c.get(Calendar.MINUTE) * MILLI_IN_MINUTE +
                c.get(Calendar.HOUR_OF_DAY) * MILLI_IN_HOUR;
    }



}
