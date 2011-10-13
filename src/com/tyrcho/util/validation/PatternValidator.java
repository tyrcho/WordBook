package com.tyrcho.util.validation;



/**
 * Validates an object by matching its String representation with a regexp pattern.
 * Uses GNU regexp package.
 * @see gnu.regexp.RE
 * 
 * @author MDA
 * @version NP
 */
public class PatternValidator extends AbstractValidator
{
   private String pattern;
   private boolean emptyAllowed;

   /**
    * Constructs the PatternValidator on the given pattern
    * with a constant reason used to describe the possible ValidationFailedExceptions.
    *
    * @param pattern the pattern used to construct the regular expression used for validation
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    * @param emptyAllowed specifies if an empty String is allowed
    * @throws RuntimeException if the pattern is invalid
    */
   public PatternValidator(String pattern, String reason, boolean emptyAllowed)
   {
   	  super(reason);
   	  this.emptyAllowed=emptyAllowed;
   	  if (emptyAllowed)
   	  {
   	  	 pattern="("+pattern+")|()";
   	  }   	  
   	  setPattern(pattern);
   }

   /**
    * Constructs the PatternValidator on the given pattern.
    * A default reason is generated from the pattern and the tested object
    * to describe the ValidationFailedExceptions.
    * If not specified in the pattern, empty Strings will be rejected by default.
    *
    * @param pattern the pattern used to construct the regular expression used for validation
    * @throws RuntimeException if the pattern is invalid
    */
   public PatternValidator(String pattern)
   {
   	  this(pattern, null, false);
   }

   /**
    * Validates value by matching its String representation with the regexp pattern.
    *
    * @param value the object to validate
    * @throws ValidationFailedException when the validation failed
    */
   public void validate(Object value) throws FailedValidationException
   {
   	   if (emptyAllowed && value==null)
   	   {
   	   	  return;
   	   }
   	   if (value==null || !value.toString().trim().matches(pattern))//!regexp.isMatch(value.toString().trim()))
   	   {
   	   	  if (getReason()==null)
   	   	  {
   	   	  	 throw new FailedValidationException("does not match pattern "+pattern, value);
   	   	  }
   	   	  else 
   	   	  {
             throw new FailedValidationException(getReason(), value);   	   	  
          }
   	   }
   }

   /**
    * Gets the pattern.
    * @return the pattern used to construct the regular expression used for validation
    */
   public String getPattern()
   {
      return pattern;
   }

   /**
    * Sets the pattern.
    * @param pattern the pattern used to construct the regular expression used for validation
    */
   public void setPattern(String pattern)
   {
      this.pattern = pattern;
   }
}