package info.daviot.gui.toolkit.calendar;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

/**
 * Allows the user to choose a year in a SpinnerComboBox.
 * 
 * @author MDA
 * @version NP
 */
public class YearChooser extends IntegerChooser
{
   public static final int MIN_YEAR_DEFAULT=1900;
   public static final int MAX_YEAR_DEFAULT=2100;

   /**
     * Constructs the YearChooser with the spinner on the right.
     */
   public YearChooser()
   {
      this(SwingConstants.RIGHT, MIN_YEAR_DEFAULT, MAX_YEAR_DEFAULT);
   }

   /**
    * Constructs the YearChooser.
    * 
    * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
    * @param minYear the first possible value to initialize the combo box
    * @param maxYear the last possible value to initialize the combo box
    * @throws IllegalArgumentException if another value is used for spinnerPosition
    * or if maxYear is lower than minYear
    */
   public YearChooser(int spinnerPosition, int minYear, int maxYear)
   {
      super(spinnerPosition, getIntegerRange(minYear, maxYear), new DecimalFormat("0000"));
      Calendar now=Calendar.getInstance();
      Integer thisYear=new Integer(now.get(Calendar.YEAR));
      getComboBox().setSelectedItem(thisYear);
   }
   
	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("YearChooser");
		YearChooser chooser=new YearChooser(SwingConstants.LEFT, 1980, 2010);
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