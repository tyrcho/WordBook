package com.tyrcho.validation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Applies the same validation to a Collection of objects.
 * 
 * @author MDA
 * @version NP
 */
public class CollectionValidator<T> implements I_Validator<Collection<T>>
{
   private I_Validator<T> validator;

   /**
    * Constructs the validator by reusing an existing validator.
    *
    * @param validator an existing validator object
    */
   public CollectionValidator(I_Validator<T> validator)
   {
      this.validator=validator;
   }
   
   /**
    * Validates a collection of objects by using the original
    * validation to all objects.
    *
    * @param value the object to validate
    * @throws SeveralFailedValidationsException when the validation failed 
    * @throws ClassCastException if value cannot be assigned to a Collection
    */
   public void validate(Collection<T> value) throws FailedValidationException
   {
      List<FailedValidationException> failedValidationExceptions=new LinkedList<FailedValidationException>();
      boolean failedValidation=false;
      for (T object : value)
      {
         try
         {
            validator.validate(object);
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
