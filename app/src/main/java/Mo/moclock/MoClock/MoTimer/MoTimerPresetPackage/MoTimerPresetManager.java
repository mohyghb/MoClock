package Mo.moclock.MoClock.MoTimer.MoTimerPresetPackage;

import android.content.Context;

import java.util.ArrayList;

import Mo.moclock.MoClock.MoTimer.MoTimer;
import Mo.moclock.MoIO.MoFile;
import Mo.moclock.MoIO.MoLoadable;
import Mo.moclock.MoReadWrite.MoReadWrite;
import Mo.moclock.MoReadWrite.MoSave;
import Mo.moclock.MoRunnable.MoRunnable;

public class MoTimerPresetManager {

    private static final String FILE_NAME = "timer_presets";
    private static final String SEP_KEY = "sj&i&&a6bgiua&";

    private static ArrayList<MoTimerPreset> presets = new ArrayList<>();


    public static void add(MoTimerPreset preset,Context context){
        presets.add(preset);
        save(context);
    }


    public static  void remove(int index,Context context){
        presets.remove(index);
        save(context);
    }

    public static int size(){
        return presets.size();
    }


    public static void save(Context context) {
        MoReadWrite.saveFile(FILE_NAME, MoFile.getData(presets),context);
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    public static void load(String data, Context context) {
        if(presets.isEmpty()){
            String[] parts = MoFile.loadable(MoReadWrite.readFile(FILE_NAME,context));
            for(String s: parts){
                if(!s.isEmpty()){
                    MoTimerPreset preset = new MoTimerPreset();
                    preset.load(s,context);
                    presets.add(preset);
                }

            }
        }
    }


    /**
     * returns the size of selected preset items
     * @return
     */
    public static int getSelectedSize(){
        int i = 0;
        for(MoTimerPreset p:presets){
            if(p.isSelected()){
                i++;
            }
        }
        return i;
    }

    /**
     * sets the selected boolean to b
     * @param b
     */
    public static void selectAllPresets(boolean b){
        for(MoTimerPreset p:presets){
            p.setSelected(b);
        }
    }

    /**
     * deletes all the selected presets
     */
    public static void deleteSelected(Context context){
        for(int i = presets.size()-1; i>=0;i--){
            if(presets.get(i).isSelected()){
                presets.remove(i);
            }
        }
        save(context);
    }

    public static ArrayList<MoTimerPreset> getPresets() {
        return presets;
    }
}
