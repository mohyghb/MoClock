package com.moh.moclock.MoGoogleSearch.Google.AnswerBox;


import java.util.ArrayList;
import java.util.List;

public class AnswerBox {




    public static final String DIVISION_D = "<d";
    public static final String DIVISION_DL = "</d";
    public static final String DIVISION_S = "<s";
    public static final String DIVISION_SL = "</s";
    private static final String[] DIVISIONS = new String[]{DIVISION_D,DIVISION_S,DIVISION_DL,DIVISION_SL};
    private static final int MAX_INT = 999999999;




    public static final int TYPE_CALCULATOR = 0;
    public static final int TYPE_WEATHER = 1;
    public static final int TYPE_TIME = 2;
    public static final int TYPE_PARAGRAPH = 3;
    public static final int TYPE_QUICK = 4;
    public static final int TYPE_QUICK1 = 5;
    public static final int TYPE_TRAILER = 6;
    public static final int TYPE_CURRENCY_CONVERTER = 7;
    public static final int TYPE_TRANSLATOR = 8;


    // every answer box have some kind of similarities
    // this is a super class for that

    private int type;
    private String data;
    //private Context context;


    private String[] divisions;
    private String appendEach;

    public AnswerBox(String data) {
        this.data = data;
        this.divisions = DIVISIONS;
        this.appendEach = "";
    }






    public String extractData(String sepKey) {
        StringBuilder extractedData = new StringBuilder();
        String[] splittedData = this.data.split(sepKey);

        // we do not care about the first splittedData that is why
        // i = 1;
        for(int i = 1;i<splittedData.length;i++){
            extractedData.append(this.continueUntil(splittedData[i], this.divisions))
                    .append(this.appendEach);
        }

        return extractedData.toString().trim();
    }


    public ArrayList<String> extractDataList(String sepKey)
    {
        ArrayList<String> extractedData = new ArrayList<>();
        String[] splittedData = this.data.split(sepKey);

        // we do not care about the first splittedData that is why
        // i = 1;
        for(int i = 1;i<splittedData.length;i++){
            extractedData.add(this.continueUntil(splittedData[i], this.divisions));
        }
        return extractedData;
    }






    // takes splited data, and a stopping string,
    // returns up to where the first sstr is seen
    public static String continueUntil(String sData, String[] sstr)
    {
        int index = foi(sData,sstr);
        if(index != MAX_INT){
            return sData.substring(0,index);
        }else{
            return "";
        }

    }


    public static String continueUntil(List<String> until, String text){
        int first = -1;
        for(String un:until){
            int index = text.indexOf(un);
            if(first == -1){
                first = index;
            }else if(index < first){
                first = index;
            }
        }

        return text.substring(0,first);
    }


    // first occurrence index
    public static int foi (String data, String[] strs)
    {

        int first = MAX_INT;
        for(int i = 0;i<strs.length;i++){
            int index = data.indexOf(strs[i]);
            if(index> 0 && index<first){
                first = index;
            }
        }
        return first;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

   // public Context getContext()
   // {
    //    return this.context;
  //  }

    public String[] getDivisions() {
        return divisions;
    }

    public void setDivisions(String[] divisions) {
        this.divisions = divisions;
    }















    public void getResult() {

    }


}
