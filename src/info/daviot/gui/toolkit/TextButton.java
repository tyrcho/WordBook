package info.daviot.gui.toolkit;

import javax.swing.JButton;

public class TextButton extends JButton
{
    private static transient Toolkit toolkit = ToolkitFactory.getToolkit();
    
    public TextButton(String text)
    {
        super(text) ;
        toolkit.initTextButton(this) ;
    }
    
}
