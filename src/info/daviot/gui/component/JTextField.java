package info.daviot.gui.component;

import info.daviot.gui.field.ITextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.text.Document;


public class JTextField extends javax.swing.JTextField implements ITextField{
    private JButton defaultButton;

    private void setupListener() {
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultButtonClick();
            }
        });
    }

    private void defaultButtonClick() {
        if (defaultButton != null)
            defaultButton.doClick();
    }

    /**
     * Defines the button which must be clicked when the user presses enter on this.
     */
    public void setDefaultButton(JButton button) {
        defaultButton = button;
    }

    public JTextField() {
        super();
        setupListener();
    }

    public JTextField(String text) {
        super(text);
        setupListener();
    }

    public JTextField(int columns) {
        super(columns);
        setupListener();
    }

    public JTextField(String text, int columns) {
        super(text, columns);
        setupListener();
    }

    public JTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        setupListener();
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
