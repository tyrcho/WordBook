package info.daviot.gui.toolkit;

import info.daviot.gui.field.ITextField;


public class TextField 
extends javax.swing.JTextField implements ITextField
{

	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;

	public TextField()
	{
		super() ;
		toolkit.initTextField(this) ;
	}

	public TextField(int columns)
	{
		super(columns) ;
		toolkit.initTextField(this) ;
	}

	public TextField(String initialText)
	{
		super(initialText) ;
		toolkit.initTextField(this) ;
	}

	public TextField(String initialText, int columns)
	{
		super(initialText, columns) ;
		toolkit.initTextField(this) ;
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
