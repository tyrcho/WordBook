package info.daviot.gui.toolkit;

import javax.swing.JLabel;

public class ImageLabel extends JLabel
{
    protected static transient Toolkit toolkit = ToolkitFactory.getToolkit();
    private int type;
    
    /**
     * Constructs a label with the specified code;
     * the toolkit is responsible to find the matching image.
     * 
     * @param type
     */
    public ImageLabel(int type)
    {
        super();
        setType(type);
        toolkit.initImageLabel(this) ;
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

