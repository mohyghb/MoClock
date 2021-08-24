package com.moh.moclock.MoClock.MoTimer.MoTimerPresetPackage;

import android.content.Context;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;

import java.util.ArrayList;

import com.moh.moclock.MoReadWrite.MoReadWrite;

public class MoTimerPresetManager {

    private static final String FILE_NAME = "timer_presets";

    private static ArrayList<MoTimerPreset> presets = new ArrayList<>();


    public static void add(MoTimerPreset preset, Context context) {
        presets.add(preset);
        save(context);
    }


    public static void remove(int index, Context context) {
        presets.remove(index);
        save(context);
    }

    public static int size() {
        return presets.size();
    }


    public static void save(Context context) {
        MoReadWrite.saveFile(FILE_NAME, MoFile.getData(presets), context);
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    public static void load(String data, Context context) {
        if (presets.isEmpty()) {
            String[] parts = MoFile.loadable(MoReadWrite.readFile(FILE_NAME, context));
            if (MoFile.isValidData(parts)) {
                String[] presetsData = MoFile.loadable(parts[0]);
                for (String s : presetsData) {
                    if (!s.isEmpty()) {
                        MoTimerPreset preset = new MoTimerPreset();
                        preset.load(s, context);
                        presets.add(preset);
                    }
                }
            }
        }
    }


    /**
     * returns the size of selected preset items
     *
     * @return
     */
    public static int getSelectedSize() {
        int i = 0;
        for (MoTimerPreset p : presets) {
            if (p.isSelected()) {
                i++;
            }
        }
        return i;
    }

    /**
     * sets the selected boolean to b
     *
     * @param b
     */
    public static void selectAllPresets(boolean b) {
        for (MoTimerPreset p : presets) {
            p.setSelected(b);
        }
    }

    /**
     * deletes all the selected presets
     */
    public static void deleteSelected(Context context) {
        for (int i = presets.size() - 1; i >= 0; i--) {
            if (presets.get(i).isSelected()) {
                presets.remove(i);
            }
        }
        save(context);
    }

    public static ArrayList<MoTimerPreset> getPresets() {
        return presets;
    }
}
