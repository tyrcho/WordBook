package com.tyrcho.util.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Applies the same validation to a Collection of objects.
 * 
 * @author MDA
 * @version NP
 */
public class CollectionValidator implements IValidator
{
   private IValidator validator;

   /**
    * Constructs the validator by reusing an existing validator.
    *
    * @param validator an existing validator object
    */
   public CollectionValidator(IValidator validator)
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
   public void validate(Object value) throws FailedValidationException
   {
      LinkedList failedValidationExceptions=new LinkedList();
      boolean failedValidation=false;
      Collection collection=(Collection)value;
      Iterator i=collection.iterator();
      while (i.hasNext())
      {
      	 Object validatedObject=i.next(); 
         try
         {
            validator.validate(validatedObject);
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
