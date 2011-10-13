package com.tyrcho.validation;

import java.io.Serializable;

/**
 * This exception is thrown when a validation fails.
 * 
 * @author MDA
 * @version NP
 */
public class FailedValidationException
   extends Exception
   implements Serializable
{
   private String valueRepresentation;
   private String reason;

   /**
    * Constructs a new FailedValidationException.
    *
    * @param reason the detailed cause of the failure in the validation
    * @param valueTested the object which was validated
    */
   public FailedValidationException(String reason, Object valueTested)
   {
      super("Validation failed on " + valueTested + " : " + reason);
      setReason(reason);
      setValueRepresentation("["+(valueTested==null?"null":valueTested.toString())+"]");
   }

   /**
    * Gets the detailed cause of the failure in the validation.
    *
    * @return the reason for the failure
    */
   public String getReason()
   {
      return reason;
   }

   private void setReason(String reason)
   {
      this.reason = reason;
   }

   /**
    * Gets the String representation of the value which caused the exception to fail.
    * 
    * @return the value.toString()
    */
   public String getValueRepresentation()
   {
      return valueRepresentation;
   }

   private void setValueRepresentation(String valueRepresentation)
   {
      this.valueRepresentation = valueRepresentation;
   }
}
