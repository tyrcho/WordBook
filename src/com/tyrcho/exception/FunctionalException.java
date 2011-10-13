package com.tyrcho.exception;


/** 
 * Bug exception class for NP.
 * 
 * @author MDA
 * @version NP
 */
public class FunctionalException extends AbstractException
{
   private static final String FUNCTIONAL_TYPE="functional";

   /** 
    * Constructs an NpFunctionalException linked to a message and a nested Exception.
    * This exception can be thrown when an error occurs on the data.
    * 
    * @param description short description of the error, could be obtained by the parent error
    * @param severity the severity of the NpFunctionalException, which should be obtained by a constant
    * @param module the calling module
    * @param comment the full description with the detailed procedure to investigate the error
    * @param messageId the id of the message which caused the error
    * @param nestedException the nested exception which caused the error
    */
   public FunctionalException(
      String description,
      String severity,
      String module,
      String comment,
      String messageId,
      Throwable nestedException)
   {
      super(FUNCTIONAL_TYPE, description, severity, module, comment, messageId, nestedException);
   }

   /** 
    * Constructs an NpFunctionalException linked to a message and a nested Exception.
    * The default severity here is WARNING.
    * 
    * @param description short description of the error, could be obtained by the parent error
    * @param module the calling module
    * @param comment the full description with the detailed procedure to investigate the error
    * @param messageId the id of the message which caused the error
    */
   public FunctionalException(
      String description,
      String module,
      String comment,
      String messageId)
   {
      super(FUNCTIONAL_TYPE, description, WARNING, module, comment, messageId, null);
   }
}
