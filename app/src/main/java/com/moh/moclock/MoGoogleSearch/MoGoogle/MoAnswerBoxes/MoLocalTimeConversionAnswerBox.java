package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoLogic.MoLogic;

public class MoLocalTimeConversionAnswerBox extends MoAnswerBox {



    public static final String SEARCHED_TIME = "<div class=\"vk_c vk_gy vk_sh card-section sL6Rbf\">";
    public static final String DAY = "<span class=\"KfQeJ\">";
    public static final String PLACE = "<span class=\"fJY1Ee\">";
    public static final String RESULT_PLACE = "<div class=\"vk_bk dDoNo aCAqKc\">";

    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(MoLogic.getMoLogicAnds(SEARCHED_TIME,DAY,PLACE,RESULT_PLACE));
    }};

    public MoLocalTimeConversionAnswerBox(String d) {
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
