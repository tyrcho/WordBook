package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.field.ITextField;
import info.daviot.gui.toolkit.event.RelativeChangeEvent;
import info.daviot.gui.toolkit.event.RelativeChangeListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.SwingConstants;




/**
 * Parent class for DateCommonChooser and DateTimeChooser.
 * 
 * @author MDA
 * @version NP
 */
public class DateCommonChooser extends info.daviot.gui.toolkit.Panel implements ITextField
{
	protected IDatePopup datePopup;
	protected Spinner spinner;
	
    /**
     * Constructs the DateChooser.
     * 
     * @param spinnerPosition either SwingConstants.LEFT or SwingConstants.RIGHT
     * @throws IllegalArgumentException if another value is used for spinnerPosition
     */
	protected DateCommonChooser(int spinnerPosition)
	{
		super(new BorderLayout());
		spinner=new Spinner();
		switch(spinnerPosition)
		{
			case SwingConstants.LEFT :
				add(spinner, BorderLayout.WEST);	
				break;
			case SwingConstants.RIGHT :
				add(spinner, BorderLayout.EAST);	
				break;
			default : 
				throw new IllegalArgumentException("Invalid Spinner position : "+spinnerPosition);
		}
        spinner.addRelativeChangeListener(new RelativeChangeListener()
        {
          public void relativeChange(RelativeChangeEvent e)
          {
            switch (e.getChangeDirection())
            {
               case RelativeChangeEvent.INCREASED :
                  chooseNextDate();
                  break;
               case RelativeChangeEvent.DECREASED :
                  choosePreviousDate();
                  break;
               default :
            }
          }
        });				
	}
	
	public void setEditable(boolean editable)
	{
		((DateTextFieldEditor)((PopupEditor)datePopup).getSimpleEditor()).setEditable(editable);
	}
	
   /**
    * Dispatches correctly the enable property.
    * 
    * @param enable if the value can be changed
    */
   public void setEnabled(boolean enable)
   {
   	  super.setEnabled(enable)  ;
   	  ((Component)datePopup).setEnabled(enable);
   	  spinner.setEnabled(enable);
   }

	/**
	 * The focus listener is added to the appropriate editor components.
	 */
    public void addFocusListener(FocusListener listener)
    { 
    	if (datePopup!=null)
    	{
	    	((PopupEditor)datePopup).getSimpleEditor().getEditorComponent().addFocusListener(listener);
    	}
    	else
    	{
    		super.addFocusListener(listener);
    	}
    }

	/**
	 * The focus listener is removed from the appropriate editor components.
	 */
    public void removeFocusListener(FocusListener listener)
    { 
    	if (datePopup!=null)
    	{
	    	((PopupEditor)datePopup).getSimpleEditor().getEditorComponent().removeFocusListener(listener);
    	}
    	else
    	{
    		super.removeFocusListener(listener);
    	}
    }

    /**
     * Gets the date selected by the user
     * 
     * @return a Date object
     */
	public Object getCurrentValue()
	{ 
	    return getDate();
	}		
	
	
	public void setCurrentValue(Object value)
	{
		setValue((Calendar)value);
	}

    /**
     * Gets the current text value.
     * 
     * @return the text entered by the user or formatted
     */
	public String getText()
	{ 
		return datePopup.getText();		
	}		
	
	public void setValue(Calendar value)
	{
		datePopup.setValue(value);
	}	
	
	public void setValue(Date value)
	{
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(value);
		datePopup.setValue(calendar);
	}	
	
	public void clear()
	{
		datePopup.clear();
	}
	
	/**
	 * Gets the selected date.
	 * 
	 * @return the date
	 */
	public Date getDate()
	{
		Calendar calendar=(Calendar)datePopup.getValue();
		return calendar==null ? null : calendar.getTime();
	}
	

   private void chooseNextDate()
   {
   	  Calendar selectedDate=(Calendar)datePopup.getValue();
   	  if (selectedDate==null)
   	  {
   	  	 selectedDate=Calendar.getInstance();
   	  }
   	  selectedDate.add(Calendar.DAY_OF_YEAR, 1);
   	  datePopup.setValue(selectedDate);   	  
   }

   private void choosePreviousDate()
   {
   	  Calendar selectedDate=(Calendar)datePopup.getValue();
   	  if (selectedDate==null)
   	  {
   	  	 selectedDate=Calendar.getInstance();
   	  }
   	  selectedDate.add(Calendar.DAY_OF_YEAR, -1);
   	  datePopup.setValue(selectedDate);   	  
   }
}

