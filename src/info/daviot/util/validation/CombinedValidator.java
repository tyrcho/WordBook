package info.daviot.util.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Combines several validations in a single object.
 * 
 * @author MDA
 * @version NP
 */
public class CombinedValidator implements IValidator
{
   private Collection validators; //Collection of IValidator

   /**
    * Constructs the validator as a combination of several validators.
    *
    * @param validators a collection of IValidator objects
    */
   public CombinedValidator(Collection validators)
   {
      this.validators=validators;
   }
   
   /**
    * Validates the value by running all validations specified by the validators
    * which this object combines.
    *
    * @param value the object to validate
    * @throws SeveralFailedValidationsException when the validation failed 
    */
   public void validate(Object value) throws FailedValidationException
   {
      LinkedList failedValidationExceptions=new LinkedList();
      boolean failedValidation=false;
      Iterator i=validators.iterator(); 
      while (i.hasNext())
      {
         IValidator validator=(IValidator)i.next(); 
         try
         {
            validator.validate(value);
         }
         catch (FailedValidationException e)
         {
            failedValidationExceptions.add(e);
            failedValidation=true;
         }
      }
      if (failedValidation)
      {
         throw new SeveralFailedValidationsException(failedValidationExceptions, value);
      }
   }
}
