package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.toolkit.Box;
import info.daviot.gui.toolkit.Panel;
import info.daviot.gui.toolkit.event.RelativeChangeEvent;
import info.daviot.gui.toolkit.event.RelativeChangeListener;

import java.awt.BorderLayout;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;




/**
 * Panel to allow the user to select a date
 * (year, month, day).
 * 
 * @author MDA
 * @version NP
 */
public class DateEditorPanel extends Panel implements ItemSelectable
{
    private Collection adjustmentListeners = new LinkedList();
	private Calendar selectedDate;
	private Calendar oldValue;
	protected YearChooser yearChooser;
	protected MonthChooser monthChooser;
	protected DayChooser dayChooser;
	
	public DateEditorPanel()
	{
		super(new BorderLayout());
		init();		
	}
	
	private void init()
	{
		yearChooser=new YearChooser();
		monthChooser=new MonthChooser();
		dayChooser=new DayChooser();
		setSelectedDate(Calendar.getInstance());
		javax.swing.Box topBox=Box.createHorizontalBox();
		topBox.add(monthChooser);
		topBox.add(Box.createHorizontalGlue());
		topBox.add(yearChooser);
		javax.swing.Box bottomBox=Box.createHorizontalBox();
		add(topBox, BorderLayout.NORTH);
		add(dayChooser, BorderLayout.CENTER);
		initListeners();
	}
	
	//Transmits ItemEvent to own listeners
	//updates selected date
	private void initListeners()
	{
		yearChooser.addItemListener(new ItemListener()
		{
	         public void itemStateChanged(ItemEvent e)
	         {
	         	if (e.getStateChange() == ItemEvent.SELECTED)
	         	{
	         		int year=Integer.parseInt(e.getItem().toString());
	         		selectedDate.set(Calendar.YEAR, year);
	         		dayChooser.setYear(year);
	         		updateUI();
	         		fireSelectedChanged();
	         	}
	         }
		});
		monthChooser.addItemListener(new ItemListener()
		{
	         public void itemStateChanged(ItemEvent e)
	         {
	         	if (e.getStateChange() == ItemEvent.SELECTED)
	         	{
	         		int month=Integer.parseInt(e.getItem().toString());
	         		selectedDate.set(Calendar.MONTH, month);
	         		dayChooser.setMonth(month);
	         		updateUI();
	         		fireSelectedChanged();
	         	}
	         }
		});
		//rolls year if month rolls over 12
		monthChooser.addRelativeChangeListener(new RelativeChangeListener()
        {
          public void relativeChange(RelativeChangeEvent e)
          {
            switch (e.getChangeDirection())
            {
               case RelativeChangeEvent.INCREASED :
                  if (monthChooser.getComboBox().getSelectedIndex()==0)
                  {
                  	 yearChooser.chooseNextItem();
                  }
                  break;
               case RelativeChangeEvent.DECREASED :
                  if (monthChooser.getComboBox().getSelectedIndex()==monthChooser.getComboBox().getItemCount()-1)
                  {
                  	 yearChooser.choosePreviousItem();
                  }
                  break;
               default :
            }
          }
        });				

		dayChooser.addItemListener(new ItemListener()
		{
	         public void itemStateChanged(ItemEvent e)
	         {
	         	if (e.getStateChange() == ItemEvent.SELECTED)
	         	{
	         		selectedDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(e.getItem().toString()));
	         		fireSelectedChanged();
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
      yearChooser.setValue(selectedDate.get(Calendar.YEAR));
      monthChooser.setMonth(selectedDate.get(Calendar.MONTH));
      dayChooser.setValue(selectedDate.get(Calendar.DAY_OF_MONTH));
   }

   /**
    * Changes the currently selected day of month.
    */
   public void setNextDay()
   {
      dayChooser.setNextDay();
   }
   
   /**
    * Changes the currently selected day of month.
    */
   public void setPreviousDay()
   {
      dayChooser.setPreviousDay();
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
		JFrame frame = new JFrame("DateEditorPanel");
		DateEditorPanel chooser=new DateEditorPanel();
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

