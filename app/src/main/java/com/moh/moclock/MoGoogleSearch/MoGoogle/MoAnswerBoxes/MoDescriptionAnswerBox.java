package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;

public class MoDescriptionAnswerBox extends MoAnswerBox {

    public static final String DESCRIPTION = "</h2><span>";
    public static final String DETAILS_TITLES = "<span class=\"w8qArf\">";
    public static final String DETAILS_ANSWERS = "</span><span class=\"LrzXr kno-fv\">";

    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(DESCRIPTION);
        add(DETAILS_TITLES);
        add(DETAILS_ANSWERS);
    }};


    public MoDescriptionAnswerBox(String d) {
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
        complexNode.display(2);
    }
}
