package info.daviot.gui

import info.daviot.gui.field.IInputField;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.security.InvalidParameterException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 * Displays a group of Radio Buttons and allows the selection to be retrieved as
 * an object.
 *
 * @author mdaviot
 */
class RadioButtonGroup(orientation: Int, values: Map[String, Any]) extends JPanel(new BorderLayout()) with IInputField {
  var selectedValue: Any = _
  val buttonGroup = new ButtonGroup();
  // Used to deselect other buttons.
  val deselect = new JRadioButton();

  val buttonsBox = if (SwingConstants.VERTICAL == orientation) Box.createVerticalBox() else Box.createHorizontalBox()
  val buttons = values.map { case (label, value) => (value -> new JRadioButton(label)) }
  buttons foreach {
    case (value, button) => {
      button.addActionListener(new ActionListener() { def actionPerformed(e: ActionEvent) { selectedValue = value } })
      buttonsBox.add(button)
      buttonsBox.add(Box.createGlue())
      buttonGroup.add(button);
    }
  }

  buttonGroup.add(deselect);
  add(buttonsBox, BorderLayout.CENTER);

  override def addFocusListener(l: FocusListener) { buttons.values.foreach(_.addFocusListener(l)) }
  override def removeFocusListener(l: FocusListener) { buttons.values.foreach(_.removeFocusListener(l)) }

  def addActionListener(l: ActionListener) { buttons.values.foreach(_.addActionListener(l)) }
  def removeActionListener(l: ActionListener) { buttons.values.foreach(_.removeActionListener(l)) }

  def setEditable(editable: Boolean) { buttons.values.foreach(_.setEnabled(editable)) }

  def getCurrentValue = selectedValue.asInstanceOf[AnyRef]

  def setCurrentValue(newValue: Any) {
    buttons.find(_._1 == newValue) match {
      case Some((_, button)) => button.doClick
      case None => deselect.setSelected(true)
    }
  }

  def clear() { setCurrentValue(null); }

}
