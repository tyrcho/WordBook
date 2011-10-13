package com.tyrcho.gui.event;

import java.util.EventObject;

/**
 * RelativeChangeEvent is used to notify interested parties that 
 * a value has been increased or decreased.
 * 
 * @author MDA
 * @version NP
 */ 
public class RelativeChangeEvent extends EventObject
{
	public static final int INCREASED=1;
	public static final int DECREASED=2;
	private int changeDirection;
	
	/**
	 * Constructs the event.
	 * 
	 * @param source the object fireing this event
	 * @param changeDirection either INCREASED or DECREASED
	 * depending on the modification of the value
	 */
	public RelativeChangeEvent(Object source, int changeDirection)
	{
		super(source);
		setChangeDirection(changeDirection);		
	}
	
	/**
	 * Gets the change direction
	 */
	public int getChangeDirection() 
	{
		return changeDirection;
	}

   private void setChangeDirection(int changeDirection)
   {
   	  if (changeDirection<1 || changeDirection>2)
   	  {
   	  	 throw new IllegalArgumentException("Invalid direction : "+changeDirection);
   	  }
   	  this.changeDirection = changeDirection;
   }

}

