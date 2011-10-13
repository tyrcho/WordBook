package info.daviot.gui.component.console;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractConsole implements Console {
    private List<CommandEventListener> commandEventListeners=new LinkedList<CommandEventListener>();
    
    public void addCommandEventListener(CommandEventListener listener) {
        commandEventListeners.add(listener);
    }

    public void removeCommandEventListener(CommandEventListener listener) {
        commandEventListeners.remove(listener);
    }
    
    public void println(String message) {
        print(message+System.getProperty("line.separator"));
    }

    public void println() {
        print(System.getProperty("line.separator"));
    }
    
    protected void fireCommandEvent(Object source, String command) {
        CommandEvent e=new CommandEvent(source, command);
        for (CommandEventListener listener : commandEventListeners) {
            listener.commandPerformed(e);
        }
    }

}
