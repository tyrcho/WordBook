package com.tyrcho.gui.toolkit.calendar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.SwingConstants;

import com.tyrcho.gui.toolkit.ComboBox;
import com.tyrcho.gui.toolkit.Panel;
import com.tyrcho.gui.toolkit.Toolkit;
import com.tyrcho.gui.toolkit.ToolkitFactory;
import com.tyrcho.gui.toolkit.event.RelativeChangeEvent;
import com.tyrcho.gui.toolkit.event.RelativeChangeListener;


/**
 * Represents a combo box associated with a spinner
 * to quickly select the next/previous element from the list.
 * 
 * @author MDA
 * @version NP
 */
public class SpinnerComboBox extends Panel
{
    private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;
    private ComboBox comboBox;
    private Spinner spinner;
    private Collection relativeChangeListeners=new LinkedList();

    /**
     * Constructs the SpinnerComboBox on an existing combo box
     * with the spinner on the right.
     * 
     * @param comboBox the combo box to use
     */
	public SpinnerComboBox(ComboBox comboBox)
	{
		this(comboBox, SwingConstants.RIGHT);		
	}
	
    /**
     * Constructs the SpinnerComboBox on an existing combo box.
     * 
     * @param comboBox the combo box to use
     * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
     * @throws IllegalArgumentException if another value is used for spinnerPosition
     */
	public SpinnerComboBox(ComboBox comboBox, int spinnerPosition)
	{
		super(new BorderLayout());
		setComboBox(comboBox);
		setSpinner(new Spinner());
		spinner.setPreferredSize(new Dimension(
				spinner.getPreferredSize().width,
				comboBox.getPreferredSize().height));
		switch(spinnerPosition)
		{
			case SwingConstants.LEFT :
				add(spinner, BorderLayout.WEST);	
				break;
			case SwingConstants.RIGHT :
				add(spinner, BorderLayout.EAST);	
				break;
			default : 
				throw new IllegalArgumentException("Invalid Spinner position : "+spinnerPosition);
		}
		add(comboBox, BorderLayout.CENTER); 
        getSpinner().addRelativeChangeListener(new RelativeChangeListener()
        {
          public void relativeChange(RelativeChangeEvent e)
          {
            switch (e.getChangeDirection())
            {
               case RelativeChangeEvent.INCREASED :
                  chooseNextItem();
                  break;
               case RelativeChangeEvent.DECREASED :
                  choosePreviousItem();
                  break;
               default :
            }
          }
        });		
        toolkit.initSpinnerComboBox(this);		
	}
	
   /**
    * Dispatches correctly the enable property.
    * 
    * @param enable if the value can be changed
    */
   public void setEnabled(boolean enable)
   {
   	  super.setEnabled(enable)  ;
   	  spinner.setEnabled(enable);
   	  comboBox.setEnabled(enable);
   }

   /**
    * Add a listener to receive relative change events when the selected value changes from 
    * one position.
    * Note that the listeners are not informed if the user selects manually
    * in the combo box.
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
	 * Informs listeners that the selected value changed.
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
	
	public Object getItem()
	{
		return getComboBox().getSelectedItem();
	}
	

   /**
    * Selects the next item in the combo box.
    */
   public void chooseNextItem()
   {
      int currentIndex = getComboBox().getSelectedIndex();
      int maxIndex = getComboBox().getItemCount();
      int newIndex = (currentIndex + 1) % maxIndex;
      getComboBox().setSelectedIndex(newIndex);
      fireRelativeChangeEvent(RelativeChangeEvent.INCREASED);
   }

   /**
    * Selects the previous item in the combo box.
    */
   public void choosePreviousItem()
   {
      int currentIndex = getComboBox().getSelectedIndex();
      int maxIndex = getComboBox().getItemCount();
      int newIndex = (currentIndex - 1 + maxIndex) % maxIndex;
      getComboBox().setSelectedIndex(newIndex);
      fireRelativeChangeEvent(RelativeChangeEvent.DECREASED);
   }
	
	/**
	 * Gets the comboBox
	 * @return Returns a ComboBox
	 */
	public ComboBox getComboBox() {
		return comboBox;
	}
   /**
    * Sets the comboBox
    * @param comboBox The comboBox to set
    */
   public void setComboBox(ComboBox comboBox)
   {
      this.comboBox = comboBox;
   }

	/**
	 * Gets the spinner
	 * @return Returns a Spinner
	 */
	public Spinner getSpinner() {
		return spinner;
	}
   /**
    * Sets the spinner
    * @param spinner The spinner to set
    */
   public void setSpinner(Spinner spinner)
   {
      this.spinner = spinner;
   }

}

