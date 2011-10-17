package info.daviot.dictionary.view;

import scala.collection.JavaConversions._
import info.daviot.dictionary.DictionaryConstants;
import info.daviot.dictionary.model.DictionaryEntry;
import info.daviot.dictionary.model.Session;
import info.daviot.dictionary.model.SessionCompleteEvent;
import info.daviot.dictionary.model.SessionCompleteListener;
import info.daviot.dictionary.model.SessionParameters;
import info.daviot.dictionary.model.TwoWayDictionary;
import info.daviot.dictionary.model.factory.DictionnaryFactory;
import info.daviot.dictionary.model.factory.XstreamDictionaryFactory;
import info.daviot.gui.component.ErrorMessageDialog;
import info.daviot.gui.toolkit.RadioButtonGroup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt._;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.swing._;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

class DictionaryFrame(firstLanguageName: String, secondLanguageName: String) extends JFrame {
  import DictionaryFrame._
  val HELP_PAGE = "https://sites.google.com/site/micheldaviot/hobbies/apprentissage-du-chinois/logiciel";
  val STRING_SEPARATOR = ",( )*";
  val EXTENSION = "dict";

  var dictionary: TwoWayDictionary = _
  val listModel = new DefaultListModel();
  val wordsList = new WordsDisplayAsList(listModel);

  val newButton = new JButton("Nouveau");
  val helpButton = new JButton("Aide");
  val deleteButton = new JButton("Supprimer");
  val searchField = new JTextField(10);
  var languageSelector: RadioButtonGroup = _

  var firstLanguageSelected = false

  val entryPanel = new EntryPanel(this);
  var currentFile: File = _

  var _modified = false

  def modified_=(m: Boolean) {
    _modified = m
    updateStatus();
  }

  def modified = _modified

  var lastSelectedWord: String = _

  val saveAction = new AbstractAction("Enregistrer") {
    def actionPerformed(e: ActionEvent) {
      saveClicked();
    }
  };

  val exportAction = new AbstractAction("Exporter") {
    def actionPerformed(e: ActionEvent) {
      exportClicked();
    }
  };

  val trainingAction = new AbstractAction("Entrainement") {
    def actionPerformed(e: ActionEvent) {
      runTraining();
    }
  };

  val printAction = new AbstractAction("Imprimer") {
    def actionPerformed(e: ActionEvent) {
      //print();
    }
  };

  val saveAsAction = new AbstractAction("Enregistrer sous") {
    def actionPerformed(e: ActionEvent) {
      saveAsClicked();
    }
  };

  val loadAction = new AbstractAction("Charger") {
    def actionPerformed(e: ActionEvent) {
      loadClicked();
    }
  };

  val helpAction = new AbstractAction("Aide") {
    def actionPerformed(e: ActionEvent) {
      showHelp();
    }
  };

  val importAction = new AbstractAction("Importer") {
    def actionPerformed(e: ActionEvent) {
      importClicked();
    }
  };

  val newDictionaryAction = new AbstractAction(
    "Nouveau dictionnaire") {
    def actionPerformed(e: ActionEvent) {
      newDictionaryClicked();
    }
  };

  val statusBar = new JLabel();

  val factory = new XstreamDictionaryFactory();

  var ignoredChars: String = _

  dictionary = new TwoWayDictionary(firstLanguageName, secondLanguageName);
  updateTitle();
  setupFrame();

  private def runTraining() {
    val parameters = SessionParametersPanel
      .showSessionParametersDialog(this,
        "Choisir la configuration de l'exercice", dictionary);
    if (parameters != null) {
      parameters.ignoredChars_$eq(ignoredChars);
      runSession(new Session(parameters));
      modified = true
    }
  }

