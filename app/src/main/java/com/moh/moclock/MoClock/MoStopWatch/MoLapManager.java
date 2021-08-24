package com.moh.moclock.MoClock.MoStopWatch;

import java.util.ArrayList;
import java.util.List;

public class MoLapManager {

    public static final int NONE_STATUS = 0;
    public static final int LOWEST_STATUS = 1;
    public static final int HIGHEST_STATUS = 2;

    private List<MoLap> laps;
    private MoLap lowest,highest;


    public MoLapManager() {
        this.laps = new ArrayList<>();
    }

    public List<MoLap> getLaps() {
        return laps;
    }

    public String getLapCounter() {
        if (!laps.isEmpty()) {
            return "Lap - " + laps.size();
        }
        return null;
    }

    public int size() {
        return laps.size();
    }

    /**
     * adds the lap to the list
     * and accordingly adjusts highest and lowest laps
     * @param lap
     */
    public void add(MoLap lap){
        this.laps.add(lap);
        updateHighLow(lap);
    }

    private void updateHighLow(MoLap lap){
        if(laps.size() >= 3){
            // if there is more than 2 laps then we can say the highest and lowest
            if(laps.size() == 3){
                // for the first time we need to consider the other two laps
                for(int i = 0; i < laps.size(); i++){
                    adjustHighLow(laps.get(i));
                }
            }else{
                // we just adjust as we go
                adjustHighLow(lap);
            }
        }
    }


    private void adjustHighLow(MoLap lap){
        if(lowest == null){
            // it is lower than the lowest
            this.lowest = lap;
        }else if(highest == null){
            // it is higher than the highest
            this.highest = lap;
        }else if(lap.getLastDiff() < lowest.getLastDiff()){
            // it is lower than the lowest
            this.lowest = lap;
        }else if(lap.getLastDiff() > highest.getLastDiff()){
            // it is higher than the highest
            this.highest = lap;
        }
    }


    /**
     * returns whether the lap is highest, lowest, or none
     * @return
     */
    public int getStatus(MoLap lap){
        if(this.highest == lap){
            return HIGHEST_STATUS;
        }else if(this.lowest == lap){
            return LOWEST_STATUS;
        }else{
            return NONE_STATUS;
        }
    }


    public void reset(){
        this.laps.clear();
        this.lowest = null;
        this.highest = null;
    }

}