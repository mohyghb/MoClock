package Mo.moclock.MoIO;

import java.util.List;

import Mo.moclock.MoClock.MoAlarmClock;

public class MoFile {


    /**
     * makes it easier to use MoSavable
     * @param sepKey
     * @param params
     * @return
     */
    public static String getData(String sepKey, Object ... params){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < params.length; i++){
            if(params[i]!=null){
                stringBuilder.append(params[i].toString());
            } else {
                stringBuilder.append("null");
            }
            if(i < params.length - 1){
                stringBuilder.append(sepKey);
            }
        }
        return stringBuilder.toString();
    }


    public static String getData(String sepKey, List<? extends MoSavable> list){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < list.size();i++){
            stringBuilder.append(list.get(i).getData());
            if(i < list.size() - 1){
                stringBuilder.append(sepKey);
            }
        }
        return stringBuilder.toString();
    }



    public static String getDataSavable(String sepKey, List<MoAlarmClock> params){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < params.size(); i++){
            stringBuilder.append(params.get(i).getData());
            if(i < params.size() - 1){
                stringBuilder.append(sepKey);
            }
        }
        return stringBuilder.toString();
    }


    public static String[] loadable(String sepKey,String data){
        return data.split(sepKey);
    }






}
