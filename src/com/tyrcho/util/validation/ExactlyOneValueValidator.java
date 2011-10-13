package com.tyrcho.util.validation;

import java.util.Collection;
import java.util.Iterator;



/**
 * Checks that a Collection contains exactly one value
 * not null and with a nonempty String representation.
 * 
 * @author MDA
 * @version NP
 */
public class ExactlyOneValueValidator extends AbstractValidator
{
   /**
    * Constructs the ExactlyOneValueValidator 
    * with a constant reason used to describe the possible ValidationFailedExceptions.
    *
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    */
   public ExactlyOneValueValidator(String reason)
   {
   	  super(reason);
   }
   
   /**
    * Constructs the ExactlyOneValueValidator.
    * A default reason is generated from the tested object
    * to describe the ValidationFailedExceptions.
    */
   public ExactlyOneValueValidator()
   {
   	  this(null);
   }
   
   /**
    * Checks that the Collection contains exactly one value
    * not null and with a nonempty String representation.
    *
    * @param value the Collection to validate
    * @throws ValidationFailedException when the validation failed
    * @throws ClassCastException if value is not a Collection
    */
   public void validate(Object value) throws FailedValidationException
   {
   	   Collection collection=(Collection) value;
   	   Iterator i=collection.iterator();
   	   int definedValuesCount=0;
   	   while (i.hasNext())
   	   {
   	   	  Object element=i.next();
   	   	  if (element!=null && !"".equals(element.toString()))
   	   	  {
   	   	  	 definedValuesCount++;
   	   	  }
   	   }
   	   if (definedValuesCount!=1)
   	   {
   	   	  if (getReason()==null)
   	   	  {
   	   	  	 throw new FailedValidationException("must contain exactly one defined value", value);
   	   	  }
   	   	  else 
   	   	  {
             throw new FailedValidationException(getReason(), value);   	   	  
          }
   	   }
   }      
}

