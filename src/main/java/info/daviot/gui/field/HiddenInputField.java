package info.daviot.gui.field;

/**
 * Basic implementation of IInputField
 * for a field not displayed in the GUI.
 * @author MDA
 */
public class HiddenInputField implements IInputField
{
   private Object currentValue;
   
   public Object getCurrentValue()
   {
   	  return currentValue;
   }

   public void setCurrentValue(Object currentValue)
   {
   	  this.currentValue=currentValue;
   }
   
   public void setEditable(boolean editable)
   {}
   
   public void requestFocus()
   {}
   
   public void clear()
   {
   	  setCurrentValue(null);
   }

}

