package com.moh.moclock.MoList;

import java.util.Arrays;
import java.util.List;

public class MoList {


    /**
     *
     * @param list
     * is an string in a format of when you call
     * List.toString e.g [2,23,4]
     * @param data
     * @return
     */
    public static List<String> get(List<String> list,String data){
        String nb = data.substring(1,data.length()-1);
        String[] objects = nb.split(",");
        list.addAll(Arrays.asList(objects));
        return list;
    }



    public static List<Integer> get(List<Integer> integers,List<String> strings){
        for(String s: strings){
            if(!s.isEmpty()){
                integers.add(Integer.parseInt(s.trim()));
            }
        }
        return integers;
    }






}