  //	private def print() {
  //		val firstLanguage = popupChooseLanguage();
  //		if (firstLanguage != null) {
  //			try {
  //				JasperReport report = JasperCompileManager
  //						.compileReport(getClass().getClassLoader()
  //								.getResourceAsStream("dictionnaire.jrxml"));
  //				Collection<Map<String, String>> data = new ArrayList<Map<String, String>>();
  //				List<String> entries = dictionary
  //						.getSortedEntries(firstLanguage);
  //				for (String word : entries) {
  //					Map<String, String> map = new HashMap<String, String>();
  //					map.put("a", word);
  //					DictionaryEntry entry = dictionary.getEntry(word,
  //							firstLanguage);
  //					Collection<String> translations = entry.translations();
  //					StringBuffer buffer = new StringBuffer();
  //					for (Iterator<String> i = translations.iterator(); i
  //							.hasNext();) {
  //						buffer.append(i.next());
  //						if (i.hasNext()) {
  //							buffer.append(", ");
  //						}
  //					}
  //					map.put("b", buffer.toString());
  //					map.put("c", entry.explaination());
  //					data.add(map);
  //				}
  //				HashMap<String, String> parameters = new HashMap<String, String>();
  //				parameters.put("LANGAGE1", firstLanguage ? firstLanguageName
  //						: secondLanguageName);
  //				parameters.put("LANGAGE2", firstLanguage ? secondLanguageName
  //						: firstLanguageName);
  //				JasperPrint print = JasperFillManager.fillReport(report,
  //						parameters, new JRMapCollectionDataSource(data));
  //				JasperViewer.viewReport(print, false);
  //			} catch (JRException e) {
  //				JOptionPane.showMessageDialog(this,
  //						"Erreur Jasper Print" + e.getMessage());
  //				e.printStackTrace();
  //			}
  //		}
  //	}

  private def popupChooseLanguage(): Option[Boolean] = {
    val choices: Array[Object] = Array(firstLanguageName, secondLanguageName)
    val choice = JOptionPane.showInputDialog(this,
      "Choisir le langage de tri", "", JOptionPane.QUESTION_MESSAGE,
      null, choices, choices(0));
    if (choice == null) {
      None;
    } else {
      Some(choice == choices(0))
    }
  }

  private def runSession(session: Session) {
    val frame = new VocabularyTestFrame("Entrainement", session);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.addSessionCompleteListener(new SessionCompleteListener() {
      def sessionComplete(e: SessionCompleteEvent) {
        DictionaryFrame.this.sessionComplete(e.score, session, frame);
      }
    });
    frame.runSession();
  }

  private def sessionComplete(score: String, session: Session, frame: VocabularyTestFrame) {
    SwingUtilities.invokeLater(new Runnable() {
      def run() {
        Thread.sleep(1000);
        val options: Array[Object] = Array("Recommencer (mêmes mots)",
          "Autre exercice", "Changer de langue ", "Retour",
          "Même type d'exercice")
        val message = format("Votre score est de %s.%n%s%nEt maintenant ?", score, frame.getErrors())
        val choice = JOptionPane
          .showOptionDialog(frame, message, "Session terminée",
            JOptionPane.CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options,
            options(4));
        frame.dispose();
        choice match {
          case 0 =>
            session.resetScore();
            runSession(session);
          case 1 =>
            runTraining();
          case 2 =>
            session.switchLanguage();
            session.resetScore();
            runSession(session);
          case 4 =>
            session.resetQuestions();
            session.resetScore();
            runSession(session);
          case 3 =>
        }
      }
    });

  }

  private def updateTitle() {
    setTitle("Dictionnaire " + firstLanguageName + "-" + secondLanguageName);
  }

