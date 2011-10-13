package info.daviot.dictionary.model;

import java.util.EventListener;

public interface SessionCompleteListener extends EventListener {
    public void sessionComplete(SessionCompleteEvent e);
}
