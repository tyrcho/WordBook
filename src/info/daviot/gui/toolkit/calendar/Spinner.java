package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.toolkit.Box;
import info.daviot.gui.toolkit.Panel;
import info.daviot.gui.toolkit.Toolkit;
import info.daviot.gui.toolkit.ToolkitFactory;
import info.daviot.gui.toolkit.event.RelativeChangeEvent;
import info.daviot.gui.toolkit.event.RelativeChangeListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.plaf.basic.BasicArrowButton;



/**
 * Represents a 2-buttons vertical component which can fire events
 * when "up" or "down" is clicked.
 * Usefull to quickly increase or decrease a value by one unit.
 * 
 * @author MDA
 * @version NP
 */
public class Spinner extends Panel
{
    private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;
    private int oldValue=0;
    private Collection relativeChangeListeners=new LinkedList();
    private BasicArrowButton upButton;
    private BasicArrowButton downButton;

	public Spinner()
	{		
		super(new BorderLayout());
		upButton=new BasicArrowButton(BasicArrowButton.NORTH);
		downButton=new BasicArrowButton(BasicArrowButton.SOUTH);
		javax.swing.Box content=Box.createVerticalBox();
		content.add(upButton);
		content.add(downButton);
		add(content, BorderLayout.CENTER);
		upButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireRelativeChangeEvent(RelativeChangeEvent.INCREASED);
			}
		});
		downButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireRelativeChangeEvent(RelativeChangeEvent.DECREASED);
			}
		});
		toolkit.initSpinner(this);
	}

   /**
    * Dispatches correctly the enable property.
    * 
    * @param enable if the value can be changed
    */
   public void setEnabled(boolean enable)
   {
   	  super.setEnabled(enable)  ;
   	  downButton.setEnabled(enable);
   	  upButton.setEnabled(enable);
   }

   /**
    * Add a listener to receive relative change events when the value changes.
    * 
    * @param l the listener to receive events
    */
   public void addRelativeChangeListener(RelativeChangeListener l)
   {
      relativeChangeListeners.add(l);
   }

   /**
    * Removes an relative change listener.
    * 
    * @param l the listener being removed	
    */
   public void removeItemListener(RelativeChangeListener l)
   {
      relativeChangeListeners.remove(l);
   }
	
	/**
	 * Informs listeners that the spinner changed.
	 * 
	 * @param direction either RelativeChangeEvent.INCREASED or RelativeChangeEvent.DECREASED
	 */ 
	public void fireRelativeChangeEvent(int direction)
	{
	       RelativeChangeEvent event=new RelativeChangeEvent(this, direction);
	   	   Iterator i=relativeChangeListeners.iterator();
	   	   while (i.hasNext())
	   	   {
	   	   	  RelativeChangeListener listener=(RelativeChangeListener)i.next();
	   	   	  listener.relativeChange(event);
	   	   }
	}
}