package info.daviot.gui.toolkit;

import javax.swing.JRadioButton;


public class RadioButton extends JRadioButton
{
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;    

	public RadioButton(String label)
	{
		super(label);
		toolkit.initRadioButton(this);    		
	}
}

