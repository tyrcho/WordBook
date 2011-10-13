package info.daviot.util.validation;

/**
 * Abstract validator which allows to specify a default reason
 * for FailedValidationException thrown.
 * 
 * @author MDA
 * @version NP
 */
public abstract class AbstractValidator implements IValidator
{
   private String reason;
   
   /**
    * Constructs the AbstractValidator 
    * with a constant reason used to describe the possible ValidationFailedExceptions.
    *
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    */
   public AbstractValidator(String reason)
   {
   	  setReason(reason);
   }

   /**
    * Constructs the AbstractValidator.
    * A default reason has to be generated from the validator and the tested object
    * to describe the ValidationFailedExceptions in the implementation of validate.
    */
   public AbstractValidator()
   {
   	  this(null);
   }

   /**
    * Gets the reason.
    * @return the reason used to construct the ValidationFailedException thrown during validation
    * or null if a default reason is constructed from the format
    */
   public String getReason()
   {
      return reason;
   }

   /**
    * Sets the reason.
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    */
   public void setReason(String reason)
   {
      this.reason = reason;
   }
}

