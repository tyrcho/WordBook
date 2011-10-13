package com.tyrcho.gui.toolkit;

import java.awt.Component;

public class TabbedPane extends javax.swing.JTabbedPane
{
    private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;
	
	public TabbedPane()
	{
		super();
		toolkit.initTabbedPane(this);
	}
	
	public Component getTab(String title)
	{
		int index=indexOfTab(title);
		return getComponent(index);
	}

}

