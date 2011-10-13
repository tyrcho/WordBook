package info.daviot.gui.toolkit;

import javax.swing.JCheckBox;

public class CheckBox extends JCheckBox
{
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;

	public CheckBox()
	{
		super() ;
		toolkit.initCheckBox(this) ;
	}
}
