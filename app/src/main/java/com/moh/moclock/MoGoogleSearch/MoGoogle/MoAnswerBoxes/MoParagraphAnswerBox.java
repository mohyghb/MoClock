package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;

public class MoParagraphAnswerBox extends MoAnswerBox {


    public static final String PARAGRAPH_ANSWER = "<span class=\"e24Kjd\">";


    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(PARAGRAPH_ANSWER);
    }};


    public MoParagraphAnswerBox(String d) {
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
        this.complexNode.display(args);
    }
}
