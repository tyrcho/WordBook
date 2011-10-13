package com.tyrcho.gui.field;

import java.util.Map;

/**
 * Specifies that an input field group can be asked the current values of its fields at once.
 * The input fields are mapped by their names.
 *
 * @author MDA
 * @version NP
 */
public interface IInputFieldGroup
{
   /**
    * Associates in the group a field with a name.
    *
    * @param name the key to use in the map
    * @param inputField the field which will be mapped to this name
    */
   //public void setInputField(String name, IInputField inputField);

   /**
    * Gets the input field associated with a name.
    *
    * @param name the key used in the map
    * @return the input field associated with this name in the map, or null if none is mapped with this name
    */
   public IInputField getInputField(String name);

   /**
    * Must be implemented to return all the values selected or chosen by the user in the GUI.
    *
    * @return the values in the form of an Map, indexed with the names of the fields
    */
   public Map<String, Object> getCurrentValues();
   
   /**
    * Must be implemented to return all the fields.
    *
    * @return the fields in the form of an Map, indexed with the names of the fields
    */
   public Map<String, IInputField> getInputFields();
   

   public void setEditable(boolean editable);

   public void clear();
}