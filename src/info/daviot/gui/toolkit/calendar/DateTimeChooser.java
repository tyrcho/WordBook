package info.daviot.gui.toolkit.calendar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.SwingConstants;



/**
 * Allows to choose the date by combining a date popup with a spinner.
 * 
 * @author MDA
 * @version NP
 */
public class DateTimeChooser extends DateCommonChooser 
{
	
    /**
     * Constructs the DateTimeChooser
     * with the spinner on the right.
     * 
     * @param format used to validate or display the date
     * @param hourValues possible values for hours initially in the combo box
     * @param minuteValues possible values for minutes initially in the combo box
     */
	public DateTimeChooser(DateFormat format,
		Integer[] hourValues, 
		Integer[] minuteValues)
	{
		this(format, SwingConstants.RIGHT, hourValues, minuteValues);		
	}
	
    /**
     * Constructs the DateTimeChooser.
     * 
     * @param format used to validate or display the date
     * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
     * @param hourValues possible values for hours initially in the combo box
     * @param minuteValues possible values for minutes initially in the combo box
     * @throws IllegalArgumentException if another value is used for spinnerPosition
     */
	public DateTimeChooser(DateFormat format, 
		int spinnerPosition, 
		Integer[] hourValues, 
		Integer[] minuteValues)
	{
		super(spinnerPosition);
		datePopup=new DateTimePopup(format, hourValues, minuteValues);
		add((Component)datePopup, BorderLayout.CENTER); 
		spinner.setPreferredSize(new Dimension(
				spinner.getPreferredSize().width,
				((PopupEditor)datePopup).getPreferredSize().height));
	}
	
	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("DateTimeChooser");
		Integer[] hourValues=IntegerChooser.getIntegerRange(8,20);
		Integer[] minuteValues={new Integer(0), new Integer(15), new Integer(30), new Integer(45)};
		DateTimeChooser chooser=new DateTimeChooser(new SimpleDateFormat("dd/MM/yyyy HH:mm"), hourValues, minuteValues);
		frame.getContentPane().add(chooser);
		frame.pack();
		frame.setVisible(true);
	}


}

