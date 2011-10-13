/*
 * ToolkitFactory.java
 *
 * Created on 07 February 2003, 12:57
 */

package com.tyrcho.gui.toolkit;


/**
 *
 * @author  id804958
 */
public class ToolkitFactory {
    
    private static Toolkit toolkit ;
    private static String toolkitClassName ; 
    private static final String DEFAULT_TOOLKIT = "com.tyrcho.gui.toolkit.AbstractToolkit" ;
    
    /**
     * Sets the toolkit used for the application.
     * 
     * @param className a fully qualified java class name 
     * (must implement Tollkit interface)
     */
    public static void setToolkit(String className)
    {
        toolkitClassName=className;
    }
    
    
        
    public static Toolkit getToolkit()
    {
        if (toolkit==null)
        {
	        try
	        {
	        	Class toolkitClass = Class.forName(toolkitClassName) ;
	            toolkit = (Toolkit) toolkitClass.newInstance() ;
	        }
	        catch(Exception e)
	        {
	            trace("Trouble for loading toolkit <" + toolkitClassName + ">") ;
	            trace("=> " + e) ;
	            toolkitClassName=DEFAULT_TOOLKIT;
	            try
	            {
		            Class toolkitClass = Class.forName(toolkitClassName) ;
		            toolkit = (Toolkit) toolkitClass.newInstance() ;                       
	            }
		        catch(Exception e2)
		        {
		        	trace ("Could not load default toolkit " +toolkitClassName);
		        	System.exit(1);
		        }
	        }
	        finally
	        {
	            trace("Toolkit loaded <" + toolkit.getClass().getName() + ">") ;
	        }
        }
        
        return toolkit ;
    }
    
    private static void trace(String msg)
    {
        System.out.println(msg) ;
    }
    
}
