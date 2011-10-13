package com.tyrcho.util.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Applies a group of validators to a group of objects.
 * The groups are represented by a Map where the keys
 * are the same between validators and value objects.
 * 
 * @author MDA
 * @version NP
 */
public class GroupValidator extends AbstractValidator
{
   private Map<Object, IValidator> validatorMap; //HashMap of IValidator

   /**
    * Constructs the validator on a group of validators.
    *
    * @param validatorMap a map of validators, indexed by a set of keys;
    * these keys will be reused in the validated object
    */
   public GroupValidator(Map<Object, IValidator> validatorMap)
   {
      this(validatorMap,null);
   }
   
   /**
    * Constructs the validator on a group of validators.
    *
    * @param validatorMap a map of validators, indexed by a set of keys;
    * these keys will be reused in the validated object
    * @param reason the reason used to construct the ValidationFailedException thrown during validation
    */
   public GroupValidator(Map<Object, IValidator> validatorMap, String reason)
   {
      super(reason);
      this.validatorMap=validatorMap;
   }
   
   /**
    * Constructs an empty group of validators.
    */
   public GroupValidator()
   {
      this(new HashMap<Object, IValidator>());
   }
   
   /**
    * Puts a validator in the group.
    * 
    * @param key used to index the validator in the map
    * @param validator the validator used to check objects with this key
    */
   public void putValidator(String key, IValidator validator)
   {
   	  validatorMap.put(key, validator);
   }
   
   /**
    * Gets a validator from the group.
    * 
    * @param key used to index the validator in the map
    * @return the validator used to check objects with this key
    */
   public IValidator get(String key)
   {
   	  return validatorMap.get(key);
   }
   
   /**
    * Validates a map of objects by applying to each object in the map
    * to the corresponding validator (same key).
    *
    * @param value the HashMap to validate
    * @throws SeveralFailedValidationsException when the validation failed 
    * @throws ClassCastException if value cannot be assigned to a Map
    */
   public void validate(Object value) throws FailedValidationException
   {
      List<FailedValidationException> failedValidationExceptions=new LinkedList<FailedValidationException>();
      boolean failedValidation=false;
      Map valueGroup=(Map)value;
      Iterator i=validatorMap.keySet().iterator();
      while (i.hasNext())
      {
      	 Object key=i.next(); 
         IValidator validator=(IValidator) validatorMap.get(key);
         Object validatedObject=valueGroup.get(key);
         try
         {
            validator.validate(validatedObject);
         }
         catch (FailedValidationException e)
         {
            failedValidationExceptions.add(e);
            failedValidation=true;
         }
      }
      if (failedValidation)
      {
      	 if (getReason()==null)
      	 {
	         throw new SeveralFailedValidationsException(failedValidationExceptions, value);
      	 }
	     else
	     {
	         throw new SeveralFailedValidationsException(failedValidationExceptions, value, getReason());
	     }
      }
   }
}
