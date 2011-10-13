package com.tyrcho.gui.toolkit.calendar;

import java.awt.BorderLayout;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.tyrcho.gui.toolkit.Panel;
import com.tyrcho.gui.toolkit.event.RelativeChangeEvent;
import com.tyrcho.gui.toolkit.event.RelativeChangeListener;


/**
 * Panel to allow the user to select a date and time
 * (year, month, day, hour, minute).
 * 
 * @author MDA
 * @version NP
 */
public class DateTimeEditorPanel extends Panel implements ItemSelectable
{
    private Collection adjustmentListeners = new LinkedList();
	private Calendar selectedDate;
	private Calendar oldValue;
	private DateEditorPanel dateEditorPanel;
	private TimeEditorPanel timeEditorPanel;
	
    /**
     * Construct the date and time editor panel with the specified possible values.
     * 
     * @param hourValues possible values for hours initially in the combo box
     * @param minuteValues possible values for minutes initially in the combo box
     */
	public DateTimeEditorPanel(Integer[] hourValues, Integer[] minuteValues)
	{
		super(new BorderLayout());
		dateEditorPanel=new DateEditorPanel();
		timeEditorPanel=new TimeEditorPanel(hourValues, minuteValues);
		init();		
	}
	
	private void init()
	{
		setSelectedDate(Calendar.getInstance());
		add(dateEditorPanel, BorderLayout.CENTER);
		add(timeEditorPanel, BorderLayout.SOUTH);
		initListeners();
	}
	
	//Transmits ItemEvent to own listeners
	//updates selected date
	private void initListeners()
	{
		dateEditorPanel.addItemListener(new ItemListener()
		{
	         public void itemStateChanged(ItemEvent e)
	         {
	         	if (e.getStateChange() == ItemEvent.SELECTED)
	         	{
	         		Calendar date=(Calendar)e.getItem();
	         		selectedDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
	         		selectedDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
	         		selectedDate.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
	         		fireSelectedChanged();
	         	}
	         }
		});
		timeEditorPanel.addItemListener(new ItemListener()
		{
	         public void itemStateChanged(ItemEvent e)
	         {
	         	if (e.getStateChange() == ItemEvent.SELECTED)
	         	{
	         		Calendar date=(Calendar)e.getItem();
	         		selectedDate.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
	         		selectedDate.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
	         		fireSelectedChanged();
	         	}
	         }
		});
		//rolls days if hours gets over one day
		timeEditorPanel.hourChooser.addRelativeChangeListener(new RelativeChangeListener()
        {
          public void relativeChange(RelativeChangeEvent e)
          {
            switch (e.getChangeDirection())
            {
               case RelativeChangeEvent.INCREASED :
                  if (timeEditorPanel.hourChooser.getComboBox().getSelectedIndex()==0)
                  {
                  	dateEditorPanel.setNextDay();
                  }
                  break;
               case RelativeChangeEvent.DECREASED :
                  if (timeEditorPanel.hourChooser.getComboBox().getSelectedIndex()==timeEditorPanel.hourChooser.getComboBox().getItemCount()-1)
                  {
                  	dateEditorPanel.setPreviousDay();
                  }
                  break;
               default :
            }
          }
        });				
	}
	
	/**
	 * Gets the selectedDate
	 * @return Returns a Calendar
	 */
	public Calendar getSelectedDate() {
		return selectedDate;
	}
	
   /**
    * Sets the selectedDate
    * @param selectedDate The selectedDate to set
    */
   public void setSelectedDate(Calendar selectedDate)
   {
      this.selectedDate = selectedDate;
      dateEditorPanel.setSelectedDate(selectedDate);
      timeEditorPanel.setSelectedDate(selectedDate);
   }

   private void fireSelectedChanged()
   {   	 
         if (selectedDate.equals(oldValue))
         {
         	return;
         }
         oldValue=(Calendar)selectedDate.clone();
         ItemEvent event =
            new ItemEvent(
               this,
               ItemEvent.ITEM_STATE_CHANGED,
               selectedDate,
               ItemEvent.SELECTED);
         Iterator i = adjustmentListeners.iterator();
         while (i.hasNext())
         {
            ItemListener listener = (ItemListener) i.next();
            listener.itemStateChanged(event);
         }
      
   }

   /**
    * Add a listener to receive item events when the value of the 
    * selected item changes.
    * The events thrown contain the value 
    * in the form of an Integer.
    * 
    * @param l the listener to receive events
    */
   public void addItemListener(ItemListener l)
   {
      adjustmentListeners.add(l);
   }

   /**
    * Removes an item listener.
    * 
    * @param l the listener being removed	
    */
   public void removeItemListener(ItemListener l)
   {
      adjustmentListeners.remove(l);
   }

   /**
    * Returns the selected date (Calendar).
    */
   public Object[] getSelectedObjects()
   {
      Object[] objects={ selectedDate };
      return objects;
   }

	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("DateTimeEditorPanel");
		Integer[] hourValues=IntegerChooser.getIntegerRange(8,20);
		Integer[] minuteValues={new Integer(0), new Integer(15), new Integer(30), new Integer(45)};
		DateTimeEditorPanel chooser=new DateTimeEditorPanel(hourValues, minuteValues);
		chooser.addItemListener(new ItemListener()
		{
           public void itemStateChanged(ItemEvent e)
           {
           	  System.out.println(e.getItem());
           }
		});
		frame.getContentPane().add(chooser);
		frame.pack();
		frame.setVisible(true);
	}

}

