package com.moh.moclock.MoClock.MoAlarmSession;

import com.moh.moclock.MoClock.MoAlarmClock;
import com.moh.moclock.MoClock.MoAlarmClockManager;
import com.moh.moclock.MoClock.MoEmptyAlarmException;

public class MoInformation {


    private  String title;
    private  String subTitle;
    private  String leftButton;
    private  String rightButton;
    private  MoInitAlarmSession.Type type;

    // only if its an alarm clock
    private int id;
    private MoAlarmClock clock;

    public MoInformation(String t,String s,MoInitAlarmSession.Type type){
        this.title = t;
        this.subTitle = s;
        this.type = type;
    }

    public MoInformation(String t,String s,MoInitAlarmSession.Type type,int i){
        this.title = t;
        this.subTitle = s;
        this.type = type;
        this.id = i;
        if(type == MoInitAlarmSession.Type.CLOCK){
            try {
                this.clock = MoAlarmClockManager.getInstance().getAlarm(id);
            } catch (MoEmptyAlarmException e) {
                //e.printStackTrace();
            }
        }
    }

    public MoAlarmClock getClock(){
        return this.clock;
    }

    public boolean isClock() {
        return this.type == MoInitAlarmSession.Type.CLOCK;
    }


    public void changeTitleIfEmpty(String t){
        if(this.title.isEmpty()){
            this.title = t;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public MoInitAlarmSession.Type getType() {
        return type;
    }

    public void setType(MoInitAlarmSession.Type type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLeftButton(String leftButton) {
        this.leftButton = leftButton;
    }

    public void setRightButton(String rightButton) {
        this.rightButton = rightButton;
    }

    public String getLeftButton() {
        return leftButton;
    }

    public String getRightButton() {
        return rightButton;
    }
}
