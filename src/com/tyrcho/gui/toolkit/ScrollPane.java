package com.tyrcho.gui.toolkit;

import java.awt.Component;

public class ScrollPane extends javax.swing.JScrollPane
{

    private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;
    
    public ScrollPane()
    {
        super() ;
        toolkit.initScrollPane(this) ;
    }

    public ScrollPane(Component view)
    {
        super(view) ;
        toolkit.initScrollPane(this) ;
    }
}
