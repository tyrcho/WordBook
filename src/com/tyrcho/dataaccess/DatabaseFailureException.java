package com.tyrcho.dataaccess;

import java.sql.SQLException;

import com.tyrcho.exception.AbstractException;

/**
 * Class defining the exception thrown in case of database failure. The DatabaseFailureException is
 * the database exception used when the error is not identified (and so thrown by a 
 * NpFunctionalException).
 * @author Philippe Paulus
 * @version FNP
 */

public class DatabaseFailureException extends AbstractException
{

   private static final String TYPE = "DatabaseFailure";
   private int sqlErrorCode = 0;

   /**
    * Constructor for DatabaseFailureException.
    * 
    * @param description short description of the error, could be obtained by the parent error
    * @param module the calling module
    * @param comment the full description with the detailed procedure to investigate the error
    * @param messageId the id of the message which caused the error
    * @param nestedException the nested exception which caused the error
    */
   public DatabaseFailureException(
      String description,
      String module,
      String comment,
      String messageId,
      Throwable nestedException)
   {	
      super(
         TYPE,
         description,
         ERROR,
         module,
         comment,
         messageId,
         nestedException);
   }

   /**
    * Constructor for DatabaseFailureException which stores the SQL error code.
    * 
    * @param description short description of the error, could be obtained by the parent error
    * @param module the calling module
    * @param comment the full description with the detailed procedure to investigate the error
    * @param messageId the id of the message which caused the error
    * @param nestedException the nested exception which caused the error
    */
   public DatabaseFailureException(
      String description,
      String module,
      String comment,
      String messageId,
      SQLException nestedException)
   {
      this(description, module, comment, messageId, (Throwable)nestedException);
      if (nestedException!=null)
      {
     	 setSqlErrorCode(nestedException.getErrorCode());
      }
   }

   /**
    * Constructor for DatabaseFailureException.
    *
    * @param description short description of the error, could be obtained by the parent error
    * @param module the calling module
    * @param comment the full description with the detailed procedure to investigate the error
    */
   public DatabaseFailureException(
      String description,
      String module,
      String comment)
   {

      this(description, module, comment, null, null);
   }

   /**
    * Gets the sql Error Code thrown by Oracle.
    * 
    * @return the error code
    */
   public int getSqlErrorCode()
   {
      return sqlErrorCode;
   }
   
   private void setSqlErrorCode(int sqlErrorCode)
   {
      this.sqlErrorCode = sqlErrorCode;
   }

}