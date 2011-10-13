package info.daviot.gui.component.listeditor;

import info.daviot.gui.field.IInputField;

public interface EditorPanel<T> extends IInputField {
    public void addItem();    
    public T getCurrentValue();
    public void addEditCompleteListener(EditCompleteListener<T> l);
    public void removeEditCompleteListener(EditCompleteListener<T> l);
}
