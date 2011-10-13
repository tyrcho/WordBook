package info.daviot.util.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This exception is thrown when a validation fails
 * because of several sub-validations.
 * 
 * @author MDA
 * @version NP
 */
public class SeveralFailedValidationsException extends FailedValidationException
{
   Collection<FailedValidationException> failedValidationExceptions;

   /**
    * Constructs a new SeveralFailedValidationsException.
    *
    * @param failedValidationExceptions a collection of FailedValidationException exceptions
    * which describe the failed sub-validations
    * @param valueTested the object which was validated
    * @param reason the detailed cause of the failure in the validation
    */
   public SeveralFailedValidationsException(
   		Collection<FailedValidationException> failedValidationExceptions, 
   		Object valueTested, 
   		String reason)
   {
      super(reason, valueTested);
      this.failedValidationExceptions=failedValidationExceptions;
   }

   /**
    * Constructs a new SeveralFailedValidationsException.
    *
    * @param failedValidationExceptions a collection of FailedValidationException exceptions
    * which describe the failed sub-validations
    * @param valueTested the object which was validated
    */
   public SeveralFailedValidationsException(Collection<FailedValidationException> failedValidationExceptions, Object valueTested)
   {
      this(failedValidationExceptions, valueTested, null);//getCombinedReason(failedValidationExceptions));
   }

   /**
    * Recursively returns all reasons for which this validation failed.
    * If a validation failed because of a sub-validation, only lowest-level validations
    * will be returned.
    * This means only the leaves of the tree of FailedValidationException are returned.
    *
    * @return a LinkedList of FailedValidationException
    */
   public Collection<FailedValidationException> getAllReasons()
   {
      LinkedList<FailedValidationException> allFailedValidationExceptions=new LinkedList<FailedValidationException>();
      Iterator<FailedValidationException> i=failedValidationExceptions.iterator();
      while (i.hasNext())
      {
      	 FailedValidationException failedValidation=(FailedValidationException)i.next();
         if (failedValidation instanceof SeveralFailedValidationsException)
         {
            SeveralFailedValidationsException severalValidationsFailedException=
               (SeveralFailedValidationsException)failedValidation;
            allFailedValidationExceptions.addAll(severalValidationsFailedException.getAllReasons());
         }
         else
         {
            allFailedValidationExceptions.add(failedValidation);
         }
      }
      return allFailedValidationExceptions;
   }
   
   public String getReason() {
       String reason=super.getReason();
       if (reason==null) {
           return getCombinedReason(getAllReasons());
       } else {
           return reason;
       }
   }

   private static String getCombinedReason(Collection<FailedValidationException> failedValidationExceptions)
   {
      StringBuffer combinedReasons=new StringBuffer();      
      Iterator i=failedValidationExceptions.iterator();
      while (i.hasNext())
      {
      	 FailedValidationException failedValidation=(FailedValidationException)i.next();
         combinedReasons.append(failedValidation.getReason());
         combinedReasons.append(System.getProperty("line.separator"));
      }
      return combinedReasons.toString();
   }
}
