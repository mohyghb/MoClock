package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoLogic.MoLogic;

public class MoTranslatorAnswerBox extends MoAnswerBox {


    public static final String SEARCHED_WORD = "<span data-dobid=\"hdw\">";
    public static final String PHONETIC = "<span class=\"XpoqFe\">/<span>";
    public static final String CLASS = "<div class=\"pgRvse vdBwhd\">";
    public static final String DEFINITION = "data-dobid=\"dfn\">";
    public static final String EXAMPLES = "<div class=\"vk_gy\">";
    public static final String SIMILAR_OPPOSITE ="<div class=\"pdpvld\">"+ MoLogic.AND + "<div class=\"hVpeib\">";
    public static final String SIMILAR_OPPOSITE_WORDS = "<div data-mh=\"-1\" role=\"listitem\">";


    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(SEARCHED_WORD);
        add(PHONETIC);
        add(CLASS);
        add(DEFINITION);
        add(EXAMPLES);
        add(SIMILAR_OPPOSITE);
        add(SIMILAR_OPPOSITE_WORDS);
    }};




    public MoTranslatorAnswerBox(String d) {
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
       // complexNode.display(args);
//        System.out.println(complexNode.getValues());
//        System.out.println(complexNode.getValues(0));
//        System.out.println(complexNode.getValues(0,0));
//        System.out.println(complexNode.getValues(0,0,0));
//        System.out.println(complexNode.getValues(0,0,0,0));
    }


}
