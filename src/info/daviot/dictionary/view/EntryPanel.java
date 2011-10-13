package info.daviot.dictionary.view;

import info.daviot.dictionary.DictionaryConstants;
import info.daviot.dictionary.model.DictionaryEntry;
import info.daviot.gui.component.ErrorMessageDialog;
import info.daviot.gui.component.JTextField;
import info.daviot.gui.field.DefaultInputFieldGroup;
import info.daviot.gui.field.IInputFieldGroup;
import info.daviot.gui.field.ITextField;
import info.daviot.util.validation.FailedValidationException;
import info.daviot.util.validation.GroupValidator;
import info.daviot.util.validation.NotEmptyValidator;
import info.daviot.util.validation.PatternValidator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * @author  ALEXIS
 */
public class EntryPanel extends JPanel {

    private static final String TRANSLATIONS_FIELD = "translations";
    private static final String WORD_FIELD = "word";
    private JLabel wordLabel = new JLabel();
    private JLabel translationLabel = new JLabel();
    private JTextField wordTextField = new JTextField();
    private JTextField translationsTextField = new JTextField();
    private JTextArea explainationTextArea = new JTextArea(4, 20);
    private JButton okButton = new JButton("Valider");
    private DictionaryFrame dictionaryFrame;
    private boolean modified;
    private String previousWord;
    private IInputFieldGroup inputFieldsGroup;
    private GroupValidator fieldsValidator;
    
    public EntryPanel(DictionaryFrame frame) {
        this.dictionaryFrame=frame;
        Map<String, ITextField> inputFields=new HashMap<String, ITextField>();
		wordTextField.setFont(DictionaryConstants.FONT);
		translationsTextField.setFont(DictionaryConstants.FONT);
        inputFields.put(WORD_FIELD, wordTextField);
        inputFields.put(TRANSLATIONS_FIELD,translationsTextField);
        inputFieldsGroup=new DefaultInputFieldGroup(inputFields);
        fieldsValidator=new GroupValidator();
        fieldsValidator.putValidator(WORD_FIELD, new NotEmptyValidator("Le mot doit �tre rempli"));
        fieldsValidator.putValidator(WORD_FIELD, new PatternValidator("[^,]+", "Le mot ne doit pas contenir de virgule", false));
        fieldsValidator.putValidator(TRANSLATIONS_FIELD, new NotEmptyValidator("La traduction doit �tre remplie"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(wordLabel);
        add(wordTextField);
        add(translationLabel);
        add(translationsTextField);
        add(new JLabel("Exemple ou explication :"));
        add(new JScrollPane(explainationTextArea));
        add(okButton);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonClicked();
            }
        });
        wordTextField.setDefaultButton(okButton);
        translationsTextField.setDefaultButton(okButton);
        explainationTextArea.setWrapStyleWord(true); 
        explainationTextArea.setLineWrap(true); 
        setupListeners();
    }
    
    private void setupListeners() {
        DocumentListener listener=new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                modified=true;
            }
            public void removeUpdate(DocumentEvent e) {
                modified=true;
            }
            public void changedUpdate(DocumentEvent e) {
                modified=true;
            }};
            wordTextField.getDocument().addDocumentListener(listener);
            translationsTextField.getDocument().addDocumentListener(listener);
            explainationTextArea.getDocument().addDocumentListener(listener);
    }

    public void requestFocus() {
        wordTextField.requestFocus();
    }
    
    public void setEnabled(boolean enabled) {
        wordTextField.setEnabled(enabled);
        translationsTextField.setEnabled(enabled);
        explainationTextArea.setEnabled(enabled);
        okButton.setEnabled(enabled);        
    }
    
    public void clear() {
        wordTextField.setText("");
        translationsTextField.setText("");
        explainationTextArea.setText("");
        setModified(false);
        previousWord=null;
    }
    
    public void setDictionaryEntry(String selectedWord, DictionaryEntry entry) {
        wordTextField.setText(selectedWord);
        StringBuffer translations=new StringBuffer();
        for (Iterator<String> i = entry.getTranslations().iterator(); i.hasNext();) {
            translations.append(i.next());
            if (i.hasNext()) translations.append(", ");                        
        }
        translationsTextField.setText(translations.toString());
        explainationTextArea.setText(entry.getExplaination());
        modified=false;
        previousWord=selectedWord;
    }
    
    public void setLanguages(String first, String second) {
        wordLabel.setText("Mot en "+first);
        translationLabel.setText("Traduction(s) en "+second);
    }
    
    protected void okButtonClicked() {
        try {
            fieldsValidator.validate(inputFieldsGroup.getCurrentValues());
            String word = wordTextField.getText().trim();
            String translations = translationsTextField.getText().trim();
            if (word.length() >0 && translations.length()>0) {
                modified=false;
                dictionaryFrame.addEntry(previousWord, word,translations, explainationTextArea.getText());
                dictionaryFrame.setModified(true);
                dictionaryFrame.newButtonClicked();
            } 
        } catch (FailedValidationException e) {
            new ErrorMessageDialog(dictionaryFrame, "Erreur dans les champs", e.getReason(), e).setVisible(true);
        }
    }

    /**
     * @return  Returns the modified.
     * @uml.property  name="modified"
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified  The modified to set.
     * @uml.property  name="modified"
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
