package com.moh.moclock.MoSection;

import android.content.Context;
import android.webkit.ValueCallback;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;

import java.util.ArrayList;
import java.util.List;

import com.moh.moclock.MoReadWrite.MoReadWrite;
import com.moh.moclock.MoReadWrite.MoSave;

public class MoSectionManager implements MoSavable, MoSave, MoLoadable {

    private final String FILE_NAME = "sectionmanager";

    public static final int ALARM_SECTION = 0 ;
    public static final int STOP_WATCH_SECTION = 1;
    public static final int WORLD_CLOCK_SECTION = 2;
    public static final int TIMER_SECTION = 3;


    private int section;
    private List<ValueCallback<Integer>> onSectionChangedListener = new ArrayList<>();


    private static MoSectionManager ourInstance = new MoSectionManager();

    public static MoSectionManager getInstance() {
        return ourInstance;
    }

    private MoSectionManager() {
        this.section = ALARM_SECTION;
    }


    public void subscribe(ValueCallback<Integer> listener) {
        if (onSectionChangedListener.contains(listener))
            return;
        onSectionChangedListener.add(listener);
    }

    public void unsubscribe(ValueCallback<Integer> listener) {
        onSectionChangedListener.remove(listener);
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        String value = MoReadWrite.readFile(FILE_NAME,context);
        this.section = Integer.parseInt(value);
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return this.section+"";
    }

    @Override
    public void save(Context context) {
        MoReadWrite.saveFile(FILE_NAME,this.getData(),context);
    }


    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
        onSectionChangedListener.forEach(integerValueCallback -> integerValueCallback.onReceiveValue(section));
    }

    public void onDestroy() {
        ourInstance.onSectionChangedListener.clear();
        ourInstance = null;
        ourInstance = new MoSectionManager();
    }
}
