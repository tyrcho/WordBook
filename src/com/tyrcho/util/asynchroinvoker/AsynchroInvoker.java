package com.tyrcho.util.asynchroinvoker;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Provide service for inkoving a service asynchronously.
 * @author Benoit Devos
 */


public class AsynchroInvoker
{
	private Object obj ;
	private Object [] paramValues ;
	private Method method ;
	
	private Object result ;
	private boolean resultAvailable = false ;
	private boolean exceptionThrown = false ;
	
	private final String lock = "lock" ; // value does not matter.
	
	private class Invoker extends Thread
	{
		public Invoker(String name)
		{
			super("Invoker [" + name + "]") ;
		}
		
		public void run()
		{
			Object tmpResult ;
			try
			{
				tmpResult = _invoke() ;
				synchronized(lock)
				{
					exceptionThrown = false ;
					resultAvailable = true ;
					result = tmpResult ;
				}
			}
			catch(InvocationTargetException ite)
			{
				tmpResult = ite.getTargetException() ;
				synchronized(lock)
				{
					resultAvailable = true ;
					exceptionThrown = true ;
					result = tmpResult ;
				}
			}
			catch(Throwable thr)
			{
				tmpResult = thr ;
				synchronized(lock)
				{
					resultAvailable = true ;
					exceptionThrown = true ;
					result = tmpResult ;
				}
			}
		}
	}
	
	/**
	 * Build a new Asyncro Invoker.
	 * @param obj the object on which method has to be called
	 * @param methodName the name of the method to be called
	 * @param paramValues the array of paramters values for method
	 * @exception NoSuchMethodException if the method does not exist on given object
	 */	
	public AsynchroInvoker(Object obj, String methodName, Object [] paramValues)
	throws NoSuchMethodException
	{
		this.obj = obj ;
		this.paramValues = paramValues ;
		
		Class clazz = obj.getClass() ;
		
		Class [] paramClasses = new Class[paramValues.length] ;
		for (int i = 0 ; i < paramClasses.length ; i ++ )
		{
			paramClasses[i] = paramValues[i].getClass() ;
		}
		method = clazz.getMethod(methodName, paramClasses) ;
	}
	
	/**
	 * Start (asynchronously) the invocation of the method
	 */
	public void invoke()
	{
		synchronized(lock)
		{
			resultAvailable = false ;
			new Invoker(obj.getClass().getName()).start() ;
		}
	}
	
	private Object _invoke()
	throws InvocationTargetException, IllegalAccessException
	{
		return method.invoke(obj, paramValues) ;
		
	}
	
	/**
	 * Check if the invocation if finished
	 * @return true if the invocation is finished
	 * @return false if invocation is still running
	 */
	public boolean isResultAvailable()
	{
		synchronized(lock)
		{
			return resultAvailable ;
		}
	}
	
	/**
	 * Provide the result of the invocation.
	 * NOTE: Should only be used if isResultAvailable() returned true.
	 * @return the result if the invocation is finished AND if method has actually returned something different from null
	 * @return null if the invocation is not finished OR if the method returned null
	 */
	public Object getResult()
	throws Throwable
	{
		synchronized(lock)
		{
			if (exceptionThrown) throw (Throwable) result ;
			
			return result ;
		}
	}

}