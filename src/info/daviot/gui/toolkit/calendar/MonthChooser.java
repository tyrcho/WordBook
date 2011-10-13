package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.toolkit.ComboBox;
import info.daviot.util.language.Translator;
import info.daviot.util.language.TranslatorFactory;

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
 * Allows the user to choose a month in a SpinnerComboBox.
 * 
 * @author MDA
 * @version NP
 */
public class MonthChooser extends SpinnerComboBox implements ItemSelectable
{
   private Collection monthAdjustmentListeners = new LinkedList();
   private Translator translator = TranslatorFactory.getTranslator();

   /**
     * Constructs the MonthChooser with the spinner on the right.
     */
   public MonthChooser()
   {
      this(SwingConstants.RIGHT);
   }

   /**
    * Constructs the MonthChooser.
    * 
    * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
    * @throws IllegalArgumentException if another value is used for spinnerPosition
    */
   public MonthChooser(int spinnerPosition)
   {
      super(new ComboBox(), spinnerPosition);
      initComboBoxData();
      initListeners();
   }

   /**
    * Add a listener to receive item events when the value of the 
    * selected month changes.
    * The events thrown contain the value 
     * in the form of an Integer (Calendar.MONTH)
    * 
    * @param l the listener to receive events
    */
   public void addItemListener(ItemListener l)
   {
      monthAdjustmentListeners.add(l);
   }

   /**
    * Returns the selected months (only one month normally)
    * in the form of an Integer (Calendar.MONTH)
    */
   public Object[] getSelectedObjects()
   {
      MonthModel selectedMonth = (MonthModel) getComboBox().getSelectedItem();
      Object[] objects={ new Integer(selectedMonth.getCalendarValue()) };
      return objects;
   }

   /**
    * Removes an item listener.
    * 
    * @param l the listener being removed	
    */
   public void removeItemListener(ItemListener l)
   {
      monthAdjustmentListeners.remove(l);
   }

   private void initListeners()
   {
      //sends item events with the Integer value
      getComboBox().addItemListener(new ItemListener()
      {
         public void itemStateChanged(ItemEvent e)
         {
		      if (e.getStateChange() == ItemEvent.SELECTED)
		      {
		            fireMonthSelectedChanged();
		      }
         }
      });
   }
   
   /**
    * Changes the currently selected month.
    * 
    * @param month the new month as a Calendar.MONTH
    */
   public void setMonth(int month)
   {
   	  getComboBox().setSelectedItem(new MonthModel(month));   	  
   }
   
   /**
    * Gets the selected month.
    * 
    * @return the month as a Calendar.MONTH
    */
   public int getMonth()
   {
   	  MonthModel month=(MonthModel) getComboBox().getSelectedItem();
   	  return month.getCalendarValue();
   }

   private void fireMonthSelectedChanged()
   {
         MonthModel monthSelected = (MonthModel) getComboBox().getSelectedItem();
         Integer calendarMonth = new Integer(monthSelected.getCalendarValue());
         ItemEvent event =
            new ItemEvent(
               this,
               ItemEvent.ITEM_STATE_CHANGED,
               calendarMonth,
               ItemEvent.SELECTED);
         Iterator i = monthAdjustmentListeners.iterator();
         while (i.hasNext())
         {
            ItemListener listener = (ItemListener) i.next();
            listener.itemStateChanged(event);
         }
      
   }

   private void initComboBoxData()
   {
      Calendar calendar = Calendar.getInstance();
      for (int i = calendar.getMinimum(Calendar.MONTH);
         i <= calendar.getMaximum(Calendar.MONTH);
         i++)
      {
      	 MonthModel model=new MonthModel(i);
         getComboBox().addItem(model);
         if (i==calendar.get(Calendar.MONTH))
         {
         	getComboBox().setSelectedItem(model);
         }
      }
   }

   private class MonthModel
   {
      private String name;
      private int calendarValue;

      public MonthModel(int calendarValue)
      {
         setCalendarValue(calendarValue);
         setName(translator.getString("Month" + calendarValue, "Components"));
      }
      
      public String toString()
      {
      	 return name;
      }
      
      public boolean equals(Object test)
      {
      	 if (test instanceof MonthModel)
      	 {
      	 	 return ((MonthModel)test).calendarValue==calendarValue;
      	 }
      	 return false;
      }

      /**
      * Gets the calendarValue
      * @return Returns a int
      */
      public int getCalendarValue()
      {
         return calendarValue;
      }
      /**
       * Sets the calendarValue
       * @param calendarValue The calendarValue to set
       */
      public void setCalendarValue(int calendarValue)
      {
         this.calendarValue = calendarValue;
      }

      /**
       * Gets the name
       * @return Returns a String
       */
      public String getName()
      {
         return name;
      }
      /**
       * Sets the name
       * @param name The name to set
       */
      public void setName(String name)
      {
         this.name = name;
      }
   }
   
	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("MonthChooser");
		MonthChooser chooser=new MonthChooser();
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