package info.daviot.gui.component;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

/**
 * A text field to input a int, associated with a slider.
 * 
 * @author tyrcho
 * 
 */
public class TextFieldWithSlider extends JPanel {
	private JSlider jSlider;
	private JTextField jTextField;
	private UndoManager undoManager;

	public TextFieldWithSlider(int min, int max, int def) {
		super(new BorderLayout());
		def = Math.min(def, max);
		add(jSlider = new JSlider(min, max, def), BorderLayout.NORTH);
		add(jTextField = new JTextField(String.valueOf(def)),
				BorderLayout.SOUTH);
		undoManager = new UndoManager();
		jTextField.getDocument().addUndoableEditListener(undoManager);
		jSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				jTextField.setText(String.valueOf(jSlider.getValue()));
			}
		});
		jTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateSlider();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateSlider();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateSlider();
			}
		});
	}

	public void addChangeListener(ChangeListener cl) {
		jSlider.addChangeListener(cl);
	}

	public void setMin(int min) {
		jSlider.setMinimum(min);
	}

	public void setMax(int max) {
		jSlider.setMaximum(max);
	}

	public void setValue(int value) {
		jSlider.setValue(value);
	}

	public int getValue() {
		return jSlider.getValue();
	}

	private void updateSlider() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					jSlider.setValue(Integer.valueOf(jTextField.getText()));
				} catch (NumberFormatException e1) {
					undoManager.undo();
				}
			}
		});
	}
}
