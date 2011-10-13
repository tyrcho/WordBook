package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.toolkit.Box;
import info.daviot.gui.toolkit.Label;
import info.daviot.gui.toolkit.Panel;
import info.daviot.gui.toolkit.TextButton;
import info.daviot.util.language.Translator;
import info.daviot.util.language.TranslatorFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;


/**
 * Allows the user to choose a day from a day panel.
 * 
 * @author MDA
 * @version NP
 */
public class DayChooser 
extends Panel implements ItemSelectable
{
   private Calendar currentCalendar = Calendar.getInstance();
   private Collection dayAdjustmentListeners = new LinkedList();
   private Translator translator = TranslatorFactory.getTranslator();
   private Panel daysGrid;
   private Component[] days=new Component[31];
   private static final Dimension daySize=new Dimension(30, 20);
   private ActionListener daysActionListener;
   private int oldDay=-1;

   public DayChooser()
   {
      super(new BorderLayout());
      init();
   }
   
   private Component constructDayLabels()
   {
   	  javax.swing.Box labels=Box.createHorizontalBox();
   	  for (int i=0; i<7; i++)
   	  {
   	  	 if (i>0)
   	  	 {
	   	  	 labels.add(Box.createHorizontalGlue());
   	  	 }
   	  	 labels.add(new Label(translator.getString("Day"+i, "Components")));   	  	 
   	  }
   	  return labels;
   }
   
   private void init()   
   {
   	  daysActionListener=new ActionListener()
   	  {
   	  	 public void actionPerformed(ActionEvent e)
   	  	 {   	  	 	
   	  	 	int day=Integer.parseInt(e.getActionCommand());
   	  	 	setValue(day);
   	  	 }
   	  };
   	  for (int i=0; i<31; i++)
   	  {
   	  	 days[i]=constructDay(i);
   	  }
   	  add(constructDayLabels(), BorderLayout.NORTH);
   	  updateDaysGrid();
   	  add(daysGrid, BorderLayout.CENTER);
   }
   
   private void updateDaysGrid()
   {
   	  if (daysGrid==null)
   	  {
   	  	 daysGrid=new Panel(new BorderLayout());
   	  }
   	  else
   	  {
   	  	 daysGrid.removeAll();
   	  }
   	  javax.swing.Box allLines=Box.createVerticalBox();
   	  javax.swing.Box line=Box.createHorizontalBox();
   	  Calendar calendar=(Calendar)currentCalendar.clone();
   	  calendar.set(Calendar.DAY_OF_MONTH, 1);
   	  //empty labels at start
   	  int day=Calendar.MONDAY;
   	  while (day%7 != calendar.get(Calendar.DAY_OF_WEEK)%7)
   	  {
   	  	 if (day%7 != Calendar.MONDAY%7)
   	  	 {
	   	  	 line.add(Box.createHorizontalGlue());
   	  	 }
   	  	 line.add(getEmptyLabel());
   	  	 day=day+1;   	  	 
   	  }
   	  //fill days
   	  boolean isFinished=false;
   	  while (!isFinished)
   	  {
   	  	 if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
   	  	 {
	   	  	 line.add(Box.createHorizontalGlue());
   	  	 }
   	  	 line.add(days[calendar.get(Calendar.DAY_OF_MONTH)-1]);
   	  	 if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
   	  	 {
   	  	 	allLines.add(line);   	  	 	
   	  	 	line=Box.createHorizontalBox();
   	  	 }
   	  	 isFinished=calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
   	  	 if (!isFinished)
   	  	 {
	   	  	 calendar.add(Calendar.DAY_OF_MONTH, 1);   	  	 
   	  	 }
   	  }
   	  //empy labels at the end
   	  day=calendar.get(Calendar.DAY_OF_WEEK);
   	  while (day%7 != Calendar.SUNDAY%7)
   	  {
   	  	 if (day%7 != Calendar.MONDAY%7)
   	  	 {
	   	  	 line.add(Box.createHorizontalGlue());
   	  	 }
   	  	 line.add(getEmptyLabel());
   	  	 day=day+1;   	  	 
   	  }
   	  allLines.add(line);   
   	  daysGrid.add(allLines, BorderLayout.CENTER);
   	  updateUI();
   }
   
   /**
    * Changes the currently selected day of month.
    * 
    * @param day the new day
    */
   public void setValue(int day)
   {
   	  	currentCalendar.set(Calendar.DAY_OF_MONTH, day);
   	  	fireDaySelectedChanged();
   }
   
   /**
    * Changes the currently selected day of month.
    */
   public void setNextDay()
   {
   	  	currentCalendar.roll(Calendar.DAY_OF_MONTH, 1);
   	  	fireDaySelectedChanged();
   }
   
   /**
    * Changes the currently selected day of month.
    */
   public void setPreviousDay()
   {
   	  	currentCalendar.roll(Calendar.DAY_OF_MONTH, -1);
   	  	fireDaySelectedChanged();
   }
   
   private static Component getEmptyLabel()
   {
   	  Label empty=new Label("");
   	  empty.setPreferredSize(daySize);
   	  empty.setMaximumSize(daySize);

//   	  empty.setForeground(empty.getBackground());
   	  return empty;
   }
   
   private Component constructDay(int index)
   {
   	  int day=index+1;
   	  String label=day<10 ? " "+day : ""+day;
      // Creates a button that doesn't react on clicks or focus changes
      TextButton button=new TextButton(label)
   	  {
//			public void addMouseListener(MouseListener l) { }
	
			public boolean isFocusTraversable() 
			{
				return false;
			}
   	  };
   	  button.setActionCommand(""+day);
   	  button.addActionListener(daysActionListener);
   	  button.setPreferredSize(daySize);
   	  button.setMaximumSize(daySize);
   	  button.setHorizontalAlignment(Label.TRAILING);
//   	  empty.setForeground(empty.getBackground());
   	  return button;
   }
   
   
   /**
    * Returns the selected days (only one day)
    * in the form of a Calendar
    */
   public Object[] getSelectedObjects()
   {
      Object[] objects={ currentCalendar };
      return objects;
   }
   
   /**
    * Changes the year.
    * 
    * @param year the new year
    */
   public void setYear(int year)
   {
   	  currentCalendar.set(Calendar.YEAR, year);
   	  updateDaysGrid();
   }

   /**
    * Changes the month.
    * 
    * @param month the new month
    */
   public void setMonth(int month)
   {
   	  currentCalendar.set(Calendar.MONTH, month);
   	  updateDaysGrid();
   }

   /**
    * Add a listener to receive item events when the value of the 
    * selected day changes.
    * The events thrown contain the value 
    * in the form of an Integer (Calendar.MONTH)
    * 
    * @param l the listener to receive events
    */
   public void addItemListener(ItemListener l)
   {
      dayAdjustmentListeners.add(l);
   }

   /**
    * Removes an item listener.
    * 
    * @param l the listener being removed	
    */
   public void removeItemListener(ItemListener l)
   {
      dayAdjustmentListeners.remove(l);
   }

   private void fireDaySelectedChanged()
   {   	  
   	  Integer day=new Integer(currentCalendar.get(Calendar.DAY_OF_MONTH));
   	  if (oldDay==day.intValue())
   	  {
   	  	 return;
   	  }
   	  if (oldDay>0)
   	  {
	   	  days[oldDay-1].setEnabled(true);
   	  }
   	  days[day.intValue()-1].setEnabled(false);
   	  oldDay=day.intValue();
   	  
      ItemEvent event =
         new ItemEvent(
            this,
            ItemEvent.ITEM_STATE_CHANGED,
            day,
            ItemEvent.SELECTED);
      Iterator i = dayAdjustmentListeners.iterator();
      while (i.hasNext())
      {
         ItemListener listener = (ItemListener) i.next();
         listener.itemStateChanged(event);
      }
   }

	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("DayChooser");
		DayChooser chooser=new DayChooser();
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