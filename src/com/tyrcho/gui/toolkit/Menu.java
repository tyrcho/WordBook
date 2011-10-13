package com.tyrcho.gui.toolkit;

import javax.swing.JMenu;

public class Menu extends JMenu {
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;    

	public Menu(String text) {
		super(text);
		toolkit.initMenu(this);
	}
}

