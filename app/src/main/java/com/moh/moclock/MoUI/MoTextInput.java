package com.moh.moclock.MoUI;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import com.moh.moclock.MoColor.MoColor;

public class MoTextInput {

    /**
     * disables getting input from the text view while at
     * the same time preserving it's text color
     * note: setEnabled(false) changes the color of the
     * text to disabled color, while you can do that, it is easier to
     * just remove the ways you can input text in the inputText
     * @param texts
     */
    public static void disable(TextInputEditText[] texts){
        for(TextInputEditText tv: texts){
            tv.setCursorVisible(false);
            tv.setFocusable(false);
        }
    }

    /**
     * opposite of what disable does
     * @param texts
     */
    public static void enable(TextInputEditText[] texts){
        for(TextInputEditText tv: texts){
            tv.setCursorVisible(true);
            tv.setFocusable(true);
            tv.setFocusableInTouchMode(true);
//            String hint = Objects.requireNonNull(tv.getText()).toString();
//            tv.setHint(hint.isEmpty()?"00":hint);
//            tv.setText("");
        }
    }


    /**
     * makes sure that the text view has two digits when focus is lost
     * @return
     */
    public static View.OnFocusChangeListener twoDigitFocusChangeListener(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    // no focus
                    TextInputEditText editText = (TextInputEditText) view;
                    String text = editText.getText().toString();
                    if(text.length() == 1){
                        editText.setText("0"+text);
                    }
                }

            }
        };
    }


    public static void setColorCondition(boolean condition, int[] colorTrue, int[] colorFalse, TextView tv, Context context){
        if(condition){
            tv.setTextColor(MoColor.getColor(colorTrue,context));
        }else{
            tv.setTextColor(MoColor.getColor(colorFalse,context));
        }
    }


}
