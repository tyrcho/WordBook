package com.tyrcho.validation;

public class IpAddressValidator<T> extends PatternValidator<T>
{
	private static final String shortPattern = "\\d{1,3}";
	private static final String pattern = shortPattern + "(\\." + shortPattern
			+ "){3}";

	public IpAddressValidator(String reason)
	{
		super(pattern, reason, false);
	}

	@Override
	public void validate(T value) throws FailedValidationException
	{
		super.validate(value);
		String stringValue = value.toString();
		for (String element : stringValue.split("\\."))
		{
			int elementValue = Integer.valueOf(element);
			if (elementValue < 0 || elementValue > 255) { throw new FailedValidationException(
					getReason() == null ? elementValue
							+ " n'est pas un entier entre 0 et 255"
							: getReason(), value); }
		}
	}
}
