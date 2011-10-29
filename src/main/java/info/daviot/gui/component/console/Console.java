package info.daviot.gui.component.console;

/**
 * Specifies a console which can display messages and sends 
 * events on user input.
 * @author tyrcho
 */
public interface Console {
    public void print(String message);
    public void println(String message);
    public void println();
    public void clear();
    public void addCommandEventListener(CommandEventListener listener);
    public void removeCommandEventListener(CommandEventListener listener);
    
}
