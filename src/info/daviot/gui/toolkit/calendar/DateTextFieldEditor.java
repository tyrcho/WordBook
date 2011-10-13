package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.toolkit.TextField;
import info.daviot.util.validation.FailedValidationException;
import info.daviot.util.validation.FormatValidator;
import info.daviot.util.validation.IValidator;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ComboBoxEditor;




/**
 * Editor which can be used in a combo box or a PopupEditor to 
 * enter a date.
 * 
 * @author MDA 
 * @version NP
 */
public class DateTextFieldEditor implements ComboBoxEditor
{
   private TextField textField=new TextField();
   private DateFormat format;
   private String oldText="";
   private Collection actionListeners=new LinkedList();   
   private IValidator formatValidator;

   /**
    * Constructs the editor.
    * 
    * @param format used to display / validate the date
    */
   public DateTextFieldEditor(DateFormat format)
   {
   	 this.format=format;
   	 formatValidator=new FormatValidator(format, null, true);
   	 textField.addFocusListener(new FocusAdapter()
   	 {
   	 	public void focusLost(FocusEvent e)
   	 	{
   	 		textFieldLostFocus();
   	 	}
   	 });
   }
   
   public void setEditable(boolean editable)
   {
   	  textField.setEditable(editable);
   }

   private void textFieldLostFocus()
   {
   	  try   	  
   	  {
   	  	 formatValidator.validate(textField.getText());
 	  	 oldText=textField.getText();
	   	 fireActionEvent();
   	  }
   	  catch (FailedValidationException e)
   	  {
//   	     textField.setText(oldText);
   	  }
   }

   public Component getEditorComponent()
   {
      return textField;
   }
   
   /**
    * Gets the user entered date.
    * 
    * @return a Calendar object or null if the value could not be parsed
    */
   public Object getItem()
   {
   	  String typedDate=textField.getText();
   	  try
   	  {
   	  	 Date date=format.parse(typedDate);
   	  	 Calendar calendar=Calendar.getInstance();
   	  	 calendar.setTime(date);
 	     return calendar;
   	  }
   	  catch (ParseException e)
   	  {
   	  	 return null;
   	  }
   }
   
   /**
    * Informs action listeners that the edited item has changed.
    */
   protected void fireActionEvent()
   {
       ActionEvent event=new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "modified");
   	   Iterator i=actionListeners.iterator();
   	   while (i.hasNext())
   	   {
   	   	  ActionListener listener=(ActionListener)i.next();
   	   	  listener.actionPerformed(event);
   	   }  	  
   }

   public void addActionListener(ActionListener l)
   {
   	  actionListeners.add(l);
   }

   public void removeActionListener(ActionListener l)
   {
   	  actionListeners.remove(l);
   }

   public void selectAll()
   {
   	  textField.selectAll();
   }

   /**
    * Sets the calendar object if a Calendar is passed
    * or the text with the toString method otherwise.
    * 
    * @param a Calendar or an Object
    */
   public void setItem(Object anObject)
   {
   	  if (anObject instanceof Calendar)
   	  {
   	  	 Date date=((Calendar)anObject).getTime();
   	  	 String dateString=format.format(date);
   	  	 textField.setText(dateString);
   	  }      	
   	  else
   	  {
   	  	 textField.setText(anObject.toString());
   	  }
   	  try   	  
   	  {
   	     format.parse(textField.getText());
   	     oldText=textField.getText();
	   	 fireActionEvent();
   	  }
   	  catch (ParseException e)
   	  {
   	  }
   }
}