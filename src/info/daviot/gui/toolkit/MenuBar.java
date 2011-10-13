package info.daviot.gui.toolkit;


public class MenuBar extends javax.swing.JMenuBar
{
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;

	public MenuBar()
	{
		super() ;
		toolkit.initMenuBar(this) ;
	}
}
