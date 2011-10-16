package info.daviot.dictionary.view;

import info.daviot.dictionary.DictionaryConstants;
import info.daviot.dictionary.model.DictionaryEntry;
import info.daviot.gui.component.ErrorMessageDialog;
import info.daviot.gui.component.JTextField;
import info.daviot.gui.field.DefaultInputFieldGroup;
import info.daviot.gui.field.IInputFieldGroup;
import info.daviot.gui.field.ITextField;
import info.daviot.util.validation._

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class EntryPanel(frame: DictionaryFrame) extends JPanel {
  val TRANSLATIONS_FIELD = "translations";
  val WORD_FIELD = "word";
  val wordLabel = new JLabel();
  val translationLabel = new JLabel();
  val wordTextField = new JTextField();
  val translationsTextField = new JTextField();
  val explainationTextArea = new JTextArea(4, 20);
  val okButton = new JButton("Valider");

  var modified = false
  var previousWord: String = _

  wordTextField.setFont(DictionaryConstants.FONT);
  translationsTextField.setFont(DictionaryConstants.FONT);
  val inputFields = Map(WORD_FIELD -> wordTextField, TRANSLATIONS_FIELD -> translationsTextField)
  val fieldsValidator = new GroupValidator(Map(
    WORD_FIELD -> new NotEmptyValidator("Le mot doit être rempli"),
    WORD_FIELD -> new PatternValidator("[^,]+", "Le mot ne doit pas contenir de virgule", false),
    TRANSLATIONS_FIELD -> new NotEmptyValidator("La traduction doit être remplie")))
  setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  add(wordLabel);
  add(wordTextField);
  add(translationLabel);
  add(translationsTextField);
  add(new JLabel("Exemple ou explication :"));
  add(new JScrollPane(explainationTextArea));
  add(okButton);
  okButton.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent) {
      okButtonClicked();
    }
  });
  wordTextField.setDefaultButton(okButton);
  translationsTextField.setDefaultButton(okButton);
  explainationTextArea.setWrapStyleWord(true);
  explainationTextArea.setLineWrap(true);
  setupListeners();

  private def setupListeners() {
    val listener = new DocumentListener() {
      def insertUpdate(e: DocumentEvent) {
        modified = true;
      }

      def removeUpdate(e: DocumentEvent) {
        modified = true;
      }

      def changedUpdate(e: DocumentEvent) {
        modified = true;
      }
    };
    wordTextField.getDocument().addDocumentListener(listener);
    translationsTextField.getDocument().addDocumentListener(listener);
    explainationTextArea.getDocument().addDocumentListener(listener);
  }

  override def requestFocus() {
    wordTextField.requestFocus();
  }

  override def setEnabled(enabled: Boolean) {
    wordTextField.setEnabled(enabled);
    translationsTextField.setEnabled(enabled);
    explainationTextArea.setEnabled(enabled);
    okButton.setEnabled(enabled);
  }

  def clear() {
    wordTextField.setText("");
    translationsTextField.setText("");
    explainationTextArea.setText("");
    modified = false
    previousWord = null;
  }

  def setDictionaryEntry(selectedWord: String, entry: DictionaryEntry) {
    wordTextField.setText(selectedWord);
    val translations = new StringBuffer();
    val i = entry.translations.iterator()
    while (i.hasNext()) {
      translations.append(i.next());
      if (i.hasNext())
        translations.append(", ");
    }
    translationsTextField.setText(translations.toString());
    explainationTextArea.setText(entry.explaination);
    modified = false;
    previousWord = selectedWord;
  }

  def setLanguages(first: String, second: String) {
    wordLabel.setText("Mot en " + first);
    translationLabel.setText("Traduction(s) en " + second);
  }

  def okButtonClicked() {
    val valuesMap = inputFields map (kv => (kv._1, kv._2.getCurrentValue()))
    val validation = fieldsValidator.validate(valuesMap)
    if (validation.isEmpty) {
      val word = wordTextField.getText().trim();
      val translations = translationsTextField.getText().trim();
      if (word.length() > 0 && translations.length() > 0) {
        modified = false;
        frame.addEntry(previousWord, word, translations,
          explainationTextArea.getText());
        frame.setModified(true);
        frame.newButtonClicked();
      }
    } else
      new ErrorMessageDialog(frame, "Erreur dans les champs", validation.toString).setVisible(true);
  }

}
