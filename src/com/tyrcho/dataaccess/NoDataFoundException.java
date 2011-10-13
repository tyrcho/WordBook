package com.tyrcho.dataaccess;

import com.tyrcho.exception.FunctionalException;

/**
 * Class defining the exception thrown in case of data not found in a select.
 */

public class NoDataFoundException extends FunctionalException
{
		/**
	 * Constructor for NoDataFoundException
	 *
     * @param severity the severity of the NpFunctionalException, which should be obtained by a constant
     * @param module the calling module
     * @param comment the full description with the detailed procedure to investigate the error
     * @param messageId the id of the message which caused the error
     * @param nestedException the nested exception which caused the error
	 */
	public NoDataFoundException(String description
										,String severity
										,String module
										,String comment
										,String messageId
										,Throwable nestedException)
	{
		super(description, severity, module, comment, messageId, nestedException);
	}

	/**
	 * Constructor for NoDataFoundException
     * 
     * @param module the calling module
     * @param comment the full description with the detailed procedure to investigate the error
     * @param messageId the id of the message which caused the error
	 */
	public NoDataFoundException(String description
								,String module
								,String comment
								,String messageId)
	{
		super(description, module, comment, messageId);
	}


}

