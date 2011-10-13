package info.daviot.util.validation;

/**
 * Validates an object by checking its String representation with a regexp pattern
 * representing an Integer ([1-9][0-9]*).
 * Uses GNU regexp package.
 * @see gnu.regexp.RE
 * 
 * @author MDA
 * @version NP
 */
public class IntegerValidator extends PatternValidator
{
	private static final String pattern="[1-9][0-9]*";
	
   /**
    * Constructs the IntegerValidator 
    * with a constant reason used to describe the possible ValidationFailedExceptions.
    *
    * @param isEmptyAllowed if true null or "" representations will be allowed
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    */
   public IntegerValidator(boolean isEmptyAllowed, String reason)
   {
   	  super(pattern, reason, isEmptyAllowed);
   }

   /**
    * Constructs the IntegerValidator ;
    * a default reason is generated from the pattern and the tested object
    * to describe the ValidationFailedExceptions.
    * 
    * @param isEmptyAllowed if true null or "" representations will be allowed
    */
   public IntegerValidator(boolean isEmptyAllowed)
   {
   	  this(isEmptyAllowed, null);
   }
}