  private def loadClicked() {
    if (!isSaved("Attention aux données en cours", "Sauver les données en cours ?"))
      return ;
    val fileChooser = new JFileChooser();
    fileChooser.setFileFilter(filter);
    fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
    if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(this)) {
      val file = fileChooser.getSelectedFile();
      load(file);
    }
  }

  private def importClicked() {
    val fileChooser = new JFileChooser();
    fileChooser.setFileFilter(filter);
    fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
    if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(this)) {
      val file = fileChooser.getSelectedFile();
      importFile(file);
    }
  }

  private def updateFrame(file: File, dictionary: TwoWayDictionary) {
    val frame = new DictionaryFrame(dictionary.firstLanguage, dictionary.secondLanguage);
    frame.dictionary = dictionary;
    frame.ignoredChars = ignoredChars;
    frame.setCurrentFile(file);
    frame.updateList();
    frame.pack();
    frame.setBounds(getBounds());
    frame.updateStatus();
    frame.setVisible(true);
    dispose();
  }

  private def load(file: File) {
    try {
      factory.fileName_$eq(file.getAbsolutePath());
      dictionary = factory.load();
      updateFrame(file, dictionary);
    } catch {
      case e: Exception =>
        new ErrorMessageDialog(this, "Fichier non valide", "Ce fichier n'a pas pu être lu " + file.getAbsolutePath(), e).setVisible(true);
        updateFrame(file, new TwoWayDictionary("", ""));
    }
  }

  private def importFile(file: File) {
    try {
      factory.fileName_$eq(file.getAbsolutePath());
      val imported = factory.load();
      modified = true;
      dictionary.addAll(imported);
      updateFrame(currentFile, dictionary);
    } catch {
      case e: Exception =>
        new ErrorMessageDialog(this, "Fichier non valide", "Ce fichier n'a pas pu être lu " + file.getAbsolutePath(), e).setVisible(true);
    }
  }

  private def exportClicked() {
    val firstLanguage = popupChooseLanguage();
    if (firstLanguage.isDefined) {
      val fileChooser = new JFileChooser();
      if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
        var file = fileChooser.getSelectedFile();
        if (!(file.getName().indexOf('.') > 0)) {
          file = new File(file.getAbsolutePath() + ".csv");
        }
        if (!file.exists()
          || JOptionPane.YES_OPTION == JOptionPane
          .showConfirmDialog(this, "Ecraser le fichier "
            + file.getAbsolutePath(), "Ecraser ?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE)) {
          export(file, firstLanguage.get);
        }
      }
    }
  }

  private def export(file: File, firstLanguage: Boolean) {
    try {
      val fileWriter = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
      for (e <- dictionary.getSortedEntries(firstLanguage)) {
        fileWriter.append(e + ";");
        val entry = dictionary.getEntry(e, firstLanguage);
        val i = entry.translations.iterator();
        while (i.hasNext()) {
          fileWriter.append(i.next());
          if (i.hasNext()) {
            fileWriter.append(",");
          }
        }
        fileWriter.append(format(";%s;%s/%s%n", entry.explaination.replaceAll("\r|\n", ""), entry.goodAnswers, entry.getTotalAnswers));
      }
      fileWriter.close();
    } catch {
      case e: IOException =>
        showSaveError(file, e);
    }
  }

  private def saveAsClicked(): Boolean = {
    val fileChooser = new JFileChooser();
    fileChooser.setFileFilter(filter);
    if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
      var file = fileChooser.getSelectedFile();
      if (!(file.getName().indexOf('.') > 0)) {
        file = new File(file.getAbsolutePath() + "." + EXTENSION);
      }
      if (!file.exists()
        || JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
          this,
          "Ecraser le fichier " + file.getAbsolutePath(),
          "Ecraser ?", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE)) {
        return save(file);
      }
    }
    return false;
  }

  private def saveClicked() = save(currentFile)

  private def save(file: File): Boolean = {
    try {
      factory.fileName_$eq(file.getAbsolutePath());
      factory.save(dictionary);
      saveAction.setEnabled(true);
      modified = false
      updateStatus();
      JOptionPane.showMessageDialog(this,
        "Le fichier " + file.getAbsolutePath()
          + " a été enregistré.");
      return true;
    } catch {
      case e: Exception =>
        showSaveError(file, e);
        return false;
    }
  }

  private def showSaveError(file: File, e: Exception) {
    new ErrorMessageDialog(
      this,
      "Impossible d'enregistrer",
      "Impossible d'écrire dans le fichier " + file.getAbsolutePath(),
      e).setVisible(true);
  }

  private def updateStatus() {
    val f = if (currentFile == null) "" else currentFile.getName()
    val m = if (modified) "modifié" else ""
    statusBar.setText(f + " " + m);
  }

  private def isSaved(title: String, message: String): Boolean = {
    if (!modified)
      return true;
    val response = JOptionPane.showConfirmDialog(this, message, title,
      JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    response match {
      case JOptionPane.YES_OPTION =>
        if (currentFile == null) {
          return saveAsClicked();
        } else {
          return saveClicked();
        }

      case JOptionPane.NO_OPTION =>
        return true;

      case JOptionPane.CANCEL_OPTION =>
        return false;
      case _ =>
        return false;
    }
  }

  def addEntry(previousWord: String, word: String, translations: String, explanation: String) {
    val translationArray = translations.split(STRING_SEPARATOR);
    dictionary.removeWord(previousWord, firstLanguageSelected);
    dictionary.removeWord(word, firstLanguageSelected);
    dictionary.addExplaination(word, explanation, firstLanguageSelected);
    for (translation <- translationArray) {
      if (firstLanguageSelected) {
        dictionary.addTranslation(word, translation);
      } else {
        dictionary.addTranslation(translation, word);
      }
      dictionary.addExplaination(translation, explanation, !firstLanguageSelected);
    }
    if (!listModel.contains(word))
      updateList();
  }

  private def setupFrame() {
    val contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    val splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    contentPane.add(splitPane, BorderLayout.CENTER);
    contentPane.add(statusBar, BorderLayout.SOUTH);
    splitPane.setLeftComponent(buildLeftPanel());
    splitPane.setRightComponent(entryPanel);
    entryPanel.setEnabled(false);
    SwingUtilities.invokeLater(new Runnable() {
      def run() {
        updateLanguageSelected();
      }
    });
    setupListeners();
    languageSelector.setCurrentValue(secondLanguageName);
    setupMenu();
  }

  private def setupMenu() {
    val menuBar = new JMenuBar();
    saveAction.setEnabled(false);
    menuBar.add(new JButton(newDictionaryAction));
    menuBar.add(Box.createGlue());
    menuBar.add(new JButton(helpAction));
    menuBar.add(new JButton(loadAction));
    menuBar.add(new JButton(saveAction));
    menuBar.add(new JButton(saveAsAction));
    menuBar.add(new JButton(importAction));
    menuBar.add(new JButton(exportAction));
    menuBar.add(Box.createGlue());
    menuBar.add(new JButton(trainingAction));
    menuBar.add(Box.createGlue());
    menuBar.add(new JButton(printAction));
    menuBar.add(Box.createGlue());
    setJMenuBar(menuBar);
  }

  private def setupListeners() {
    searchField.getDocument().addDocumentListener(new DocumentListener() {
      val updateListRunner = new Runnable() {
        def run() {
          updateList();
        }
      };

      def insertUpdate(e: DocumentEvent) {
        SwingUtilities.invokeLater(updateListRunner);
      }

      def removeUpdate(e: DocumentEvent) {
        SwingUtilities.invokeLater(updateListRunner);
      }

      def changedUpdate(e: DocumentEvent) {
        SwingUtilities.invokeLater(updateListRunner);
      }
    });
    wordsList.addListSelectionListener(new ListSelectionListener() {
      def valueChanged(e: ListSelectionEvent) {
        if (!e.getValueIsAdjusting())
          listSelectionChanged(getSelectedWord());
      }
    });
    newButton.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        newButtonClicked();
      }
    });
    helpButton.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        showHelp();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        deleteButtonClicked();
      }
    });
    languageSelector.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        SwingUtilities.invokeLater(new Runnable() {
          def run() {
            updateLanguageSelected();
          }
        });
      }
    });
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      override def windowClosing(e: WindowEvent) {
        if (isSaved("Confirmer la sortie", "Sauver avant de quitter ?"))
          dispose();
      }
    });
  }

  private def listSelectionChanged(selectedWord: String) {
    if (selectedWord != null && selectedWord.length() > 0) {
      if (entryPanel.modified && lastSelectedWord != null
        && !selectedWord.equals(lastSelectedWord)) {
        val response = JOptionPane.showConfirmDialog(this,
          "Les données ont été modifiées",
          "Conserver les modifications ?",
          JOptionPane.YES_NO_CANCEL_OPTION,
          JOptionPane.QUESTION_MESSAGE);
        response match {
          case JOptionPane.YES_OPTION =>
            entryPanel.okButtonClicked();

          case JOptionPane.NO_OPTION =>

          case _ =>
            wordsList.setSelectedValue(selectedWord, false);
        }
      }
      val entry = if (firstLanguageSelected) dictionary.getFirstLanguageEntry(selectedWord)
      else dictionary.getSecondLanguageEntry(selectedWord);
      entryPanel.setDictionaryEntry(selectedWord, entry);
      entryPanel.setEnabled(true);
    } else {
      entryPanel.clear();
      entryPanel.setEnabled(false);
    }
    lastSelectedWord = selectedWord;
  }

  private def buildLeftPanel(): Component = {
    val panel = new JPanel(new BorderLayout());
    val topBox = Box.createHorizontalBox();
    topBox.add(newButton);
    topBox.add(Box.createHorizontalStrut(10));
    topBox.add(deleteButton);
    topBox.add(Box.createHorizontalStrut(10));
    topBox.add(new JLabel("Rechercher "));
    topBox.add(searchField);
    panel.add(topBox, BorderLayout.NORTH);
    wordsList.setFont(DictionaryConstants.FONT);
    panel.add(new JScrollPane(wordsList.asInstanceOf[Component]), BorderLayout.CENTER);
    val languages = new HashMap[String, String]();
    languages.put(firstLanguageName, firstLanguageName);
    languages.put(secondLanguageName, secondLanguageName);
    languageSelector = new RadioButtonGroup(SwingConstants.HORIZONTAL,
      languages);
    panel.add(languageSelector, BorderLayout.SOUTH);
    return panel;
  }

  def deleteButtonClicked() {
    val selectedWord = getSelectedWord();
    if (firstLanguageSelected) {
      dictionary.removeFirstLanguageWord(selectedWord);
    } else {
      dictionary.removeSecondLanguageWord(selectedWord);
    }
    listModel.removeElement(selectedWord);
    modified = true
  }

  def newButtonClicked() {
    entryPanel.clear();
    entryPanel.setEnabled(true);
    entryPanel.requestFocus();
  }

  private def updateLanguageSelected() {
    val currentValue = languageSelector.getCurrentValue().asInstanceOf[String]
    val languageSelectionChanged = firstLanguageSelected != firstLanguageName.equals(currentValue);
    if (languageSelectionChanged) {
      firstLanguageSelected = !firstLanguageSelected;
      updateList();
    }
    entryPanel.setLanguages(if (firstLanguageSelected) firstLanguageName else secondLanguageName,
      if (firstLanguageSelected) secondLanguageName else firstLanguageName);
  }

  private def updateList() {
    val listData = new ArrayList[String](dictionary.getSortedEntries(firstLanguageSelected));
    var searchFilter = searchField.getText().trim();
    if (searchFilter.length() > 0) {
      searchFilter = ".*" + searchFilter + ".*";
      val i = listData.iterator();
      while (i.hasNext()) {
        val word = i.next();
        if (!word.matches(searchFilter))
          i.remove();
      }
    }
    listModel.removeAllElements();
    for (word <- listData) {
      listModel.addElement(word);
    }
    wordsList.setSelectedIndex(0);
  }

  private def getSelectedWord() = wordsList.getSelectedValue().asInstanceOf[String]

  val filter = new FileFilter() {
    override def accept(f: File) = f.isDirectory() || f.getName().endsWith(EXTENSION);
    override def getDescription() = "*." + EXTENSION;
  };

  def setCurrentFile(currentFile: File) {
    this.currentFile = currentFile;
    if (currentFile == null) {
      saveAction.setEnabled(false);
    } else {
      saveAction.setEnabled(true);
      updatePropertiesFile(currentFile);
    }
  }

  def newDictionaryClicked() {
    if (!isSaved("Attention aux donnèes en cours",
      "Sauver les données en cours ?"))
      return ;

    val firstLanguage = JOptionPane.showInputDialog(this, "Première langue ?", null, JOptionPane.INFORMATION_MESSAGE,
      null, Locale.getISOLanguages().asInstanceOf[Array[Object]], "fr").asInstanceOf[String];
    val secondLanguage = JOptionPane.showInputDialog(this,
      "Deuxième langue ?", null, JOptionPane.INFORMATION_MESSAGE,
      null, Locale.getISOLanguages().asInstanceOf[Array[Object]], "en").asInstanceOf[String];
    dictionary = new TwoWayDictionary(firstLanguage, secondLanguage);
    updateFrame(null, dictionary);
  }

  private def showHelp() {
    try {
      Desktop.getDesktop().browse(new URI(HELP_PAGE));
    } catch {
      case e: Exception =>
        new ErrorMessageDialog(this, "Impossible d'ouvrir l'aide",
          "Impossible d'ouvrir l'adresse " + HELP_PAGE, e)
          .setVisible(true);
    }
  }
}

object DictionaryFrame extends Application {
  val propertiesFileName = System.getProperty("user.home") + "/dict.properties";

  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  val frame = new DictionaryFrame("Français", "Español");
  // frame.addEntry("hola", "salut, bonjour", "Hola se�or");
  frame.pack();
  try {
    frame.load(new File(readPropertiesFile()));
  } catch {
    case e: IOException =>
      println("Premier démarrage, le fichier " + propertiesFileName + " n'existe pas encore");
      frame.setVisible(true);
  }

  def readPropertiesFile(): String = {
    val properties = new Properties();
    val os = new FileInputStream(propertiesFileName);
    properties.load(os);
    return properties.getProperty("currentFile");
  }

  def updatePropertiesFile(currentFile: File) {
    val properties = new Properties();
    properties.put("currentFile", currentFile.getAbsolutePath());
    val os = new FileOutputStream(propertiesFileName);
    properties.store(os, "dictionary");
  }
}