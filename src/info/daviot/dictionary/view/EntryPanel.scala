package info.daviot.dictionary.view

import info.daviot.dictionary.DictionaryConstants
import info.daviot.dictionary.model.DictionaryEntry
import info.daviot.gui.component.ErrorMessageDialog
import info.daviot.gui.field.DefaultInputFieldGroup
import info.daviot.gui.field.IInputFieldGroup
import info.daviot.gui.field.ITextField
import info.daviot.util.validation._
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.Iterator
import javax.swing._
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import scala.swing._
import scala.swing.event._

class EntryPanel(frame: DictionaryFrame) extends BoxPanel(Orientation.Vertical) {
  val TRANSLATIONS_FIELD = "translations"
  val WORD_FIELD = "word"
  val wordLabel = new Label()
  val translationLabel = new Label()
  val wordTextField = new TextField()
  val translationsTextField = new TextField()
  val explainationTextArea = new TextArea(4, 20)
  val okButton = new Button("Valider")

  var modified = false
  var previousWord: String = _

  wordTextField.font = DictionaryConstants.FONT
  translationsTextField.font = DictionaryConstants.FONT
  val inputFields = Map(WORD_FIELD -> wordTextField, TRANSLATIONS_FIELD -> translationsTextField)
  val fieldsValidator = new GroupValidator(Map(
    WORD_FIELD -> new NotEmptyValidator("Le mot doit être rempli"),
    WORD_FIELD -> new PatternValidator("[^,]+", "Le mot ne doit pas contenir de virgule", true),
    TRANSLATIONS_FIELD -> new NotEmptyValidator("La traduction doit être remplie")))
  contents += wordLabel
  contents += wordTextField
  contents += translationLabel
  contents += translationsTextField
  contents += new Label("Exemple ou explication :")
  contents += new ScrollPane(explainationTextArea)
  contents += okButton
  okButton.reactions += { case ButtonClicked(_) => okButtonClicked }
  Seq(wordTextField, translationsTextField) foreach
    (_.keys.reactions += { case KeyPressed(_, Key.Enter, _, _) => okButtonClicked })
  explainationTextArea.peer.setWrapStyleWord(true)
  explainationTextArea.peer.setLineWrap(true)
  setupListeners()

  private def setupListeners() {
    Seq(wordTextField, translationsTextField, explainationTextArea) foreach
      (_.reactions += { case ValueChanged(_) => modified = true })
  }

  override def requestFocus() {
    wordTextField.requestFocus()
  }

  def setEnabled(enabled: Boolean) {
    Seq(wordTextField, translationsTextField, explainationTextArea, okButton) foreach
      (_.enabled = enabled)
  }

  def clear() {
    Seq(wordTextField, translationsTextField, explainationTextArea) foreach
      (_.text = "")
    modified = false
    previousWord = null
  }

  def setDictionaryEntry(selectedWord: String, entry: DictionaryEntry) {
    wordTextField.text = selectedWord
    val translations = new StringBuffer()
    val i = entry.translations.iterator()
    while (i.hasNext()) {
      translations.append(i.next())
      if (i.hasNext())
        translations.append(", ")
    }
    translationsTextField.text = translations.toString
    explainationTextArea.text = entry.explaination
    modified = false
    previousWord = selectedWord
  }

  def setLanguages(first: String, second: String) {
    wordLabel.text = "Mot en " + first
    translationLabel.text = "Traduction(s) en " + second
  }

  def okButtonClicked() {
    val valuesMap = inputFields map { case (key, field) => (key, field.text) }
    val validation = fieldsValidator.validate(valuesMap)
    if (validation.isEmpty) {
      val word = wordTextField.text.trim
      val translations = translationsTextField.text.trim
      if (word.length() > 0 && translations.length() > 0) {
        modified = false
        frame.addEntry(previousWord, word, translations, explainationTextArea.text)
        frame.modified = true
        frame.newButtonClicked()
      }
    } else
      new ErrorMessageDialog(frame.peer, "Erreur dans les champs", validation.toString).setVisible(true)
  }

}
