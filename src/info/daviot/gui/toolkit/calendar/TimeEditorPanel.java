package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.toolkit.Box;
import info.daviot.gui.toolkit.Label;
import info.daviot.gui.toolkit.Panel;
import info.daviot.gui.toolkit.event.RelativeChangeEvent;
import info.daviot.gui.toolkit.event.RelativeChangeListener;
import info.daviot.util.language.Translator;
import info.daviot.util.language.TranslatorFactory;

import java.awt.BorderLayout;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.SwingConstants;


/**
 * Panel to allow the user to select a time
 * (hour, minute).
 * 
 * @author MDA
 * @version NP
 */
public class TimeEditorPanel extends Panel implements ItemSelectable
{
	protected HourChooser hourChooser;
	protected MinuteChooser minuteChooser;
    private Translator translator = TranslatorFactory.getTranslator();
    private Collection adjustmentListeners = new LinkedList();
	private Calendar selectedDate;
	private Calendar oldValue;
    
    /**
     * Construct the time editor panel with the specified possible values.
     * 
     * @param hourValues possible values for hours initially in the combo box
     * @param minuteValues possible values for minutes initially in the combo box
     */
	public TimeEditorPanel(Integer[] hourValues, Integer[] minuteValues)
	{
		super(new BorderLayout());
		hourChooser=new HourChooser(SwingConstants.RIGHT, hourValues);
		minuteChooser=new MinuteChooser(SwingConstants.RIGHT, minuteValues);
		init();		
	}
	
	private void init()
	{
		setSelectedDate(Calendar.getInstance());
		javax.swing.Box bottomBox=Box.createHorizontalBox();
		bottomBox.add(hourChooser);
		bottomBox.add(Box.createHorizontalStrut(5));
		bottomBox.add(new Label(translator.getString("hours", "Components")));
		bottomBox.add(Box.createHorizontalStrut(10));
		bottomBox.add(minuteChooser);		
		bottomBox.add(Box.createHorizontalStrut(5));
		bottomBox.add(new Label(translator.getString("minutes", "Components")));
		add(bottomBox, BorderLayout.CENTER);
		initListeners();
	}
	
	//Transmits ItemEvent to own listeners
	//updates selected date
	private void initListeners()
	{
		hourChooser.addItemListener(new ItemListener()
		{
	         public void itemStateChanged(ItemEvent e)
	         {
	         	if (e.getStateChange() == ItemEvent.SELECTED)
	         	{
	         		selectedDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(e.getItem().toString()));
	         		fireSelectedChanged();
	         	}
	         }
		});
		minuteChooser.addItemListener(new ItemListener()
		{
	         public void itemStateChanged(ItemEvent e)
	         {
	         	if (e.getStateChange() == ItemEvent.SELECTED)
	         	{
	         		selectedDate.set(Calendar.MINUTE, Integer.parseInt(e.getItem().toString()));
	         		fireSelectedChanged();
	         	}
	         }
		});
		//rolls hours if minutes gets over one hour
		minuteChooser.addRelativeChangeListener(new RelativeChangeListener()
        {
          public void relativeChange(RelativeChangeEvent e)
          {
            switch (e.getChangeDirection())
            {
               case RelativeChangeEvent.INCREASED :
                  if (minuteChooser.getComboBox().getSelectedIndex()==0)
                  {
                  	hourChooser.chooseNextItem();
                  }
                  break;
               case RelativeChangeEvent.DECREASED :
                  if (minuteChooser.getComboBox().getSelectedIndex()==minuteChooser.getComboBox().getItemCount()-1)
                  {
                  	hourChooser.choosePreviousItem();
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
      hourChooser.setValue(selectedDate.get(Calendar.HOUR_OF_DAY));
      minuteChooser.setValue(selectedDate.get(Calendar.MINUTE));
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
		JFrame frame = new JFrame("TimeEditorPanel");
		Integer[] hourValues=IntegerChooser.getIntegerRange(8,20);
		Integer[] minuteValues={new Integer(0), new Integer(15), new Integer(30), new Integer(45)};
		TimeEditorPanel chooser=new TimeEditorPanel(hourValues, minuteValues);
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


