package com.tyrcho.gui.component.console;

import java.util.EventObject;


/**
 * An event thrown by a console when the user inputs something.
 * 
 * @author tyrcho
 */
public class CommandEvent extends EventObject {
    private static final long serialVersionUID = -7260718406791269256L;
    private String text;
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CommandEvent(Object source, String text) {
        super(source);
        setText(text);
    }
}
