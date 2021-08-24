package com.moh.moclock.MoClock.MoClockSugestions;

import java.util.Comparator;

public class MoSuggestionComparator implements Comparator<MoPrioritySuggestion> {
    @Override
    public int compare(MoPrioritySuggestion t, MoPrioritySuggestion t1) {
        return Float.compare(t.getPriority(),t1.getPriority());
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }
}
