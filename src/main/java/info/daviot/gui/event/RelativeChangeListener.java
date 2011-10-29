package info.daviot.gui.event;

/**
 * A RelativeChangeEvent gets fired when a value is increased or decreased.
 * 
 * @author MDA
 * @version NP
 */
public interface RelativeChangeListener
{
   /**
    * Gets called when the value is increased or decreased.
    */
   public void relativeChange(RelativeChangeEvent evt)  ;
}

