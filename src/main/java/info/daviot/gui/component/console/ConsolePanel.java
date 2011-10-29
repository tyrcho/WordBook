package info.daviot.gui.component.console;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Displays a JPanel with a command line and a textarea with messages, IRC-like.
 * 
 * @author tyrcho
 */
public class ConsolePanel extends AbstractConsole {
	private static final long serialVersionUID = 430291218652793973L;
	protected JPanel panel;
	private JTextArea messagesArea;
	private JTextField commandLine;
	private JScrollPane scrollPane;

	public JPanel getPanel() {
		return panel;
	}

	public ConsolePanel() {
		init();
	}

	private void init() {
		panel = new JPanel(new BorderLayout());
		messagesArea = new JTextArea(20, 80);
		messagesArea.setLineWrap(true);
		messagesArea.setWrapStyleWord(true);
		messagesArea.setEditable(false);
		commandLine = new JTextField();
		JButton okButton = new JButton("OK");
		commandLine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okButtonClicked();
			}
		});
		Box box = Box.createHorizontalBox();
		box.add(commandLine);
		box.add(okButton);
		scrollPane = new JScrollPane(messagesArea);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(box, BorderLayout.SOUTH);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButtonClicked();
			}
		});
	}

	public void setFont(Font f) {
		messagesArea.setFont(f);
		commandLine.setFont(f);
	}

	private void okButtonClicked() {
		String command = commandLine.getText();
		commandLine.setText("");
		println(command);
		fireCommandEvent(this, command);
	}

	public void print(String message) {
		messagesArea.append(message);
		final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
	}

	public void requestFocus() {
		commandLine.requestFocus();
	}

	public void clear() {
		messagesArea.setText("");
		commandLine.setText("");
	}
}
