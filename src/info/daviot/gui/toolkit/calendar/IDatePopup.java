package info.daviot.gui.toolkit.calendar;



/**
 * Specifies common methods for Date or DateTime popups.
 * 
 * @version NP
 * @author MDA
 */
public interface IDatePopup
{
    /**
     * Gets the current text value.
     * 
     * @return the text entered by the user or formatted
     */
	public String getText();

	public void setValue(Object value);

	public void clear();

	public Object getValue();
}

