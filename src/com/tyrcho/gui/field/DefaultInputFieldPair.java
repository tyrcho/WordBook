package com.tyrcho.gui.field;

import com.tyrcho.util.misc.OrderedPair;


/**
 * Default implementation of a group of two input fields in an ordered pair.
 *
 * @author MDA
 * @version NP
 */
public class DefaultInputFieldPair implements IInputFieldPair
{
   private IInputField firstField;
   private IInputField secondField;
   
   public DefaultInputFieldPair(IInputField firstField, IInputField secondField)
   {
   	  setFirstField(firstField);
   	  setSecondField(secondField);
   }

   /**
    * Defines the first input field in the pair.
    *
    * @param inputField the first field
    */
   public void setFirstField(IInputField inputField)
   {
      firstField=inputField;
   }

   /**
    * Gets the first input field.
    *
    * @return the first input field 
    */
   public IInputField getFirstField()
   {
      return firstField;
   }

   /**
    * Defines the second input field in the pair.
    *
    * @param inputField the second field
    */
   public void setSecondField(IInputField inputField)
   {
      secondField=inputField;
   }

   /**
    * Gets the second input field.
    *
    * @return the second input field 
    */
   public IInputField getSecondField()
   {
      return secondField;
   }
   
   /**
    * Gets the values of this pair, in the same order as they were input.
    * 
    * @return the two values
    */
   public OrderedPair getCurrentValues()
   {
   	  Comparable first=(Comparable)firstField.getCurrentValue();
   	  Comparable second=(Comparable)secondField.getCurrentValue();
   	  return new OrderedPair(first, second);
   }
}

