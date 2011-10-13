package info.daviot.dictionary.view;

import java.awt.Font;

import javax.swing.event.ListSelectionListener;

public interface WordsListDisplay {

	void setSelectedIndex(int i);

	Object getSelectedValue();

	void setFont(Font font);

	void setSelectedValue(Object selectedValue, boolean shouldScroll);

	void addListSelectionListener(ListSelectionListener listSelectionListener);

}
