/*
 * ToolkitException.java
 *
 * Created on 10 February 2003, 11:04
 */

package com.tyrcho.gui.toolkit;

/**
 *
 * @author  id804958
 */
public class ToolkitException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>ToolkitException</code> without detail message.
     */
    public ToolkitException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>ToolkitException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ToolkitException(String msg)
    {
        super(msg);
    }
}
