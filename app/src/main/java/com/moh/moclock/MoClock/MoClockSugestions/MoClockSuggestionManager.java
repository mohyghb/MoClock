package com.moh.moclock.MoClock.MoClockSugestions;


import android.content.Context;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

import com.moh.moclock.MoClock.MoAlarmClock;
import com.moh.moclock.MoDate.MoDate;
import com.moh.moclock.MoReadWrite.MoReadWrite;

// a way to intelligently tell what time
// the user will be setting an alarm for
public class MoClockSuggestionManager {


    private static final String FILE_NAME = "suggestionclock";
    private static final String SEP_KEY = "suggestionclock";
    // we only offer 4 popular suggestion
    private static final int SUGGESTION_SIZE = 4;
    private static final float ACCURACY = 0.1f;

    // key: when they created the alarm
    // value: what time they set the alarm for
    private static ArrayList<MoClockSuggestion> suggestions = new ArrayList<>();


    /**
     * the alarm that is being added
     *
     * @param c
     */
    public static void add(MoAlarmClock c, Context context) {
        load(context);
        MoClockSuggestion suggestion = new MoClockSuggestion(c);
        suggestions.add(suggestion);
        save(context);
    }

    public static void reset(Context context) {
        suggestions.clear();
        save(context);
    }


    public static void save(Context context) {
        MoReadWrite.saveFile(FILE_NAME, MoFile.getData(suggestions), context);
    }

    public static void load(Context context) {
        if (suggestions.isEmpty()) {
            String[] com = MoFile.loadable(MoReadWrite.readFile(FILE_NAME, context));
            if (MoFile.isValidData(com)) {
                String[] suggestionsData = MoFile.loadable(com[0]);
                for (String c : suggestionsData) {
                    if (!c.isEmpty()) {
                        MoClockSuggestion suggestion = new MoClockSuggestion(c, context);
                        suggestions.add(suggestion);
                    }
                }
            }
        }
    }

    public static int size() {
        return suggestions.size();
    }

    /**
     * @return a list of clock suggestions that are created based on the time of request
     */
    public static Iterable<MoPrioritySuggestion> getSuggestions(Context c) {
        load(c);
        PriorityQueue<MoPrioritySuggestion> priorityQueue = new PriorityQueue<>(SUGGESTION_SIZE,
                new MoSuggestionComparator());
        HashSet<String> addedTimes = new HashSet<>();
        MoDate currTime = new MoDate();
        for (MoClockSuggestion s : suggestions) {
            float d = s.isInRange(currTime);
            if (d > ACCURACY) {
                // if it is accurate enough of suggestion
                // then we add it to our priority
                // float rounded = (float)Math.round(d*10)/10;
                MoPrioritySuggestion prioritySuggestion = new MoPrioritySuggestion(s, d);
                if (!addedTimes.contains(prioritySuggestion.getTime())) {
                    // no duplicates
                    priorityQueue.add(prioritySuggestion);
                    addedTimes.add(prioritySuggestion.getTime());
                }
            }
        }
        return priorityQueue;
    }

}
