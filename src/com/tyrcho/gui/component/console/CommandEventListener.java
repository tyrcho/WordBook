package com.tyrcho.gui.component.console;

import java.util.EventListener;

public interface CommandEventListener extends EventListener {
    public void commandPerformed(CommandEvent e);
}
