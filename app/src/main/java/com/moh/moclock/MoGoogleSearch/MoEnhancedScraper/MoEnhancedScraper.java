package com.moh.moclock.MoGoogleSearch.MoEnhancedScraper;

import java.util.ArrayList;
import java.util.List;

import com.moh.moclock.MoGoogleSearch.MoComplexData.MoComplexNode;
import com.moh.moclock.MoGoogleSearch.MoLogic.MoLogic;
import com.moh.moclock.MoGoogleSearch.MoLogic.MoLogicUnit;



public class MoEnhancedScraper {


    /**
     * @todo need to add a funcitonallity where you can parse multiple things
     *   before you pass it to the next layer
     *   this is the solution for:
     *   "definition of love"
     *   each type has SIMILAR or OPPOSITE
     *   but when we parse words, every word is not in it's category
     *   this AND STATIC FIELD will help us achieve multiple separation
     */







    public static void scrape(int keyIndex, List<String> keys, String data, MoComplexNode genericNode){
        List<String> stop = new ArrayList<>();
        stop.add("</span>");
        stop.add("</div>");
        if (keyIndex >= keys.size()) {
            return;
        }

        String sepKey = keys.get(keyIndex);
        String[] sepData = MoEnhancedScraper.separate(sepKey,data);

        for (int i = 1; i < sepData.length; i++) {
            String value = MoString.normalizeString(MoString.continueUntil(stop,sepData[i]));
            //String remData = sepData[i];
            MoComplexNode cn = new MoComplexNode(value);
            scrape(keyIndex + 1, keys, sepData[i],cn);
            genericNode.add(cn);
        }
    }






    private static String[] separate(String splitter, String text) {
        ArrayList<MoLogicUnit> units = MoLogic.runGetSimple(splitter);
        ArrayList<String> keys = new ArrayList<>();
        for (MoLogicUnit unit : units) {
            ArrayList<MoLogicUnit> insideUnits = unit.getUnits();
            for (MoLogicUnit inUnit : insideUnits) {
                keys.add(inUnit.getData());
            }
        }
        return MoString.MoSplit(keys,text);
    }



}
