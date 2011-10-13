package info.daviot.util.validation;

/**
 * Never fails.
 * @author MDA
 */
public class AlwaysOkValidator extends AbstractValidator
{
	/**
	 * Never fails.
	 */
   public void validate(Object value) throws FailedValidationException
   {
   }
}

