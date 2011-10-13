package com.tyrcho.gui.component.listeditor;

public abstract class RegexpFilter<T> extends DefaultFilter<T> {

    private String regexp;

    public RegexpFilter(String regexp) {
        this.regexp = regexp;
    }
    
    public RegexpFilter() {
        this(".*");
    }

    public String getRegexp() {
        return ".*"+regexp+".*";
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
        fireFilterEvent();
    }

}
