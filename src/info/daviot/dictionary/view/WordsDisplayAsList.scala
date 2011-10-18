package info.daviot.dictionary.view

import javax.swing.DefaultListModel
import javax.swing.JList

class WordsDisplayAsList(listModel: DefaultListModel) extends JList(listModel) with WordsListDisplay