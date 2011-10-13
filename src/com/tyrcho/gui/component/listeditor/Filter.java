package com.tyrcho.gui.component.listeditor;

/**
 * Filters elements based on a search string. 
 * @author tyrcho
 */
public interface Filter<T> {
    /**
     * Checks if the filter authorizes this object based on this search string.
     */
    public boolean accepts(T object);
    
    public void addFilterListener(FilterListener f);
    public void removeFilterListener(FilterListener f);
}
