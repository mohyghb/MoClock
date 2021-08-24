package com.moh.moclock.MoGoogleSearch.MoEnhancedScraper;

import java.util.ArrayList;
import java.util.List;

public class MoString {

    public static String normalizeString(String text) {
        boolean s = true;

        while(s) {
            if (text.contains("<")) {
                int indexOfOpen = text.indexOf("<");
                int indexOfClose = text.indexOf(">");

                while(indexOfClose < indexOfOpen){
                    text = text.substring(text.indexOf(">") + 1);
                    indexOfClose = text.indexOf(">");
                }
                indexOfOpen = text.indexOf("<");
                text = text.replace(text.substring(indexOfOpen,indexOfClose + 1),"");
                //text = text.substring(text.indexOf(">") + 1);
            } else if (text.contains(">")) {
                text = text.substring(text.indexOf(">") + 1);
            } else{
                s = false;
            }
        }
        return text;
    }


    public static String continueUntil(List<String> until, String text){
        int first = -1;
        for(String un:until){
            int index = text.indexOf(un);
            if(first == -1){
                first = index;
            }else if(index < first && index != -1){
                first = index;
            }
        }
        if (first == -1) {
            return text;
        }
        return text.substring(0,first);
    }


    public static String[] MoSplit(ArrayList<String> splitter, String text) {
        ArrayList<String> splitStrings = new ArrayList<>();


        while (true) {
            int firstIndex = firstIndex(splitter,text);
            if (firstIndex == -1) {
                splitStrings.add(text);
                break;
            }
            int second = text.indexOf(splitter.get(firstIndex));
            if(second < 0){
                break;
            }
            splitStrings.add(text.substring(0,second));
            text = text.substring(second + splitter.get(firstIndex).length());
        }
        String[] strings = new String[splitStrings.size()];
        return splitStrings.toArray(strings);
    }

    public static int firstIndex(ArrayList<String> splitter, String text){
        int index = -1;
        int nearestSplitter = 999999999;

        for (int i = 0;i < splitter.size(); i++ ) {
            int sindex = text.indexOf(splitter.get(i));
            if (sindex < nearestSplitter && sindex != -1) {
                nearestSplitter = sindex;
                index = i;
            }
        }

        return index;
    }




    public static String getUntil(String until, String text){
        return text.substring(0,text.indexOf(until));
    }


    public static String[] getUntil(String until, String[] texts){
        for (int i = 0; i < texts.length; i++) {
            texts[i] = getUntil(until, texts[i]);
        }
        return texts;
    }



}
