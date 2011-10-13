package com.tyrcho.gui.toolkit;

import java.awt.Component;

public class SplitPane extends javax.swing.JSplitPane
{

    private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;
    
    public SplitPane()
    {
        super() ;
        toolkit.initSplitPane(this) ;
    }

    public SplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent)
    {
        super(newOrientation, newLeftComponent, newRightComponent);
        toolkit.initSplitPane(this) ;
    }
}
