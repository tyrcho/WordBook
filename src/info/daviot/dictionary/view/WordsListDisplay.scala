package info.daviot.dictionary.view;

import java.awt.Font;

import javax.swing.event.ListSelectionListener;

trait WordsListDisplay {

  def setSelectedIndex(i: Int);
  def getSelectedValue(): Any
  def setFont(font: Font);
  def setSelectedValue(selectedValue: Any, shouldScroll: Boolean);
  def addListSelectionListener(listSelectionListener: ListSelectionListener);

}
