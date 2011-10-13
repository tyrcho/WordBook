package info.daviot.gui.field;

import info.daviot.util.misc.OrderedPair;



/**
 * Groups two input fields in an ordered pair.
 * Useful to check that the values are in the correct order.
 * Note : the input fields must return {@link java.lang.Comparable} values.
 *
 * @author MDA
 * @version NP
 */
public interface IInputFieldPair
{
   /**
    * Defines the first input field in the pair.
    *
    * @param inputField the first field
    */
   public void setFirstField(IInputField inputField);

   /**
    * Gets the first input field.
    *
    * @return the first input field 
    */
   public IInputField getFirstField();

   /**
    * Defines the second input field in the pair.
    *
    * @param inputField the second field
    */
   public void setSecondField(IInputField inputField);

   /**
    * Gets the second input field.
    *
    * @return the second input field 
    */
   public IInputField getSecondField();
   
   /**
    * Gets the values of this pair, in the same order as they were input.
    * 
    * @return the two values
    */
   public OrderedPair getCurrentValues();
}

