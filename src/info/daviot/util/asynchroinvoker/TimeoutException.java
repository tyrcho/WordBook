package info.daviot.util.asynchroinvoker;


/**
 * Raised whenever a Timed Invocation has not replied within the given time limit
 * @author Benoit Devos
 */


public class TimeoutException extends Exception
{
	public TimeoutException(String descr)
	{
		super(descr) ;
	}
}


