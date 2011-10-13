package info.daviot.util.ruleparser;

import java.util.*;

/** 
 * Represents a value (constant or variable parameter).
 * 
 * @author MDA 
 */
class Value
{
   private String value;
   private boolean constant;

   /** 
    * Constructs a Value.
    *
    * @param value either the parameter name or the constant value
    * @param constant true if use it as a constant
    */
   public Value(String value, boolean constant)
   {
      this.value= value;
      this.constant= constant;
   }

   /**
    * Returns the real value : either the constant or the paramter value.
    * Null parameters or not found are represented by "null";
    * 
    * @param parameters a Hashtable with the set of parameters
    * @return the real value
    */
   public String getValue(Hashtable parameters)
   {
      if (constant)
      {
         return value;
      }
      else
      {
         return (String) parameters.get(value)+"";
      }
   }

   public String toString()
   {
      if (constant)
         {
         return (value);
      }
      else
         {
         return "$" + value;
      }
   }

   /**
    * Tests if the String reprensentation are equals.
    * Null value are compared to "null" String without case sensitiveness.
    * 
    * @param otherValue the value to compareTo
    * @param a Hashtable with the set of parameters
    */
   public boolean equals(Value otherValue, Hashtable parameters)
   {
      String otherRealValue= otherValue.getValue(parameters);
      String realValue=getValue(parameters);
      return otherRealValue.equals(realValue)
      	|| ("null".equalsIgnoreCase(realValue)
      		&& "null".equalsIgnoreCase(otherRealValue));
   }

   /**
    * Compare the String reprensentation.
    * Null value are compared to "null" String.
    * 
    * @param otherValue the value to compareTo
    * @param a Hashtable with the set of parameters
    */
   public int compareTo(Value otherValue, Hashtable parameters)
   {
      String otherRealValue= otherValue.getValue(parameters);
      String value=getValue(parameters);
      int comparison=value.compareTo(otherRealValue);
      return comparison;
   }
}