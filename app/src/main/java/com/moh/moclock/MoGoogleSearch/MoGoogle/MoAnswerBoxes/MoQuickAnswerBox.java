package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;

public class MoQuickAnswerBox extends MoAnswerBox {

    public static final String TITLE_CODE = "<span class=\"GzssTd\"><span>";
    public static final String ANSWER_CODE = "<div class=\"Z0LcW\">";
    public static final String SUBJECT_CODE = "<span class=\"qLLird\"><span>";


    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(TITLE_CODE);
        add(SUBJECT_CODE);
        add(ANSWER_CODE);

    }};

    public MoQuickAnswerBox(String d) {
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
        complexNode.display(args);
    }
}
