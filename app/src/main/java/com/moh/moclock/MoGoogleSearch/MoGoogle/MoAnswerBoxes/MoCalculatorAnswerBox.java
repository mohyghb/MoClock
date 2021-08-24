package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;

public class MoCalculatorAnswerBox extends MoAnswerBox {


    public static final String EQUATION_CODE = "<span jsname=\"ubtiRe\" class=\"vUGUtc\">";
    public static final String RESULT_CODE = "<span jsname=\"VssY5c\" class=\"qv3Wpe\" id=\"cwos\">";



    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(EQUATION_CODE);
        add(RESULT_CODE);
    }};



    public MoCalculatorAnswerBox(String d) {
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
        //complexNode.display(args);
    }


}
