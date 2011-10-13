package com.tyrcho.gui.field;

/**
 * Specific input field where the user can enter a free text.
 * The getCurrentValue() method can return the same value as getText()
 * (such as in TextField) or a different object (such as in DateChooser)
 * so it can be validated or processed differently.
 * 
 * @author MDA
 * @version NP
 */
public interface ITextField extends IInputField
{
	/**
	 * Gets the text entered by the user.
	 * The getCurrentValue() method can return the same value as getText()
	 * (such as in TextField) or a different object (such as in DateChooser)
	 * so it can be validated or processed differently.
	 * 
	 * @return the text
	 */
	public String getText();
}

