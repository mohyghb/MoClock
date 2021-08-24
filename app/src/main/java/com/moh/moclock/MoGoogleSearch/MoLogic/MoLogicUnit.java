package com.moh.moclock.MoGoogleSearch.MoLogic;

import java.util.ArrayList;

public abstract class MoLogicUnit {


    protected MoLogicType type;
    protected String data;

    protected ArrayList<MoLogicUnit> units;

    public MoLogicUnit() {
        this.units = new ArrayList<>();
    }




    public ArrayList<MoLogicUnit> getUnits() {
        return units;
    }

    public String getData() {
        return data;
    }

    //    /**
//     * runs logic on this data
//     */
//    public abstract void run();

}
