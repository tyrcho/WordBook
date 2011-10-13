/*
 * Label.java
 *
 * Created on 28 January 2003, 15:20
 */

package info.daviot.gui.toolkit;

public class Label extends javax.swing.JLabel
{
	public static final int DEFAULT = 1 ;
	public static final int TITLE = 2 ;
	public static final int STRONG = 3 ;

	private int type = DEFAULT;
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;    

	public Label(String text, int type) {
		super(text);
		this.type=type;
		toolkit.initLabel(this);
	}

	public Label(String text) {
		this(text, DEFAULT);
	}

	public int getType()
	{
		return this.type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

}
