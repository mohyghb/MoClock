package com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;

public class MoWeatherAnswerBox extends MoAnswerBox {



    public static final String PLACE_CODE = "<div class=\"vk_gy vk_h\" id=\"wob_loc\">";
    public static final String DATE_TIME_CODE = "<div class=\"vk_gy vk_sh\" id=\"wob_dts\">";
    public static final String WEATHER_STATE_CODE = "<span class=\"vk_gy vk_sh\" id=\"wob_dc\">";

    public static final String TEMPERATURE_CODE = "<span class=\"wob_t\" id=\"wob_tm\" style=\"display:inline\">";
    public static final String TEMPERATURE1_CODE = "<span class=\"wob_t\" id=\"wob_ttm\" style=\"display:none\">";

    public static final String PRECIPITATION_PERCENTAGE_CODE = "<span id=\"wob_pp\">";
    public static final String HUMIDITY_PERCENTAGE_CODE = "<span id=\"wob_hm\">";
    public static final String WIND_SPEED_CODE = "<span class=\"wob_t\" id=\"wob_ws\">";

    public static final String IMAGE_URI_CODE = "<img style=\"float:left;height:64px;width:64px\" alt=\"";
    public static final String SOURCE_CODE = "src=\"";


    public static final String UNIT_TEMPERATURE = "<span aria-label=";




    public static final String WEEK_NAMES = "<div class=\"vk_lgy\" style=\"padding-top";
    public static final String WEEK_NAMES_IMAGES = "<div style=\"display:inline-block\">";
    public static final String WEEK_TEMPERATURE_1 = "<span class=\"wob_t\" style=\"display:inline\">";
    public static final String WEEK_TEMPERATURE_2 = "</span><span class=\"wob_t\" style=\"display:none\">";




    public static final ArrayList<String> splitters = new ArrayList<String>(){{
        add(PLACE_CODE);
        add(DATE_TIME_CODE);
        add(WEATHER_STATE_CODE);
        add(TEMPERATURE_CODE);
        add(TEMPERATURE1_CODE);
        add(UNIT_TEMPERATURE);
        add(PRECIPITATION_PERCENTAGE_CODE);
        add(HUMIDITY_PERCENTAGE_CODE);
        add(WIND_SPEED_CODE);

        add(WEEK_NAMES);
//        add(WEEK_NAMES_IMAGES);
        add(WEEK_TEMPERATURE_1);
        add(WEEK_TEMPERATURE_2);

    }};


    private String place;
    private String dateTime;
    private String state;
    private String temperature1;
    private String temperature2;
    private String precipitationPercentage;
    private String humidityPercentage;
    private String windSpeed;





    public MoWeatherAnswerBox(String d) {
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
