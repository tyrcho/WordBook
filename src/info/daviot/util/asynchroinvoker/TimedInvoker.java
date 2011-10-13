package info.daviot.util.asynchroinvoker;



/**
 * Provide support for providing a time limit to any synchrone call.
 * @author Benoit Devos
 */


public class TimedInvoker
{
	private AsynchroInvoker asynchroInvoker ;
	private long TIMEOUT_STEP = 50 ; // must be > 0
	
	private Object obj ;
	private String methodName ;
	
	/**
	 * Build a new Timed Invoker.
	 * @param obj the object on which method has to be called
	 * @param methodName the name of the method to be called
	 * @param paramValues the array of paramters values for method
	 * @exception NoSuchMethodException if the method does not exist on given object
	 */	
	public TimedInvoker(Object obj, String methodName, Object [] paramValues)
	throws NoSuchMethodException
	{
		asynchroInvoker = new AsynchroInvoker(obj, methodName, paramValues) ;
		
		// keep a link to these object only for generating the exception
		this.obj = obj ;
		this.methodName = methodName ;
	}
	
	/**
	 * Invoke the method.
	 * @param timeout the maximum time (in ms) that we can wait for
	 * @return the result of the invocation
	 * @exception TimeoutException if the given timeout is elapsed and no result is available
	 */
	public Object invoke(long timeout)
	throws TimeoutException, Throwable
	{
		long startTime = System.currentTimeMillis() ;
		long endTime = startTime + timeout ;
		
		asynchroInvoker.invoke() ;
		
		while (endTime-System.currentTimeMillis() > 0)
		{
			if (asynchroInvoker.isResultAvailable()) return asynchroInvoker.getResult() ;
			
			long sleepTime = 0;
			if ((endTime-System.currentTimeMillis()) > TIMEOUT_STEP)
				sleepTime = TIMEOUT_STEP ;
			else
				sleepTime = endTime-System.currentTimeMillis() ;
			
			if (sleepTime > 0)
			{
				Thread.sleep(sleepTime) ;
			}
		}
		
		if (asynchroInvoker.isResultAvailable()) return asynchroInvoker.getResult() ;
		
		throw new TimeoutException("Timeout while invoking service <" + methodName + "> on object of type <" + obj.getClass().getName() + ">") ;
	}
}


