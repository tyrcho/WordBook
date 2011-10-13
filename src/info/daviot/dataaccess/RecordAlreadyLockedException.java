package info.daviot.dataaccess;

import info.daviot.exception.FunctionalException;

/**
 * This class defines the exception thrown when
 * a lock is requested on a database record, and that another user (or process) has already locked it.
 * 
 * @author Benoit Devos
 * @version FNP
 */

public class RecordAlreadyLockedException extends FunctionalException
{
	private String lockingUserId;

	/**
	 * Constructor for RecordAlreadyLockedException
	 *
     * @param userId the user who locked the record
     * @param severity the severity of the NpFunctionalException, which should be obtained by a constant
     * @param module the calling module
     * @param comment the full description with the detailed procedure to investigate the error
     * @param messageId the id of the message which caused the error
     * @param nestedException the nested exception which caused the error
	 */
	public RecordAlreadyLockedException(String userId
										,String severity
										,String module
										,String comment
										,String messageId
										,Throwable nestedException)
	{
		super("["+userId+"]", severity, module, comment, messageId, nestedException);
		setLockingUserId(userId);
	}


	/**
	 * Constructor for RecordAlreadyLockedException
	 *
     * @param severity the severity of the NpFunctionalException, which should be obtained by a constant
     * @param module the calling module
     * @param comment the full description with the detailed procedure to investigate the error
     * @param messageId the id of the message which caused the error
     * @param nestedException the nested exception which caused the error
	 */
	public RecordAlreadyLockedException(String severity
										,String module
										,String comment
										,String messageId
										,Throwable nestedException)
	{
		this("Unknown user", severity, module, comment, messageId, nestedException);
	}

	/**
	 * Constructor for RecordAlreadyLockedException
	 *
     * @param module the calling module
     * @param comment the full description with the detailed procedure to investigate the error
     * @param messageId the id of the message which caused the error
	 */
	public RecordAlreadyLockedException(String module
									   ,String comment
									   ,String messageId)
	{
		this(DEFAULT_SEVERITY, module, comment, messageId, null);
	}

	/**
	 * Gets the lockingUserId.
	 * 
	 * @return the user who locked the record
	 */
	public String getLockingUserId() {
		return lockingUserId;
	}
   /**
    * Sets the lockingUserId.
    * 
     * @param userId the user who locked the record
    */
   public void setLockingUserId(String lockingUserId)
   {
      this.lockingUserId = lockingUserId;
   }

}

