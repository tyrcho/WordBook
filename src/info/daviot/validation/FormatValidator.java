package info.daviot.validation;

import java.text.Format;
import java.text.ParseException;

/**
 * Validates an object by matching its String representation with a 
 * {@link java.text.Format} object.
 * This is useful instead of {@link be.belgacom.itg.common.util.validation.PatternValidator}
 * when you already use a SimpleDateFormat object for instance.
 * 
 * @author MDA
 * @version NP
 */
public class FormatValidator<T> extends AbstractValidator<T>
{
   private Format format;
   private boolean emptyAllowed;

   /**
    * Constructs the FormatValidator on the given format
    * with a constant reason used to describe the possible ValidationFailedExceptions.
    *
    * @param format the format used for validation
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    * @param emptyAllowed specifies if an empty representation "" is allowed
    */
   public FormatValidator(Format format, String reason, boolean emptyAllowed)
   {
   	  super(reason);
   	  setFormat(format);
   	  setEmptyAllowed(emptyAllowed);
   }

   /**
    * Constructs the FormatValidator on the given format.
    * A default reason is generated from the format and the tested object
    * to describe the ValidationFailedExceptions.
    * By default, an empty representation "" for an object is not allowed.
    *
    * @param format the format used for validation
    */
   public FormatValidator(Format format)
   {
   	  this(format, null, false);
   }

   /**
    * Validates value by matching its String representation with the format.
    *
    * @param value the object to validate
    * @throws ValidationFailedException when the validation failed
    */
   public void validate(T value) throws FailedValidationException
   {
   	   if (isEmptyAllowed() && "".equals(value.toString()))
   	   {
   	   	  return;
   	   }
   	   try
   	   {
   	   	  format.parseObject(value.toString());
   	   }
   	   catch (ParseException e)
   	   {
   	   	  if (getReason()==null)
   	   	  {
   	   	  	 throw new FailedValidationException("does not match format "+format, value);
   	   	  }
   	   	  else 
   	   	  {
             throw new FailedValidationException(getReason(), value);   	   	  
          }
   	   }
   }

   /**
    * Gets the format.
    * @return the format used for validation
    */
   public Format getFormat()
   {
      return format;
   }

   /**
    * Sets the format.
    * @param format the format used for validation
    */
   public void setFormat(Format format)
   {
      this.format = format;
   }
	
	/**
	 * Tests if an object with an empty representation "" is allowed.
	 */
	public boolean isEmptyAllowed() 
	{
		return emptyAllowed;
	}
 
   /**
    * @param emptyAllowed specifies if an empty representation "" is allowed
    */
   public void setEmptyAllowed(boolean emptyAllowed)
   {
      this.emptyAllowed = emptyAllowed;
   }

}
