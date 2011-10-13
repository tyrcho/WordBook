package com.tyrcho.util.validation;

import java.util.Collection;

/**
 * Checks that the collection contains at least one element.
 * @author MDA
 */
public class NotEmptyCollectionValidator extends AbstractValidator
{
	public static final String defaultReason="empty_collection";
	
   /**
    * Constructs the NotEmptyCollectionValidator 
    * with a constant reason used to describe the possible ValidationFailedExceptions.
    *
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    */
   public NotEmptyCollectionValidator(String reason)
   {
   	  super(reason);
   }
   
   /**
    * Constructs the NotEmptyCollectionValidator.
    * A default reason is generated from the tested object
    * to describe the ValidationFailedExceptions.
    */
   public NotEmptyCollectionValidator()
   {
   	  this(defaultReason);
   }

   /**
    * Validates a collection of objects which is expected to be not empty.
    *
    * @param value the object to validate
    * @throws ClassCastException if value cannot be assigned to a Collection
    */
   public void validate(Object value) throws FailedValidationException
   {
   	  Collection collection=(Collection) value;
   	  if (collection.isEmpty())
   	  {
   	  	  throw new FailedValidationException(getReason(), value);
   	  }
   }

}

