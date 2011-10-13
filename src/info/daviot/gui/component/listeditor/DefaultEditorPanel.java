package info.daviot.gui.component.listeditor;

import info.daviot.gui.component.OkCancelPanel;
import info.daviot.gui.field.IInputFieldGroup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public abstract class DefaultEditorPanel<T> 
extends OkCancelPanel implements EditorPanel<T> {
    private List<EditCompleteListener<T>> listeners = new ArrayList<EditCompleteListener<T>>();
    protected IInputFieldGroup            fields;

    public DefaultEditorPanel(IInputFieldGroup fields) {
        this.fields = fields;
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditOkEvent(getCurrentValue());
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditCancelledEvent();
            }
        });
        okButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }

    public void addEditCompleteListener(EditCompleteListener<T> l) {
        listeners.add(l);
    }

    public void removeEditCompleteListener(EditCompleteListener<T> l) {
        listeners.remove(l);
    }

    protected void fireEditOkEvent(T editedObject) {
        for (int i = listeners.size() - 1; i >= 0; i--) {
            EditCompleteListener<T> listener = listeners.get(i);
            listener.editOk(editedObject);
        }
    }

    protected void fireEditCancelledEvent() {
        for (int i = listeners.size() - 1; i >= 0; i--) {
            EditCompleteListener<T> listener = listeners.get(i);
            listener.editCancelled();
        }
    }

    public void addItem() {
        setEditable(true);
        clear();
        requestFocus();
    }

    public void setEditable(boolean editable) {
        fields.setEditable(editable);
        okButton.setEnabled(editable);
        cancelButton.setEnabled(editable);
    }

    public void clear() {
        fields.clear();
    }

}
