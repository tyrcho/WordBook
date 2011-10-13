package com.tyrcho.gui.component;

import java.io.File;

public interface FileActionListener {
    public void newFile();
    
    public void load(File file);

    /**
     * @return true if the file could be saved
     */
    public boolean save(File file);


}
