package com.moh.moclock.MoGoogleSearch.Google.AnswerBox;


import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.CalculatorAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.CurrencyConverterAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.ParagraphAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.QuickAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.QuickAnswerBox1;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.TimeAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.TrailerAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.TranslatorAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.WeatherAnswerBox;

public class AnswerBoxObject extends AnswerBox {


    private ArrayList<AnswerBox> objects;


    public AnswerBoxObject(int type, String data) {
        super(data);
        this.objects = new ArrayList<>();
        this.objects.add(initObject(type,data));
    }


    private AnswerBox initObject(int type, String data)
    {
        switch (type){
            case AnswerBox.TYPE_CALCULATOR:
                return new CalculatorAnswerBox(data);
            case AnswerBox.TYPE_WEATHER:
                return new WeatherAnswerBox(data);
            case AnswerBox.TYPE_PARAGRAPH:
                return new ParagraphAnswerBox(data);
            case AnswerBox.TYPE_TIME:
                return new TimeAnswerBox(data);
            case AnswerBox.TYPE_QUICK:
                return new QuickAnswerBox(data);
            case AnswerBox.TYPE_QUICK1:
                return new QuickAnswerBox1(data);
            case AnswerBox.TYPE_TRAILER:
                return new TrailerAnswerBox(data);
            case AnswerBox.TYPE_CURRENCY_CONVERTER:
                return new CurrencyConverterAnswerBox(data);
            case AnswerBox.TYPE_TRANSLATOR:
                return new TranslatorAnswerBox(data);
        }
        return null;
    }


    public void getResult() {
        if (this.objects != null) {
            for (AnswerBox answerBox : this.objects) {
                if (answerBox != null) {
                    answerBox.getResult();
                }
            }
        }
    }



}
