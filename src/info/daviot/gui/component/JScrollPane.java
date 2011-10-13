package info.daviot.gui.component;

import java.awt.Component;

import javax.swing.JScrollBar;

public class JScrollPane extends javax.swing.JScrollPane {
	public JScrollPane(Component view)
	{
		super(view) ;
	}

	public JScrollPane()
	{
		super() ;
	}
	
	/**
	 * Should reset the scroll bars to the top left.
	 */
	public void scrollToBeginning()
	{
		JScrollBar verticalScrollBar=getVerticalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMinimum());
		JScrollBar horizontalScrollBar=getHorizontalScrollBar();
		horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
		revalidate();
	}
}

