package com.tyrcho.gui.toolkit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.tyrcho.gui.field.IInputField;


/**
 * Displays a group of Radio Buttons and allows the selection to be 
 * retrieved as an object.
 * @author MDA.
 */
public class RadioButtonGroup 
extends Panel
implements IInputField, SwingConstants
{
	private Object selectedValue;
	private Map buttons=new HashMap();
	private Map values;
	private ButtonGroup buttonGroup=new ButtonGroup();
	//Used to deselect other buttons.
	private JRadioButton deselect=new JRadioButton();
	
	/**
	 * @param orientation either VERTICAL or HORIZONTAL, used to layout buttons
	 * @param values a map with keys=labels, values=the object to return
	 */
	public RadioButtonGroup(int orientation, Map values)
	{
		super(new BorderLayout());
		this.values=values;
		javax.swing.Box buttonsBox;
		switch (orientation)
		{
			case VERTICAL : buttonsBox=Box.createVerticalBox();
			case HORIZONTAL : buttonsBox=Box.createHorizontalBox();
			break;
			default : throw new InvalidParameterException(orientation +" is not a valid orientation.");
		}
		for (Iterator i=values.keySet().iterator(); i.hasNext(); )
		{
			String label=(String)i.next();
			JRadioButton button=createRadioButton(label);
			final Object value=values.get(label);
			buttons.put(label, button);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					selectedValue=value;
				}
			});
			buttonsBox.add(button);
			if (i.hasNext())
			{
				buttonsBox.add(Box.createGlue());
			}
			buttonGroup.add(button);
		}
		buttonGroup.add(deselect);
		add(buttonsBox, BorderLayout.CENTER);
	}
	
	public void addFocusListener(FocusListener l)
	{
		if (buttons!=null)
		{
			for (Iterator i=buttons.values().iterator(); i.hasNext(); )
			{
				JRadioButton button=(JRadioButton) i.next();
				button.addFocusListener(l);
			}
		}
	}
	
	public void removeFocusListener(FocusListener l)
	{
		if (buttons!=null)
		{
			for (Iterator i=buttons.values().iterator(); i.hasNext(); )
			{
				JRadioButton button=(JRadioButton) i.next();
				button.removeFocusListener(l);
			}
		}
	}

	public void addActionListener(ActionListener l)	
	{
		if (buttons!=null)
		{
			for (Iterator i=buttons.values().iterator(); i.hasNext(); )
			{
				JRadioButton button=(JRadioButton) i.next();
				button.addActionListener(l);
			}
		}
	}
	
	public void removeActionListener(ActionListener l)	
	{
		if (buttons!=null)
		{
			for (Iterator i=buttons.values().iterator(); i.hasNext(); )
			{
				JRadioButton button=(JRadioButton) i.next();
				button.removeActionListener(l);
			}
		}
	}
	
	/**
	 * Can be override to return a specific implementation of JRadioButton.
	 */
	protected JRadioButton createRadioButton(String label)
	{
		return new JRadioButton(label);
	}
	
	public Object getCurrentValue()
	{
		return selectedValue;
	}
	
	public void setCurrentValue(Object newValue)
	{
		boolean found=false;
		for (Iterator i=values.keySet().iterator(); i.hasNext(); )
		{
			Object label=i.next();
			Object value=values.get(label);
			if (value.equals(newValue))
			{
                found=true;
				JRadioButton button=(JRadioButton)buttons.get(label);
				button.doClick();
			}
		}	
		if (!found)
		{
			deselect.setSelected(true);
		}	
	}

	public void clear()
	{
		setCurrentValue(null);
	}
	
	public void setEditable(boolean editable)
	{
		for (Iterator i=buttons.values().iterator(); i.hasNext(); )
		{
			JRadioButton button=(JRadioButton)i.next();
			button.setEnabled(editable);
		}
	}
}

