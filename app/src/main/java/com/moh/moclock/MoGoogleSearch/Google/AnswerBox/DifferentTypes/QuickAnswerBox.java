package com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes;


import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.AnswerBox;

public class QuickAnswerBox extends AnswerBox {

    public static final String QUESTION_CODE = "<div class=\"vk_gy vk_sh\">";
    public static final String ANSWER_CODE = "<div class=\"dDoNo vk_bk\">";


    String question;
    String answer;

    public QuickAnswerBox(String data)
    {
        super(data);
        this.initValues();
    }

    private void initValues()
    {
        this.question = super.extractData(QUESTION_CODE);
        this.answer = super.extractData(ANSWER_CODE);
    }

    @Override
    public String toString() {
        return "QuickAnswerBox{" + "\n"+
                "question='" + question + '\'' + "\n"+
                ", answer='" + answer + '\'' + "\n"+
                '}';
    }

    //@Override
    public void getResult() {
        System.out.print(this.toString());
        //return this.question+"\n"+this.answer;
    }
}
