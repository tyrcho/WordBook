package com.tyrcho.gui.field;

/**
 * Specifies that an input field can be asked its current value.
 * 
 * @author MDA
 * @version NP
 */
public interface IInputField {
    /**
     * Must be implemented to return the value selected or chosen by the user in the GUI.
     * 
     * @return the current value in the field
     */
    public Object getCurrentValue();

    /**
     * Must be implemented to display a value in the GUI.
     */
    public void setCurrentValue(Object value);

    public void setEditable(boolean editable);

    public void clear();

    public void requestFocus();

}