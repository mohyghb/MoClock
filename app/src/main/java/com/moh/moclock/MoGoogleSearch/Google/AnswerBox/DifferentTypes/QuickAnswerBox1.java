package com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes;




import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.AnswerBox;

public class QuickAnswerBox1 extends AnswerBox {

    public static final String TITLE_CODE = "<span class=\"GzssTd\"><span>";
    public static final String ANSWER_CODE = "<div class=\"Z0LcW\">";
    public static final String SUBJECT_CODE = "<span class=\"qLLird\"><span>";


    String title;
    String subject;
    String answer;

    public QuickAnswerBox1(String data)
    {
        super(data);
        this.initValues();
    }

    private void initValues()
    {
        this.title = super.extractData(TITLE_CODE);
        this.answer = super.extractData(ANSWER_CODE);
        this.subject = super.extractData(SUBJECT_CODE);
    }

    @Override
    public String toString() {
        return "QuickAnswerBox1{" + "\n"+
                "title='" + title + '\'' + "\n"+
                ", subject='" + subject + '\'' + "\n"+
                ", answer='" + answer + '\'' + "\n"+
                '}';
    }

    @Override
    public void getResult() {
        System.out.print(this.toString());
        //return this.title+" "+this.subject+":"+"\n"+this.answer.replaceAll("&nbsp;"," ");
    }
}
