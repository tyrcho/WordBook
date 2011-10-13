package info.daviot.gui.toolkit;

import info.daviot.gui.field.ITextField;

public class ComboBox 
extends javax.swing.JComboBox implements ITextField
{
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;

	public ComboBox()
	{
		super() ;
		toolkit.initComboBox(this) ;
	}

	public ComboBox(Object[] rowData) {
		super(rowData);
		toolkit.initComboBox(this) ;
	}
	
	public Object getCurrentValue()
	{
		return getSelectedItem();
	}
	
	public String getText()
	{
		return getSelectedItem().toString();
	}
	
	public void setCurrentValue(Object value)
	{
		setSelectedItem(value);
	}
	
	public void clear()
	{
		setSelectedItem("");
	}
}
