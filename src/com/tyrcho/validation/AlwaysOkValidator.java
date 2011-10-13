package com.tyrcho.validation;

/**
 * Never fails.
 * @author MDA
 */
public class AlwaysOkValidator<T> extends AbstractValidator<T>
{
	/**
	 * Never fails.
	 */
   public void validate(T value) 
   {
   }
}

