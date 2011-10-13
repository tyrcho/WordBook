package com.tyrcho.util.validation;

import java.util.Date;


/**
 * Validates a Date by comparing it with a constant Date.
 * 
 * @author MDA
 * @version NP
 */
public class DateValidator extends AbstractValidator
{
	public static final int COMPARISON_GREATER=1;
	public static final int COMPARISON_GREATER_EQUAL=2;
	public static final int COMPARISON_EQUAL=0;
	public static final int COMPARISON_LOWER=3;
	public static final int COMPARISON_LOWER_EQUAL=4;
	private Date referenceDate;
	private int comparisonMode;
	
   /**
    * Constructs the DateValidator
    * with a constant reason used to describe the possible ValidationFailedExceptions.
    *
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    * @param referenceDate the date to compare to
    * @param comparisonMode to define if the validated date must be 
    * greater, lower, equal, greater or equal or lower or equal than referenceDate
    * @throws IllegalArgumentException if the comparisonMode is not recognized
    */
   public DateValidator(String reason, Date referenceDate, int comparisonMode)
   {
      super(reason);
      setComparisonMode(comparisonMode);
      setReferenceDate(referenceDate);
   }

   /**
    * Constructs the DateValidator.
    * A default reason is generated 
    * to describe the ValidationFailedExceptions.
    *
    * @param referenceDate the date to compare to
    * @param comparisonMode to define if the validated date must be 
    * greater, lower, equal, greater or equal or lower or equal than referenceDate
    * @throws IllegalArgumentException if the comparisonMode is not recognized
    */
   public DateValidator(Date referenceDate, int comparisonMode)
   {
      this(null, referenceDate, comparisonMode);
   }


   /**
    * Validates that the date is correctly positioned relatively to referenceDate.
    *
    * @param value the {@link java.util.Date} to validate
    * @throws FailedValidationException when the validation failed
    * @throws ClassCastException if the value is not a Date
    */
   public void validate(Object value) throws FailedValidationException
   {
      Date date = (Date) value; 
      switch (comparisonMode)
      {
      	  case COMPARISON_EQUAL:
      	  	 if (date.compareTo(referenceDate)!=0)
      	  	 {
		         if (getReason() == null)
		         {
		            throw new FailedValidationException("Not equals "+referenceDate, value);
		         }
		         else
		         {
		            throw new FailedValidationException(getReason(), value);
		         }
      	  	 }
      	  	 break;
      	 
      	  case COMPARISON_GREATER:
      	  	 if (date.compareTo(referenceDate)<=0)
      	  	 {
		         if (getReason() == null)
		         {
		            throw new FailedValidationException("Not greater than "+referenceDate, value);
		         }
		         else
		         {
		            throw new FailedValidationException(getReason(), value);
		         }
      	  	 }
      	  	 break;
      	 
      	  case COMPARISON_GREATER_EQUAL:
      	  	 if (date.compareTo(referenceDate)<0)
      	  	 {
		         if (getReason() == null)
		         {
		            throw new FailedValidationException("Not greater or equal than "+referenceDate, value);
		         }
		         else
		         {
		            throw new FailedValidationException(getReason(), value);
		         }
      	  	 }
      	  	 break;
      	 
      	  case COMPARISON_LOWER:
      	  	 if (date.compareTo(referenceDate)>=0)
      	  	 {
		         if (getReason() == null)
		         {
		            throw new FailedValidationException("Not lower than "+referenceDate, value);
		         }
		         else
		         {
		            throw new FailedValidationException(getReason(), value);
		         }
      	  	 }
      	  	 break;
      	 
      	  case COMPARISON_LOWER_EQUAL:
      	  	 if (date.compareTo(referenceDate)>0)
      	  	 {
		         if (getReason() == null)
		         {
		            throw new FailedValidationException("Not lower or equal than "+referenceDate, value);
		         }
		         else
		         {
		            throw new FailedValidationException(getReason(), value);
		         }
      	  	 }
      	  	 break;
      	  	 
      	  default:
      	  	  throw new IllegalStateException("Unexpected comparison mode "+comparisonMode);
      }     
   }

	/**
	 * Gets the comparisonMode
	 * @return Returns a int
	 */
	public int getComparisonMode() {
		return comparisonMode;
	}
   /**
    * Sets the comparisonMode
    * @param comparisonMode The comparisonMode to set
    * @throws IllegalArgumentException if the comparisonMode is not recognized
    */
   public void setComparisonMode(int comparisonMode)
   {
   	  if (comparisonMode>4 || comparisonMode<0)
   	  {
   	  	 throw new IllegalArgumentException("Invalid comparisonMode : "+comparisonMode);
   	  }
      this.comparisonMode = comparisonMode;
   }

	/**
	 * Gets the referenceDate
	 * @return Returns a Date
	 */
	public Date getReferenceDate() {
		return referenceDate;
	}
   /**
    * Sets the referenceDate
    * @param referenceDate The referenceDate to set
    */
   public void setReferenceDate(Date referenceDate)
   {
      this.referenceDate = referenceDate;
   }

}

