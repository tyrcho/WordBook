package com.tyrcho.gui.component.listeditor;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultFilter<T> implements Filter<T> {
    private List<FilterListener> listeners=new ArrayList<FilterListener>();
    
    public void addFilterListener(FilterListener f) {
        listeners.add(f);
    }

    public void removeFilterListener(FilterListener f) {
        listeners.remove(f);
    }

    protected void fireFilterEvent() {
        for (int i=listeners.size()-1; i>=0; i--) {
            listeners.get(i).filterModified();
        }
    }
}
