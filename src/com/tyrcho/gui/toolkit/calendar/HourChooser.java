package com.tyrcho.gui.toolkit.calendar;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

/**
 * Allows the user to choose an hour in a SpinnerComboBox.
 * 
 * @author MDA
 * @version NP
 */
public class HourChooser extends IntegerChooser
{
   public static final int MIN_HOUR_DEFAULT=0;
   public static final int MAX_HOUR_DEFAULT=23;

   /**
     * Constructs the HourChooser with the spinner on the right.
     */
   public HourChooser()
   {
      this(SwingConstants.RIGHT, MIN_HOUR_DEFAULT, MAX_HOUR_DEFAULT);
   }

   /**
    * Constructs the HourChooser.
    * 
    * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
    * @param minHour the first possible value to initialize the combo box
    * @param maxHour the last possible value to initialize the combo box
    * @throws IllegalArgumentException if another value is used for spinnerPosition
    * or if maxHour is lower than minHour
    */
   public HourChooser(int spinnerPosition, int minHour, int maxHour)
   {
      this(spinnerPosition, getIntegerRange(minHour, maxHour));
   }
   
   /**
    * Constructs the HourChooser.
    * 
    * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
    * @param defaultData the array to initialize the combo box
    * @throws IllegalArgumentException if another value is used for spinnerPosition
    */
   public HourChooser(int spinnerPosition, Integer[] defaultData)
   {
      super(spinnerPosition, defaultData, new DecimalFormat("00"));
   }
     
	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("HourChooser");
		HourChooser chooser=new HourChooser();
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