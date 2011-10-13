package info.daviot.util.validation;

/**
 * Specifies the interface for a validator object.
 * 
 * @author MDA
 * @version NP
 */
public interface IValidator
{
   /**
    * Implementations of this interface will define this method
    * to validate a value.
    *
    * @param value the object to validate
    * @throws FailedValidationException when the validation failed
    */
   public void validate(Object value) throws FailedValidationException;

}
