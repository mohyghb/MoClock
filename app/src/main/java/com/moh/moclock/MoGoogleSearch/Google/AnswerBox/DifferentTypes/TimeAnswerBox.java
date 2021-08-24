package com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes;


import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.AnswerBox;

public class TimeAnswerBox  extends AnswerBox {




    public static final String TIME_CODE = "<div class=\"gsrt vk_bk dDoNo\" aria-level=\"3\" role=\"heading\">";
    public static final String WEEK_DAY_CODE = "<div class=\"vk_gy vk_sh\">";
    public static final String MONTH_YEAR_TIMEZONE_CODE = "<span class=\"KfQeJ\">";
    public static final String PLACE_CODE = "</div> \n" + "                 <span>";


    private String time;
    private String weekDay;
    private String monthYearTimeZone;
    private String place;



    public TimeAnswerBox(String data)
    {
        super(data);
        this.initValues();
    }

    private void initValues()
    {
        this.time = super.extractData(TIME_CODE);
        this.weekDay = super.extractData(WEEK_DAY_CODE);
        this.monthYearTimeZone = super.extractData(MONTH_YEAR_TIMEZONE_CODE);
        this.place = super.extractData(PLACE_CODE);
    }

    @Override
    public String toString() {
        return "TimeAnswerBox{" + "\n"+
                "time='" + time + '\'' +  "\n"+
                ", weekDay='" + weekDay + '\'' + "\n" +
                ", monthYearTimeZone='" + monthYearTimeZone + '\''+ "\n" +
                ", place='" + place + '\'' + "\n"+
                '}';
    }

    @Override
    public void getResult() {
        System.out.print(this.toString());
       // return String.format("Time: %s\nweekDay:%s\ndetails:%s\nplace:%s",this.time,this.weekDay,this.monthYearTimeZone,this.place);
    }
}
