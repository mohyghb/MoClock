package Mo.moclock.MoIO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MoFile {

    private static final String NULL = "null";
    private static final String LENGTH = "length";




    /**
     //     * makes it easier to use MoSavable
     //     * @param sepKey
     //     * @param params
     //     * @return
     //     */
    public static String getData(Object ... params){
        JSONObject jsonObject = new JSONObject();
        for(int i = 0; i < params.length; i++){
            try {
                if(params[i]!=null){
                    jsonObject.put(i+"",params[i].toString());
                }else{
                    jsonObject.put(i+"",NULL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        addLengthToJson(jsonObject, params.length);
        return jsonObject.toString();
    }

    private static void addLengthToJson(JSONObject jsonObject, int length) {
        // saving the length for later on
        try {
            jsonObject.put(LENGTH, length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * returns the data of a savable list as a JSON string
     * @param list
     * @return
     */
    public static String getData(List< ? extends MoSavable> list){
        JSONObject jsonObject = new JSONObject();
        for(int i = 0; i < list.size(); i++){
            try {
                if(list.get(i)!=null){
                    jsonObject.put(i+"",list.get(i).getData());
                }else{
                    jsonObject.put(i+"",NULL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // saving the size for later on
        addLengthToJson(jsonObject, list.size());
        return jsonObject.toString();
    }


    public static String getData(MoSavable ... list){
        return getData(new ArrayList<>(Arrays.asList(list)));
    }


    /**
     * converts data of a set into json object and then into
     * an string
     * @param set
     * @return
     */
    public static String getData(Iterable<?> set){
        JSONObject jsonObject = new JSONObject();
        int i = 0;
        for(Object o: set){
            try {
                if(o!=null){
                    jsonObject.put(i+"",o.toString());
                }else{
                    jsonObject.put(i+"",NULL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }
        addLengthToJson(jsonObject,i);
        return jsonObject.toString();
    }



    /**
     * returns a list of sub-data based on the data
     * that is given to us
     * @param data should be a JSON string
     * @return
     */
    public static String[] loadable(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            // making a list of appropriate length of sub-data
            String[] subs = new String[jsonObject.getInt(LENGTH)];
            for(int i = 0; i < subs.length; i++){
                subs[i] = jsonObject.getString(i+"");
            }
            return subs;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }





}
