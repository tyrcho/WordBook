package com.tyrcho.gui.toolkit;

import javax.swing.JMenuItem;

public class MenuItem extends JMenuItem
{
    private static transient Toolkit toolkit = ToolkitFactory.getToolkit();
    
    public MenuItem(String text)
    {
        super(text) ;
        toolkit.initMenuItem(this) ;
    }
    
}
