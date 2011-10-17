package info.daviot.gui.component.listeditor;

import info.daviot.gui.component.JTextField;
import info.daviot.gui.field.DefaultInputFieldGroup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ListEditorPanel<T> extends JPanel {
	private SearchableList<T> list;
	private EditorPanel<T> editorPanel;
	private Action addAction = new AbstractAction("Ajouter") {
		public void actionPerformed(ActionEvent e) {
			addAction();
		}
	};
	private Action deleteAction = new AbstractAction("Supprimer") {
		public void actionPerformed(ActionEvent e) {
			deleteAction();
		}
	};
	private JButton addButton = new JButton(addAction);
	private JButton deleteButton = new JButton(deleteAction);
	private EditingMode editingMode;
	private RemovableEditCompleteListener editCompleteListener = new RemovableEditCompleteListener();
	protected int modifiedElementPosition;

	public ListEditorPanel(List<T> elements, EditorPanel<T> editorPanel,
			RegexpFilter<T> filter) {
		list = new SearchableList<T>(elements, filter);
		this.editorPanel = editorPanel;
		init();
	}

	private void deleteAction() {
		editCompleteListener.removeHimself();
		list.deleteSelectedItems();
	}

	private void addAction() {
		synchronized (this) {
			editCompleteListener.removeHimself();
			editingMode = EditingMode.NEW;
			editorPanel.addItem();
			editorPanel.addEditCompleteListener(editCompleteListener);
		}
	}

	private void init() {
		setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				list, (Component) editorPanel);
		add(splitPane, BorderLayout.CENTER);
		javax.swing.Box buttons = Box.createHorizontalBox();
		buttons.add(addButton);
		buttons.add(Box.createHorizontalGlue());
		buttons.add(deleteButton);
		add(buttons, BorderLayout.NORTH);
		list.getList().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					editCompleteListener.removeHimself();
					Object lastSelectedValue = list.getList()
							.getSelectedValue();
					modifiedElementPosition = list.getList().getSelectedIndex();
					if (lastSelectedValue == null) {
						editorPanel.clear();
						editorPanel.setEditable(false);
					} else {
						synchronized (this) {
							editorPanel.setCurrentValue(lastSelectedValue);
							editorPanel.setEditable(true);
							editorPanel
									.addEditCompleteListener(editCompleteListener);
						}
					}
				}
			}
		});
		editorPanel.setEditable(false);
	}

	private final class RemovableEditCompleteListener implements
			EditCompleteListener<T> {
		private void removeHimself() {
			editorPanel.removeEditCompleteListener(editCompleteListener);
			editorPanel.setEditable(false);
		}

		public void editOk(T editedObject) {
			if (EditingMode.NEW == editingMode) {
				list.addElement(editedObject);
				editingMode = EditingMode.EDIT;
			} else {
				list.updateElement(modifiedElementPosition, editedObject);
			}
			removeHimself();
		}

		public void editCancelled() {
			removeHimself();
		}
	}

	private static class StringEditorPanel extends DefaultEditorPanel<String> {
		private JTextField field = new JTextField();

		public StringEditorPanel() {
			super(new DefaultInputFieldGroup(Collections.singletonMap("toto",
					new JTextField())));
			field = (JTextField) fields.getInputField("toto");
			add(field);
		}

		public String getCurrentValue() {
			return field.getText();
		}

		public void setCurrentValue(Object value) {
			field.setCurrentValue(value);

		}

		public void requestFocus() {
			field.requestFocus();
		}

	}

	public static void main(String[] args) {
		List<String> data = new ArrayList<String>();
		data.addAll(Collections.nCopies(1, "aaaa"));
		data.addAll(Collections.nCopies(1, "toto"));
		data.addAll(Collections.nCopies(1, "aze"));
		Collections.shuffle(data);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ListEditorPanel<String> listEditor = new ListEditorPanel<String>(data,
				new StringEditorPanel(), new RegexpFilter<String>() {
					public boolean accepts(String object) {
						return object.matches(getRegexp());
					}
				});
		frame.getContentPane().add(listEditor);
		frame.pack();
		frame.setVisible(true);
	}

	public SearchableList<T> getList() {
		return list;
	}

	public List<T> getElements() {
		return list.getElements();
	}

}
