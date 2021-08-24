package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;

public class MoTimeAnswerBox extends MoAnswerBox {

    public static final String TIME_CODE = "<div class=\"gsrt vk_bk dDoNo\" aria-level=\"3\" role=\"heading\">";
    public static final String WEEK_DAY_CODE = "<div class=\"vk_gy vk_sh\">";
    public static final String MONTH_YEAR_TIMEZONE_CODE = "<span class=\"KfQeJ\">";
    public static final String PLACE_CODE = "span class=\"vk_gy vk_sh";
    public static final String TIME_ZONE_TITLE = "div class=\"vk_bk dDoNo";
    public static final String TIME_ZONE_DATE = "div class=\"vk_gy vk_sh";
    public static final String TIME_ZONE = "<div>";


    public static final String SEARCHED_TIME = "<div class=\"vk_c vk_gy vk_sh card-section sL6Rbf\">";
    public static final String DAY = "<span class=\"KfQeJ\">";
    public static final String PLACE = "<span class=\"fJY1Ee\">";
    public static final String RESULT_PLACE = "<div class=\"vk_bk dDoNo aCAqKc\">";

    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(TIME_ZONE_TITLE);
        add(TIME_ZONE);
        add(TIME_ZONE_DATE);
        add(TIME_CODE);
        add(WEEK_DAY_CODE);
        add(MONTH_YEAR_TIMEZONE_CODE);
        add(PLACE_CODE);
    }};



    public MoTimeAnswerBox(String d) {
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
