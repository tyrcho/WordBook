package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.toolkit.ComboBox;

import java.awt.Component;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxEditor;


/**
 * Allows the user to choose an integer in a SpinnerComboBox.
 * 
 * @author MDA
 * @version NP
 */
public class IntegerChooser extends SpinnerComboBox implements ItemSelectable
{
   private Collection adjustmentListeners = new LinkedList();
   Integer oldValue;
   private NumberFormat format;

   /**
     * Constructs the IntegerChooser with the spinner on the right.
     * 
     * @param defaultData the array to initialize the combo box
     * @param format the format used to display the numbers (but they are not validated)
     */
   public IntegerChooser(Integer[] defaultData, NumberFormat format)
   {
      this(SwingConstants.RIGHT, defaultData, format);
   }

   /**
    * Constructs the IntegerChooser.
    * 
    * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
    * @param defaultData the array to initialize the combo box
     * @param format the format used to display the numbers (but they are not validated)
    * @throws IllegalArgumentException if another value is used for spinnerPosition
    */
   public IntegerChooser(int spinnerPosition, Integer[] defaultData, NumberFormat format)
   {
      super(new ComboBox(defaultData), spinnerPosition);
      this.format=format;
      getComboBox().setEditable(true);
      initFormatRenderer();
      initListeners();
   }
   
   public void initFormatRenderer()
   {
   	  getComboBox().setRenderer(new DefaultListCellRenderer()
   	  {
   	  	 public Component getListCellRendererComponent(JList list,
              Object value,
              int index,
              boolean isSelected,
              boolean cellHasFocus)
         {
         	 if (value instanceof Integer)
         	 {
         	 	String formattedInteger=format.format((Integer)value);
         	 	return super.getListCellRendererComponent(list, formattedInteger, index, isSelected, cellHasFocus);
         	 }
         	 else
         	 {
         	    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         	 }
         }
   	  });
   	  getComboBox().setEditor(new BasicComboBoxEditor()
   	  {
   	  	 public void setItem(Object item)
   	  	 {
         	 if (item instanceof Integer)
         	 {
         	 	String formattedInteger=format.format((Integer)item);
         	 	super.setItem(formattedInteger);
         	 }
         	 else
         	 {
         	    super.setItem(item);
         	 }   	  	 	
   	  	 }
   	  });
   }
   
   
   /**
    * Gets an array of Integer in range min, max (inclusive).
    * 
    * @return all values between min and max
    */
   public static Integer[] getIntegerRange(int min, int max)
   {
   	  if (min>max)
   	  {
   	  	 throw new IllegalArgumentException("The values are not in order : "+min+", " +max);
   	  }
   	  Integer[] values=new Integer[max-min+1];
   	  for (int i=min; i<=max; i++)
   	  {
   	  	 values[i-min]=new Integer(i);
   	  }
   	  return values;
   }

   /**
    * Sets the value in the combo box.
    * 
    * @param value the new value
    */
   public void setValue(int value)
   {
   	  Integer newValue=new Integer(value);
   	  getComboBox().setSelectedItem(newValue);
   }
   
   /**
    * Gets the selected integer.
    * 
    * @return the selected value as an int
    */
   public int getValue()
   {
   	  return Integer.parseInt(getComboBox().getSelectedItem().toString());
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
    * Returns the selected Integer.
    */
   public Object[] getSelectedObjects()
   {
      Integer selected = (Integer) getComboBox().getSelectedItem();
      Object[] objects={ selected };
      return objects;
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
   
   //sends item events with the Integer value
   private void initListeners()
   {      
      getComboBox().addItemListener(new ItemListener()
      {
         public void itemStateChanged(ItemEvent e)
         {
            if (e.getStateChange() == ItemEvent.SELECTED)
	        {
               Object item=e.getItem();
               fireSelectedChanged(item);
	        }
         }
      });
      getComboBox().addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
         	Object item=getComboBox().getSelectedItem();
            fireSelectedChanged(item);
         }
      });
      getComboBox().getEditor().getEditorComponent().addFocusListener(new FocusAdapter()
	  {
         public void focusLost(FocusEvent event)  
   	  	 {
   	  	 	try
   	  	 	{
   	  	 		JTextField source=(JTextField)event.getSource();
   	  	 		int value=Integer.parseInt(source.getText());
   	  	 		setValue(value);
   	  	 	}
   	  	 	catch (NumberFormatException e)
   	  	 	{
   	  	 	}
   	  	 }
	  	});

   }

   private void fireSelectedChanged(Object item)
   {
   	  try
   	  {
	         Integer selected= new Integer(item.toString());
	         if (selected.equals(oldValue))
	         {
	         	return;
	         }
	         oldValue=selected;
	         ItemEvent event =
	            new ItemEvent(
	               this,
	               ItemEvent.ITEM_STATE_CHANGED,
	               selected,
	               ItemEvent.SELECTED);
	         Iterator i = adjustmentListeners.iterator();
	         while (i.hasNext())
	         {
	            ItemListener listener = (ItemListener) i.next();
	            listener.itemStateChanged(event);
	         }
	      
   	  }
   	  catch (NumberFormatException ex)
   	  {
   	  	/*
   	  	 if (oldValue!=null)
   	  	 {
   	  	 	getComboBox().setSelectedItem(oldValue);
   	  	 }
   	  	 */   	  	 
   	  }
   }
  
	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("IntegerChooser");
		Integer[] data={new Integer(1), new Integer(2), new Integer(3)};
		IntegerChooser chooser=new IntegerChooser(SwingConstants.LEFT, data, new java.text.DecimalFormat("000"));
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