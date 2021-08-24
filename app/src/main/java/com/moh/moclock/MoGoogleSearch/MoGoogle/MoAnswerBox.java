package com.moh.moclock.MoGoogleSearch.MoGoogle;


import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoComplexData.MoComplexNode;
import com.moh.moclock.MoGoogleSearch.MoDisplay.MoDisplayable;
import com.moh.moclock.MoGoogleSearch.MoEnhancedScraper.MoEnhancedScraper;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoQuickAnswerBox;

public abstract class MoAnswerBox implements MoDisplayable {


    // how is the weather
    public static final String WEATHER = "Weather";

    public static final String TIME = "Local Time";

    public static final String LOCAL_TIME_CONVERSION = "Local Time Conversion";

    // what is 2+2
    public static final String CALCULATOR = "Calculator";


    // how old is the universe
    public static final String SNIPPET = "Featured";

    // age of sun
    public static final String KNOWLEDGE = "Knowledge";

    //how long is end game
    public static final String DESCRIPTION = "Description";

    // does not have a code for itself
    public static final String DICTIONARY = "placeholder=\"Search for a word\"";

    public static final String UNIT = "Unit";

    public static final String CURRENCY = "Currency";




    // any web links
    public static final String WEB_RESULT = "Web";



    public static final String QUICK_ANSWER = MoQuickAnswerBox.ANSWER_CODE;


    protected String data;
    protected MoComplexNode complexNode;

    public MoAnswerBox(String d) {
        this.data = d;
        this.complexNode = new MoComplexNode();
        MoEnhancedScraper.scrape(0,getSplitters(),d, complexNode);
    }

    public ArrayList<String> getDataAt(Integer ... indexes){
        if(complexNode == null)
            return new ArrayList<>();
        return complexNode.get(indexes).getAllValues();
    }

    public ArrayList<String> getWholeData(){
        if(complexNode == null)
            return new ArrayList<>();
        return complexNode.get(0).getAllValues();
    }


    public abstract ArrayList<String> getSplitters();






}
