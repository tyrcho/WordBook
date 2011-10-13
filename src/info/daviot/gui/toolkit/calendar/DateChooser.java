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
public class DateChooser extends DateCommonChooser 
{
    /**
     * Constructs the DateChooser
     * with the spinner on the right.
     * 
     * @param format used to validate or display the date
     */
	public DateChooser(DateFormat format)
	{
		this(format, SwingConstants.RIGHT);		
	}	
	
    /**
     * Constructs the DateChooser.
     * 
     * @param format used to validate or display the date
     * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
     * @throws IllegalArgumentException if another value is used for spinnerPosition
     */
	public DateChooser(DateFormat format, int spinnerPosition)
	{
		super(spinnerPosition);
		datePopup=new DatePopup(format);
		add((Component)datePopup, BorderLayout.CENTER); 
		spinner.setPreferredSize(new Dimension(
				spinner.getPreferredSize().width,
				((PopupEditor)datePopup).getPreferredSize().height));
	}
	
	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("DateChooser");
		DateChooser chooser=new DateChooser(new SimpleDateFormat("dd/MM/yyyy HH:mm"));
		frame.getContentPane().add(chooser);
		frame.pack();
		frame.setVisible(true);
	}


}

