package com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes;


import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.AnswerBox;

public class ParagraphAnswerBox extends AnswerBox {



    public static final String ANSWER_CODE = "<span class=\"e24Kjd\">";


    private String answer;


    public ParagraphAnswerBox(String data)
    {
        super(data);
        this.initValues();
    }


    private void initValues()
    {
        this.answer = super.extractData(ANSWER_CODE);
    }

    // this is a special case since there are </b> <b> in there
    // which could interfere with others
    // therefore we need a function dedicated to itself
    private void extarctParagraph()
    {

    }

    @Override
    public String toString() {
        return "ParagraphAnswerBox{" + "\n" +
                "answer='" + answer + '\''+ "\n" +
                '}';
    }

    //@Override
    public void getResult() {

        System.out.print(this.toString());
        //return this.answer.replaceAll("<b>","").replaceAll("</b>","");
    }
}
