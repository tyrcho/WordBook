package info.daviot.dictionary.view;

import javax.swing.DefaultListModel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class WordsDisplayAsList extends JList implements WordsListDisplay {

	public WordsDisplayAsList(DefaultListModel listModel) {
		super(listModel);
	}


}
