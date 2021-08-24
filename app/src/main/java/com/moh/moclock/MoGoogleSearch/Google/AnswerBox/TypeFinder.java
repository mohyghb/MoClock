package com.moh.moclock.MoGoogleSearch.Google.AnswerBox;


import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.CalculatorAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.CurrencyConverterAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.ParagraphAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.QuickAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.QuickAnswerBox1;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.TimeAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.TrailerAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.TranslatorAnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes.WeatherAnswerBox;


public class TypeFinder {

    // this class finds the type of an answer box
    // the types are
    // calculator
    // weather
    // time
    // normal
    // error (then we use the links)


    public static final int TYPE_ERROR = -1;


    public static int find(String data)
    {
        if(data.contains(CalculatorAnswerBox.RESULT_CODE)){
            return AnswerBox.TYPE_CALCULATOR;
        }else if(data.contains(WeatherAnswerBox.TEMPERATURE_CODE)){
            return AnswerBox.TYPE_WEATHER;
        }else if(data.contains(TimeAnswerBox.TIME_CODE)){
            return AnswerBox.TYPE_TIME;
        }
        else if(data.contains(QuickAnswerBox.ANSWER_CODE)){
            return AnswerBox.TYPE_QUICK;
        }else if(data.contains(QuickAnswerBox1.ANSWER_CODE)){
            return AnswerBox.TYPE_QUICK1;
        }
        else if(data.contains(ParagraphAnswerBox.ANSWER_CODE)){
            return AnswerBox.TYPE_PARAGRAPH;
        }else if(data.contains(TrailerAnswerBox.LINK_CODE)){
            return AnswerBox.TYPE_TRAILER;
        }else if(data.contains(CurrencyConverterAnswerBox.RESULT_VALUE_CODE)){
            return AnswerBox.TYPE_CURRENCY_CONVERTER;
        }
        else if(data.contains(TranslatorAnswerBox.DEFINITIONS_CODE)){
            return AnswerBox.TYPE_TRANSLATOR;
        }

        return TYPE_ERROR;
    }

}
