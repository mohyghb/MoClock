package com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes;



import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.AnswerBox;

public class CalculatorAnswerBox extends AnswerBox {



    public static final String EQUATION_CODE = "<span jsname=\"ubtiRe\" class=\"vUGUtc\">";
    public static final String RESULT_CODE = "<span jsname=\"VssY5c\" class=\"qv3Wpe\" id=\"cwos\">";


    private String equation;
    private String result;

    public CalculatorAnswerBox(String data)
    {
        super(data);
        this.initValues();
    }

    private void initValues()
    {
        this.equation = super.extractData(CalculatorAnswerBox.EQUATION_CODE);
        this.result = super.extractData(CalculatorAnswerBox.RESULT_CODE);
    }

    @Override
    public String toString() {
        return "CalculatorAnswerBox{" + "\n"+
                "equation='" + equation + '\'' + "\n"+
                ", result='" + result + '\'' + "\n"+
                '}';
    }

    @Override
    public void getResult()
    {
        System.out.print(this.toString());
        //return this.equation+" "+ this.result;
    }

}
