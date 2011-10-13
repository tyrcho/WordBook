package com.tyrcho.util.validation;

import com.tyrcho.util.misc.OrderedPair;


/**
 * Validates that an ordered pair is correctly ordered.
 * 
 * @author MDA
 * @version NP
 */
public class OrderedPairValidator extends AbstractValidator
{
   private boolean isStrict;

   /**
    * Constructs the OrderedPairValidator
    * with a constant reason used to describe the possible ValidationFailedExceptions.
    *
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    * @param isStrict specifies if the fields are allowed to be equal or empty
    * (true means the fields must be different and both present)
    */
   public OrderedPairValidator(String reason, boolean isStrict)
   {
      super(reason);
      setStrict(isStrict);
   }

   /**
    * Constructs the OrderedPairValidator.
    * A default reason is generated 
    * to describe the ValidationFailedExceptions.
    * By default, the validator is strict.
    */
   public OrderedPairValidator()
   {
      this(null, true);
   }

   /**
    * Validates that the ordered pair is correctly ordered.
    *
    * @param value the {@link be.belgacom.itg.common.util.OrderedPair} to validate
    * @throws FailedValidationException when the validation failed
    * @throws ClassCastException if the value is not an OrderedPair
    */
   public void validate(Object value) throws FailedValidationException
   {
      OrderedPair orderedPair = (OrderedPair) value;      
      if (!orderedPair.isOrdered() 
            || (isStrict() && !orderedPair.isStrictlyOrdered()))
      {
         if (getReason() == null)
         {
            throw new FailedValidationException("Not in correct order", value);
         }
         else
         {
            throw new FailedValidationException(getReason(), value);
         }
      }

   }

   /**
    * true means the fields must be different.
    */
   public boolean isStrict()
   {
      return isStrict;
   }
   
   /**
    * @param isStrict specifies if the fields are allowed to be equal 
    * (true means the fields must be different)
    */
   public void setStrict(boolean isStrict)
   {
      this.isStrict = isStrict;
   }
}