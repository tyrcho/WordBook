package com.tyrcho.gui.component.listeditor;

public interface EditCompleteListener<T> {
    public void editOk(T editedObject);

    public void editCancelled();
}
