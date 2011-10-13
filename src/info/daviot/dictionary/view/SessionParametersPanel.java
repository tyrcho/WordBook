package info.daviot.dictionary.view;

import info.daviot.dictionary.model.SessionParameters;
import info.daviot.dictionary.model.TwoWayDictionary;
import info.daviot.gui.component.TextFieldWithSlider;
import info.daviot.gui.toolkit.RadioButtonGroup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.java.util.SpringUtilities;

public class SessionParametersPanel extends JPanel {
	private static final long serialVersionUID = 7727334170064237821L;
	private RadioButtonGroup languageButtonGroup;
	private TextFieldWithSlider questionCountField;
	private TextFieldWithSlider randomCountField;
	// private TextFieldWithSlider badCountField;
	// private TextFieldWithSlider recentCountField;
	private TwoWayDictionary dictionary;
	protected boolean isUpdating;
	private static SessionParametersPanel panel;

	public SessionParametersPanel(TwoWayDictionary dictionary) {
		this.dictionary = dictionary;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		Map<String, Boolean> languages = new HashMap<String, Boolean>();
		languages.put(dictionary.firstLanguage(), true);
		languages.put(dictionary.secondLanguage(), false);
		languageButtonGroup = new RadioButtonGroup(RadioButtonGroup.VERTICAL,
				languages);
		languageButtonGroup.setCurrentValue(Boolean.FALSE);
		add(languageButtonGroup, BorderLayout.NORTH);
		add(buildPercentsFields(), BorderLayout.CENTER);
		languageButtonGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				questionCountField.setMax(getMaxSize());
			}
		});
	}

	private int getMaxSize() {
		return dictionary.getEntries(
				(Boolean) languageButtonGroup.getCurrentValue()).size();
	}

	private Component buildPercentsFields() {
		JPanel jPanel = new JPanel(new SpringLayout());
		jPanel.add(new JLabel("% Al�atoire"));
		jPanel.add(randomCountField = new TextFieldWithSlider(0, 100, 30));
		jPanel.add(new JLabel("Nombre de questions"));
		jPanel.add(questionCountField = new TextFieldWithSlider(0,
				getMaxSize(), 10));
		SpringUtilities.makeCompactGrid(jPanel, 2, 2);
		return jPanel;
	}

	private ChangeListener buildPercentsChangeListener(
			final TextFieldWithSlider current, final TextFieldWithSlider first,
			final TextFieldWithSlider second) {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						final int total = current.getValue() + first.getValue()
								+ second.getValue();
						if (total > 100) {
							int diff1 = Math.min(total - 100, first.getValue());
							first.setValue(first.getValue() - diff1);
							int diff2 = total - diff1 - 100;
							if (diff2 > 0) {
								second.setValue(second.getValue() - diff2);
							}
						} else if (total < 100) {
							int diff1 = Math.min(100 - total,
									100 - first.getValue());
							first.setValue(first.getValue() + diff1);
							int diff2 = 100 - total - diff1;
							if (diff2 > 0) {
								second.setValue(second.getValue() + diff2);
							}
						}
					}
				});
			}
		};
	}

	public SessionParameters getSessionParameters() {
		return new SessionParameters(dictionary,
				((Boolean) languageButtonGroup.getCurrentValue())
						.booleanValue(), questionCountField.getValue(),
				randomCountField.getValue());
	}

	public static SessionParameters showSessionParametersDialog(JFrame frame,
			String title, TwoWayDictionary dictionary) {
		if (panel == null) {
			panel = new SessionParametersPanel(dictionary);
		}
		int action = JOptionPane.showConfirmDialog(frame, panel,
				"Param�tres de session", JOptionPane.OK_CANCEL_OPTION);
		if (action == JOptionPane.OK_OPTION) {
			return panel.getSessionParameters();
		} else {
			return null;
		}
	}
}