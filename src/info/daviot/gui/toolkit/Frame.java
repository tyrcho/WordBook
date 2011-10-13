package info.daviot.gui.toolkit;

public class Frame extends javax.swing.JFrame {
    
    private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;
    
    public Frame()
    {
        super() ;
        toolkit.initFrame(this) ;
    }
    
    public Frame(String title) 
    {
        super(title) ;
        toolkit.initFrame(this) ;
    }
    
    
}
