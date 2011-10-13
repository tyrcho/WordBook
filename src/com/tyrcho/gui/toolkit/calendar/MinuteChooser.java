package com.tyrcho.gui.toolkit.calendar;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

/**
 * Allows the user to choose an minute in a SpinnerComboBox.
 * 
 * @author MDA
 * @version NP
 */
public class MinuteChooser extends IntegerChooser
{
   public static final int MIN_MINUTE_DEFAULT=0;
   public static final int MAX_MINUTE_DEFAULT=59;

   /**
     * Constructs the MinuteChooser with the spinner on the right.
     */
   public MinuteChooser()
   {
      this(SwingConstants.RIGHT, MIN_MINUTE_DEFAULT, MAX_MINUTE_DEFAULT);
   }

   /**
    * Constructs the MinuteChooser.
    * 
    * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
    * @param minMinute the first possible value to initialize the combo box
    * @param maxMinute the last possible value to initialize the combo box
    * @throws IllegalArgumentException if another value is used for spinnerPosition
    * or if maxMinute is lower than minMinute
    */
   public MinuteChooser(int spinnerPosition, int minMinute, int maxMinute)
   {
      this(spinnerPosition, getIntegerRange(minMinute, maxMinute));
   }
   
   /**
    * Constructs the MinuteChooser.
    * 
    * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
    * @param defaultData the array to initialize the combo box
    * @throws IllegalArgumentException if another value is used for spinnerPosition
    */
   public MinuteChooser(int spinnerPosition, Integer[] defaultData)
   {
      super(spinnerPosition, defaultData, new DecimalFormat("00"));
   }
  
	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("MinuteChooser");
		MinuteChooser chooser=new MinuteChooser();
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