package info.daviot.dictionary.view

import info.daviot.dictionary.model.SessionParameters
import info.daviot.dictionary.model.TwoWayDictionary
import info.daviot.gui.component.TextFieldWithSlider
import info.daviot.gui.toolkit.RadioButtonGroup
import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SpringLayout
import com.sun.java.util.SpringUtilities
import javax.swing.SwingConstants

import scala.collection.JavaConversions._

class SessionParametersPanel(dictionary: TwoWayDictionary) extends JPanel {
  val languages = Map(dictionary.firstLanguage -> true, dictionary.secondLanguage -> false)
  val languageButtonGroup = new RadioButtonGroup(SwingConstants.VERTICAL, languages)
  val randomCountField = new TextFieldWithSlider(0, 100, 30)
  val questionCountField = new TextFieldWithSlider(0, getMaxSize(), 10)

  setLayout(new BorderLayout())
  languageButtonGroup.setCurrentValue(false)
  add(languageButtonGroup, BorderLayout.NORTH)
  add(buildPercentsFields(), BorderLayout.CENTER)
  languageButtonGroup.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent) {
      questionCountField.setMax(getMaxSize())
    }
  })

  private def getMaxSize() = dictionary.getEntries(languageButtonGroup.getCurrentValue().asInstanceOf[Boolean]).size()

  private def buildPercentsFields() = {
    val jPanel = new JPanel(new SpringLayout())
    jPanel.add(new JLabel("% Aléatoire"))
    jPanel.add(randomCountField)
    jPanel.add(new JLabel("Nombre de questions"))
    jPanel.add(questionCountField)
    SpringUtilities.makeCompactGrid(jPanel, 2, 2)
    jPanel
  }

  def getSessionParameters() =
    new SessionParameters(dictionary, languageButtonGroup.getCurrentValue().asInstanceOf[Boolean], questionCountField.getValue(), randomCountField.getValue())
}

object SessionParametersPanel {
  var panel: SessionParametersPanel = _
  def showSessionParametersDialog(frame: JFrame, title: String, dictionary: TwoWayDictionary): SessionParameters = {
    if (panel == null) panel = new SessionParametersPanel(dictionary)
    if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(frame, panel, "Paramètres de session", JOptionPane.OK_CANCEL_OPTION))
      panel.getSessionParameters()
    else
       null
  }
}