package info.daviot.gui.toolkit.calendar;

import info.daviot.gui.toolkit.TextField;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ComboBoxEditor;
import javax.swing.JFrame;



/**
 * Allows the user to choose a date from a combo box.
 * 
 * @version NP
 * @author MDA
 */
public class DatePopup extends PopupEditor implements IDatePopup
{
   private DateFormat format;
   
   /**
    * Constructs the date chooser.
    * 
    * @param format used to validate or display the date
    */
   public DatePopup(DateFormat format)
   {
      super(new DateTextFieldEditor(format), new DatePopupEditor(), Calendar.getInstance());
      this.format=format;
      //transmits changes to simple editor
      ((DateEditorPanel)popupEditor.getEditorComponent()).addItemListener(new ItemListener()
      {
	         public void itemStateChanged(ItemEvent e)
	         {
	         	if (e.getStateChange() == ItemEvent.SELECTED)
	         	{
	         		simpleEditor.setItem(e.getItem());	         		
	         	}
	         }
	  });      
   }
   
   /**
    * Gets the text entered in the simple text editor.
    * 
    * @return the text
    */
   public String getText()
   {
   	  TextField textFieldEditor=(TextField)simpleEditor.getEditorComponent();
   	  return textFieldEditor.getText();
   }

   public static void main(String[] s)
   {
      JFrame frame = new JFrame("DatePopup");
      DatePopup chooser = new DatePopup(new SimpleDateFormat("dd/MM/yyyy"));
      frame.getContentPane().add(chooser);
      frame.pack();
      frame.setVisible(true);
   }

}

class DatePopupEditor implements ComboBoxEditor
{
   private DateEditorPanel datePanel=new DateEditorPanel();
   private Collection actionListeners=new LinkedList();   
   
   public DatePopupEditor()
   {
   	  datePanel.addItemListener(new ItemListener()
   	  {
   	  	 public void itemStateChanged(ItemEvent e)
   	     {
   	     	fireActionEvent();	 
   	     }
   	  });
   }

   public Component getEditorComponent()
   {
      return datePanel;
   }
   public Object getItem()
   {
      return datePanel.getSelectedDate();
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
   }

   public void setItem(Object anObject)
   {
   	  if (anObject instanceof Calendar)
      datePanel.setSelectedDate((Calendar)anObject);
   }
}