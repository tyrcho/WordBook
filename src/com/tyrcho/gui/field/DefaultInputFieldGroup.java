package com.tyrcho.gui.field;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * Default implementation of an input field group done on a HashMap.
 * 
 * @author MDA
 * @version NP
 */
public class DefaultInputFieldGroup implements IInputFieldGroup {
    protected Map<String, IInputField> inputFields; // HashMap of IInputField

    /**
     * Constructs the input field group on an existing map of fields.
     * 
     * @param inputFields
     *            a map of fields indexed with their names
     */
    public DefaultInputFieldGroup(Map<String, ? extends IInputField> inputFields) {
        setInputFields(inputFields);
    }

    /**
     * Constructs the input field group on an empty map of fields.
     */
    public DefaultInputFieldGroup() {
        this.inputFields = new HashMap<String, IInputField>();
    }

    /**
     * Associates in the group a field with a name.
     * 
     * @param name
     *            the key to use in the map
     * @param inputField
     *            the field which will be mapped to this name
     */
    public void setInputField(String name, IInputField inputField) {
        inputFields.put(name, inputField);
    }

    /**
     * Gets the input field associated with a name.
     * 
     * @param name
     *            the key used in the map
     * @return the input field associated with this name in the map, or null if none is mapped with this name
     */
    public IInputField getInputField(String name) {
        return inputFields.get(name);
    }

    /**
     * Gets all the values selected or chosen by the user in the GUI.
     * 
     * @return the values in the form of an HashMap, indexed with the names of the fields
     */
    public Map<String, Object> getCurrentValues() {
        Map<String, Object> currentValues = new HashMap<String, Object>();
        Iterator<String> i = inputFields.keySet().iterator();
        while (i.hasNext()) {
            String name = i.next();
            IInputField inputField = getInputField(name);
            currentValues.put(name, inputField.getCurrentValue());
        }
        return currentValues;
    }

    public Map<String, IInputField> getInputFields() {
        return inputFields;
    }

    public void setInputFields(Map<String, ? extends IInputField> inputFields) {
        this.inputFields = new HashMap<String, IInputField>(inputFields);
    }

    public void setEditable(boolean editable) {
        for (IInputField field : inputFields.values()) {
            field.setEditable(editable);
        }
    }

    public void clear() {
        for (IInputField field : inputFields.values()) {
            field.clear();
        }
    }

}