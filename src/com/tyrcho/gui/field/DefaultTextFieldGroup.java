package com.tyrcho.gui.field;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * Default implementation of an text field group done on a HashMap.
 * Differs from DefaultInputFieldGroup since it returns getText() values
 * instead of getCurrentValue() values.
 *
 * @author MDA
 * @version NP
 */
public class DefaultTextFieldGroup extends DefaultInputFieldGroup
{
   /**
    * Constructs the text field group on an existing map of fields.
    *
    * @param inputFields a map of fields indexed with their names
    */
   public DefaultTextFieldGroup(Map<String, ITextField> inputFields)
   {
   	  super(inputFields);
   }
	
   public DefaultTextFieldGroup()
   {
   	  this(new HashMap());
   }
	
   /**
    * Gets all the text values entered by the user in the GUI.
    * Queries getText() on each ITextField.
    *
    * @return the values in the form of an HashMap, indexed with the names of the fields
    * @throws ClassCastException if a IInputField is not an ITextField
    */
   public Map getCurrentValues()
   {
      HashMap currentValues = new HashMap();
      Iterator i = inputFields.keySet().iterator();
      while (i.hasNext())
      {
         String name = (String) i.next();
         ITextField textField = (ITextField) getInputField(name);
         currentValues.put(name, textField.getText());
      }
      return currentValues;
   }
}