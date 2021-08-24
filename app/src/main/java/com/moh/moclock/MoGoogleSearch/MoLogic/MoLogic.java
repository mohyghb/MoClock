package com.moh.moclock.MoGoogleSearch.MoLogic;

import java.util.ArrayList;

public class MoLogic {


    public static final String AND = "mo_and";
    public static final String OR = "mo_or";


    /**
     *  or ''''' or ''''''' or
     *  each or might or might not have ANDs
     * @param data
     * @return
     */
    public static ArrayList<MoLogicUnit> runGetSimple (String data) {
        ArrayList<MoLogicUnit> logicUnits = new ArrayList<>();
        // splitting or arguments first
        String[] orSep = data.split(OR);
        for (String s : orSep) {
            MoLogicOr or = new MoLogicOr(s);
            String[] ands = s.split(AND);
            for (String a : ands) {
                MoLogicAnd andLogic = new MoLogicAnd(a);
                or.units.add(andLogic);
            }
            logicUnits.add(or);
        }
        return logicUnits;
    }


    public MoLogicType getType(String type){
        switch (type) {
            case MoLogic.OR:
                return MoLogicType.OR;
            case MoLogic.AND:
                return MoLogicType.AND;
        }
        return null;
    }


    public static String getMoLogicAnds(String ... texts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <texts.length; i++) {
            stringBuilder.append(texts[i]);
            if (i != texts.length - 1){
                stringBuilder.append(MoLogic.AND);
            }
        }
        return stringBuilder.toString();
    }




}
