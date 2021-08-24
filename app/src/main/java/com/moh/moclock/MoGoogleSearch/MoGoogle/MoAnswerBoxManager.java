package com.moh.moclock.MoGoogleSearch.MoGoogle;


import java.util.ArrayList;
import java.util.List;

import com.moh.moclock.MoGoogleSearch.MoDisplay.MoDisplayable;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoCalculatorAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoCurrencyConverterAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoDescriptionAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoLocalTimeConversionAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoParagraphAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoQuickAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoTimeAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoTranslatorAnswerBox;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxes.MoWeatherAnswerBox;

public class MoAnswerBoxManager implements MoDisplayable {





    List<MoAnswerBox> answerBoxes;

    public MoAnswerBoxManager() {
        this.answerBoxes = new ArrayList<>();
    }

    public void process(String webResults) {
        //String[] types = MoString.getUntil(MoHtmlHelper.TYPE_UNTIL,MoHtmlHelper.splitTypes(webResults));
        String[] types = MoHtmlHelper.splitTypes(webResults);
        for (int i = 0; i < types.length; i++) {
            decide(types[i]);
        }
    }

    public List<MoAnswerBox> getAnswerBoxes() {
        if(answerBoxes == null)
            return new ArrayList<>();
        return answerBoxes;
    }


    private void decide (String type) {
        if (type.startsWith(MoAnswerBox.WEATHER)) {
            // this is for weather
            answerBoxes.add(new MoWeatherAnswerBox(type));
        } else if (type.contains(MoAnswerBox.DICTIONARY)) {
            // this is for translator
            answerBoxes.add(new MoTranslatorAnswerBox(type));
        } else if (type.startsWith(MoAnswerBox.CALCULATOR)) {
            // calculator answer box
            answerBoxes.add(new MoCalculatorAnswerBox(type));
        } else if (type.startsWith(MoAnswerBox.DESCRIPTION)) {
            // description answer box
            answerBoxes.add(new MoDescriptionAnswerBox(type));
        } else if (type.startsWith(MoAnswerBox.LOCAL_TIME_CONVERSION)) {
            answerBoxes.add(new MoLocalTimeConversionAnswerBox(type));
        } else if (type.startsWith(MoAnswerBox.TIME)) {
            answerBoxes.add(new MoTimeAnswerBox(type));
        } else if (type.startsWith(MoAnswerBox.SNIPPET)) {
            answerBoxes.add(new MoParagraphAnswerBox(type));
        } else if (type.contains(MoAnswerBox.QUICK_ANSWER)) {
            answerBoxes.add(new MoQuickAnswerBox(type));
        } else if (type.startsWith(MoAnswerBox.CURRENCY)) {
            answerBoxes.add(new MoCurrencyConverterAnswerBox(type));
        }
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
        for (MoAnswerBox answerBox: this.answerBoxes) {
            answerBox.display(args);
        }
    }
}
