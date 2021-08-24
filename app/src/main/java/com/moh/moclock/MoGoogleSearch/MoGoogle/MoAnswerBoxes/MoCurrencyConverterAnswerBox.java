package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;

public class MoCurrencyConverterAnswerBox extends MoAnswerBox {


    public static final String SEARCH_VALUE_CODE = "<span class=\"DFlfde eNFL1\">";
    public static final String SEARCH_UNIT_CODE= "<span class=\"vLqKYe\"";
    public static final String RESULT_VALUE_CODE = "<span class=\"DFlfde SwHCTb\"";
    public static final String RESULT_UNIT_CODE= "<span class=\"MWvIVe\"";
    public static final String DATE_TIME = "<div class=\"hqAUc\">";


    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(SEARCH_VALUE_CODE);
        add(SEARCH_UNIT_CODE);
        add(RESULT_VALUE_CODE);
        add(RESULT_UNIT_CODE);
        add(DATE_TIME);
    }};

    public MoCurrencyConverterAnswerBox(String d) {
        super(d);
    }

    @Override
    public ArrayList<String> getSplitters() {
        return splitters;
    }

    /**
     * takes args and makes the class which implements it
     * displayable for the user
     * for example this can be used in the U.I
     *
     * @param args arguments that are passed to this method via other classes
     */
    @Override
    public <T> void display(T... args) {
        System.out.println(complexNode.getAllValues());
    }
}
