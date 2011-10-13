package com.tyrcho.gui.toolkit;

import com.tyrcho.gui.field.ITextField;


public class TextArea
extends javax.swing.JTextArea implements ITextField
{
    
    private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;
    
    public TextArea()
    {
        super() ;
        toolkit.initTextArea(this) ;
    }
    
    public TextArea(int rows, int columns)
    {
        super(rows, columns) ;
        toolkit.initTextArea(this) ;
    }
    
    public TextArea(String initialText)
    {
        super(initialText) ;
        toolkit.initTextArea(this) ;
    }
    
	public Object getCurrentValue()
	{
		return getText();
	}    
	
	public void setCurrentValue(Object value)
	{
		setText(value==null?"":value.toString());
	}

	public void clear()
	{
		setText("");
	}
}
