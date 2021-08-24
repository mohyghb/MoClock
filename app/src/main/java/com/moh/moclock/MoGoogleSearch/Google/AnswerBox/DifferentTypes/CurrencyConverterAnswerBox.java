package com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes;

import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.AnswerBox;


public class CurrencyConverterAnswerBox extends AnswerBox {


    public static final String SEARCH_VALUE_CODE = "<span class=\"DFlfde\" id=\"knowledge-currency__src-amount\">";
    public static final String SEARCH_UNIT_CODE= "id=\"knowledge-currency__src-currency\">";
    public static final String RESULT_VALUE_CODE = "id=\"knowledge-currency__tgt-amount\">";
    public static final String RESULT_UNIT_CODE= "id=\"knowledge-currency__tgt-currency\">";



    String search_value;
    String search_unit;
    String result_value;
    String result_unit;

    public CurrencyConverterAnswerBox(String data)
    {
        super(data);
        this.initValues();
    }

    private void initValues()
    {
        this.search_unit = extractData(SEARCH_UNIT_CODE);
        this.search_value = extractData(SEARCH_VALUE_CODE);
        this.result_unit = extractData(RESULT_UNIT_CODE);
        this.result_value = extractData(RESULT_VALUE_CODE);
    }

    @Override
    public String toString() {
        return "CurrencyConverterAnswerBox{" + "\n"+
                "search_value='" + search_value + '\''+ "\n" +
                ", search_unit='" + search_unit + '\''+ "\n" +
                ", result_value='" + result_value + '\'' + "\n"+
                ", result_unit='" + result_unit + '\''+ "\n" +
                '}';
    }

    //@Override
    public void getResult() {
        System.out.print(this.toString());
        //return this.search_value +" " + this.search_unit+ " is "+ this.result_value+ " "+this.result_unit;
    }


}
