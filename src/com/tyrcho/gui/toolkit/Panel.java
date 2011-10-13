package com.tyrcho.gui.toolkit;

import java.awt.LayoutManager;

public class Panel extends javax.swing.JPanel {
    
    private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;
    
    public Panel()
    {
        super() ;
        toolkit.initPanel(this) ;
    }
    
    public Panel(LayoutManager layout)
    {
        super(layout) ;
        toolkit.initPanel(this) ;
    }
}

