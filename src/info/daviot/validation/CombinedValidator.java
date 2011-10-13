package info.daviot.validation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Combines several validations in a single object.
 * 
 * @author MDA
 * @version NP
 */
public class CombinedValidator<T> implements I_Validator<T>
{
	private Collection<I_Validator<? super T>> validators; // Collection of IValidator

	/**
	 * Constructs the validator as a combination of several validators.
	 * 
	 * @param validators
	 *            a collection of IValidator objects
	 */
	public CombinedValidator(Collection<I_Validator<? super T>> validators)
	{
		this.validators = validators;
	}

	/**
	 * Validates the value by running all validations specified by the
	 * validators which this object combines.
	 * 
	 * @param value
	 *            the object to validate
	 * @throws SeveralFailedValidationsException
	 *             when the validation failed
	 */
	public void validate(T value) throws FailedValidationException
	{
		List<FailedValidationException> failedValidationExceptions = new LinkedList<FailedValidationException>();
		boolean failedValidation = false;
		for (I_Validator<? super T> validator : validators)
		{
			try
			{
				validator.validate(value);
			}
			catch (FailedValidationException e)
			{
				failedValidationExceptions.add(e);
				failedValidation = true;
			}
		}
		if (failedValidation) { throw new SeveralFailedValidationsException(
				failedValidationExceptions, value); }
	}
}
