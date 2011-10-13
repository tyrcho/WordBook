package com.tyrcho.gui.component;

import javax.swing.text.Document;

import com.tyrcho.gui.field.ITextField;

public class JTextArea extends javax.swing.JTextArea implements ITextField {
    private static final long serialVersionUID = 5818154789466873133L;

    public JTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
    }

    public JTextArea(Document doc) {
        super(doc);
    }

    public JTextArea(int rows, int columns) {
        super(rows, columns);
    }

    public JTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
    }

    public JTextArea(String text) {
        super(text);
    }

    public JTextArea() {
        super();
    }

    public Object getCurrentValue() {
        return getText();
    }

    public void setCurrentValue(Object value) {
        setText(value.toString());
    }

    public void clear() {
        setCurrentValue("");
    }

}
