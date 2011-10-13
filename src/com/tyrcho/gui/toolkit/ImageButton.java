package com.tyrcho.gui.toolkit;

import javax.swing.Action;
import javax.swing.JButton;

/**
 * JButton with an image (up to inherited classes).
 * 
 * @author MDA
 * @version NP
 */
public class ImageButton extends JButton
{
    protected static transient Toolkit toolkit = ToolkitFactory.getToolkit();
    private int type;
    
    /**
     * Constructs a button to the specified type;
     * the toolkit is responsible to find the matching image.
     * 
     * @param buttonType
     */
    public ImageButton(int buttonType)
    {
        super();
        setType(buttonType);
        toolkit.initImageButton(this) ;
    }
    
	public ImageButton(int buttonType, Action action)
	{
		super(action);
		setType(buttonType);
		setText(null);
		toolkit.initImageButton(this) ;
	}
    
    
	/**
	 * Gets the type
	 * @return Returns a int
	 */
	public int getType() {
		return type;
	}
   /**
    * Sets the type
    * @param type The type to set
    */
   public void setType(int type)
   {
      this.type = type;
   }

}
