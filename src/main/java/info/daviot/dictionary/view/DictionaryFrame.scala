package info.daviot.dictionary.view

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.BorderLayout
import java.awt.Desktop
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.URI
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.HashMap
import java.util.Locale
import java.util.Properties
import scala.collection.JavaConversions.asScalaBuffer
import scala.swing.Action
import info.daviot.scala.swing.MenuCreation._
import DictionaryFrame.updatePropertiesFile
import info.daviot.dictionary.model.Session
import info.daviot.dictionary.model.SessionCompleteEvent
import info.daviot.dictionary.model.SessionCompleteListener
import info.daviot.dictionary.model.TwoWayDictionary
import info.daviot.dictionary.DictionaryConstants
import info.daviot.gui.component.ErrorMessageDialog
import info.daviot.gui.RadioButtonGroup
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.filechooser.FileFilter
import javax.swing.Box
import javax.swing.DefaultListModel
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenuBar
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.WindowConstants
import info.daviot.util.language.LocalizedResources._
import info.daviot.util.language.LocalizedResources
import info.daviot.scala.swing.SaveBeforeLoseModifications
import info.daviot.dictionary.model.factory.XstreamDictionaryFactory
import scala.swing._
import scala.swing.event._

class DictionaryFrame extends Frame with SaveBeforeLoseModifications {
  import DictionaryFrame._
  //TODO when using scala components
  //  optionPaneParent = this
  LocalizedResources.defaultFileName = "wordbook"

  var firstLanguageName: String = "FR"
  var secondLanguageName: String = "EN"
  val HELP_PAGE = "https://sites.google.com/site/micheldaviot/hobbies/apprentissage-du-chinois/logiciel"
  val STRING_SEPARATOR = ",( )*"
  val EXTENSION = "dict"

  var dictionary: TwoWayDictionary = _
  val listModel = new DefaultListModel()
  val wordsList = new WordsDisplayAsList(listModel)

  val newButton = new Button("New".l)
  val helpButton = new Button("Aide")
  val deleteButton = new Button("Supprimer")
  val searchField = new TextField(10)

  var languageSelector = buildSelector
  def buildSelector = {
    val languages = Map(firstLanguageName -> firstLanguageName, secondLanguageName -> secondLanguageName)
    val selector = new RadioButtonGroup(SwingConstants.HORIZONTAL, languages)
    selector.setCurrentValue(secondLanguageName)
    selector.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) { Swing.onEDT(updateLanguageSelected) }
    })
    selector
  }

  var firstLanguageSelected = false

  val entryPanel = new EntryPanel(this)
  var currentFile: File = _

  def setModified(m: Boolean) {
    modified_=(m)
    updateStatus()
  }

  var lastSelectedWord: String = _

  val saveAction = Action("Enregistrer")(save).peer
  val exportAction = Action("Exporter")(exportClicked).peer
  val trainingAction = Action("Entrainement")(runTraining).peer
  val printAction = Action("Imprimer") { //print()
  }.peer
  val saveAsAction = Action("Enregistrer sous")(saveAsClicked).peer
  val loadAction = Action("Charger")(loadClicked).peer
  val helpAction = Action("Aide")(showHelp).peer
  val importAction = Action("Importer")(importClicked).peer
  val newDictionaryAction = Action("Nouveau dictionnaire")(newDictionaryClicked).peer

  val statusBar = new Label

  //TODO : define ignored chars based on languages (especially for Arabic)
  var ignoredChars: String = _

  dictionary = new TwoWayDictionary(firstLanguageName, secondLanguageName)
  updateTitle()
  setupFrame()

  private def runTraining() {
    val parameters = SessionParametersPanel.showSessionParametersDialog(peer, "Choisir la configuration de l'exercice", dictionary)
    if (parameters != null) {
      parameters.ignoredChars = ignoredChars
      runSession(new Session(parameters))
      setModified(true)
    }
  }

  //	private def print() {
  //		val firstLanguage = popupChooseLanguage()
  //		if (firstLanguage != null) {
  //			try {
  //				JasperReport report = JasperCompileManager
  //						.compileReport(getClass().getClassLoader()
  //								.getResourceAsStream("dictionnaire.jrxml"))
  //				Collection<Map<String, String>> data = new ArrayList<Map<String, String>>()
  //				List<String> entries = dictionary
  //						.getSortedEntries(firstLanguage)
  //				for (String word : entries) {
  //					Map<String, String> map = new HashMap<String, String>()
  //					map.put("a", word)
  //					DictionaryEntry entry = dictionary.getEntry(word,
  //							firstLanguage)
  //					Collection<String> translations = entry.translations()
  //					StringBuffer buffer = new StringBuffer()
  //					for (Iterator<String> i = translations.iterator() i
  //							.hasNext()) {
  //						buffer.append(i.next())
  //						if (i.hasNext()) {
  //							buffer.append(", ")
  //						}
  //					}
  //					map.put("b", buffer.toString())
  //					map.put("c", entry.explaination())
  //					data.add(map)
  //				}
  //				HashMap<String, String> parameters = new HashMap<String, String>()
  //				parameters.put("LANGAGE1", firstLanguage ? firstLanguageName
  //						: secondLanguageName)
  //				parameters.put("LANGAGE2", firstLanguage ? secondLanguageName
  //						: firstLanguageName)
  //				JasperPrint print = JasperFillManager.fillReport(report,
  //						parameters, new JRMapCollectionDataSource(data))
  //				JasperViewer.viewReport(print, false)
  //			} catch (JRException e) {
  //				JOptionPane.showMessageDialog(this,
  //						"Erreur Jasper Print" + e.getMessage())
  //				e.printStackTrace()
  //			}
  //		}
  //	}

  private def popupChooseLanguage(): Option[Boolean] = {
    val choices: Array[Object] = Array(firstLanguageName, secondLanguageName)
    val choice = JOptionPane.showInputDialog(peer, "Choisir le langage de tri", "", JOptionPane.QUESTION_MESSAGE, null, choices, choices(0))
    if (choice == null) {
      None
    } else {
      Some(choice == choices(0))
    }
  }

  private def runSession(session: Session) {
    val frame = new VocabularyTestFrame("Entrainement", session)
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    frame.addSessionCompleteListener(new SessionCompleteListener() {
      def sessionComplete(e: SessionCompleteEvent) {
        DictionaryFrame.this.sessionComplete(e.score, session, frame)
      }
    })
    frame.runSession()
  }

  private def sessionComplete(score: String, session: Session, frame: VocabularyTestFrame) {
    SwingUtilities.invokeLater(new Runnable() {
      def run() {
        Thread.sleep(1000)
        val options: Array[Object] = Array("Recommencer (mêmes mots)",
          "Autre exercice", "Changer de langue ", "Retour",
          "Même type d'exercice")
        val message = format("Votre score est de %s.%n%s%nEt maintenant ?", score, frame.getErrors())
        val choice = JOptionPane
          .showOptionDialog(frame, message, "Session terminée",
            JOptionPane.CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options,
            options(4))
        frame.dispose()
        choice match {
          case 0 =>
            session.resetScore()
            runSession(session)
          case 1 =>
            runTraining()
          case 2 =>
            session.switchLanguage()
            session.resetScore()
            runSession(session)
          case 4 =>
            session.resetQuestions()
            session.resetScore()
            runSession(session)
          case 3 =>
        }
      }
    })

  }

  private def updateTitle() {
    title = "Dictionnaire " + firstLanguageName + "-" + secondLanguageName
  }

  private def loadClicked() {
    if (checkAndSave("Attention aux données en cours", "Sauver les données en cours ?")) {
      //isSaved("Attention aux données en cours", "Sauver les données en cours ?")) {
      val fileChooser = new JFileChooser()
      fileChooser.setFileFilter(filter)
      fileChooser.setDialogType(JFileChooser.OPEN_DIALOG)
      if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(peer)) {
        val file = fileChooser.getSelectedFile()
        load(file)
      }
    }
  }

  private def importClicked() {
    val fileChooser = new JFileChooser()
    fileChooser.setFileFilter(filter)
    fileChooser.setDialogType(JFileChooser.OPEN_DIALOG)
    if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(peer)) {
      val file = fileChooser.getSelectedFile()
      importFile(file)
    }
  }

  private def load(file: File) {
    try {
      dictionary = XstreamDictionaryFactory.load(file)
      update(file, dictionary)
    } catch {
      case e: Exception =>
        showLoadError(file, e)
    }
  }

  private def importFile(file: File) {
    try {
      val imported = XstreamDictionaryFactory.load(file)
      setModified(true)
      dictionary.addAll(imported)
      update(currentFile, dictionary)
    } catch {
      case e: Exception =>
        showLoadError(file, e)
    }
  }

  private def exportClicked() {
    val firstLanguage = popupChooseLanguage()
    if (firstLanguage.isDefined) {
      val fileChooser = new JFileChooser()
      if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(peer)) {
        var file = fileChooser.getSelectedFile()
        if (!(file.getName().indexOf('.') > 0)) {
          file = new File(file.getAbsolutePath() + ".csv")
        }
        if (!file.exists || JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(peer,
          "Ecraser le fichier " + file.getAbsolutePath(), "Ecraser ?",
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
          export(file, firstLanguage.get)
        }
      }
    }
  }

  private def export(file: File, firstLanguage: Boolean) {
    try {
      val fileWriter = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"))
      for (e <- dictionary.getSortedEntries(firstLanguage)) {
        fileWriter.append(e + "")
        val entry = dictionary.getEntry(e, firstLanguage)
        val i = entry.translations.iterator()
        while (i.hasNext()) {
          fileWriter.append(i.next())
          if (i.hasNext()) {
            fileWriter.append(",")
          }
        }
        fileWriter.append(format("%s%s/%s%n", entry.explaination.replaceAll("\r|\n", ""), entry.goodAnswers, entry.getTotalAnswers))
      }
      fileWriter.close()
    } catch {
      case e: IOException =>
        showSaveError(file, e)
    }
  }

  private def saveAsClicked(): Boolean = {
    val fileChooser = new JFileChooser()
    fileChooser.setFileFilter(filter)
    if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(peer)) {
      var file = fileChooser.getSelectedFile()
      if (!(file.getName().indexOf('.') > 0)) {
        file = new File(file.getAbsolutePath() + "." + EXTENSION)
      }
      if (!file.exists()
        || JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
          peer,
          "Ecraser le fichier " + file.getAbsolutePath(),
          "Ecraser ?", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE)) {
        save(file)
      }
    }
    false
  }

  override def save(): Boolean = save(currentFile)

  private def save(file: File): Boolean = {
    try {
      XstreamDictionaryFactory.save(dictionary, file)
      saveAction.setEnabled(true)
      setModified(false)
      updateStatus()
      JOptionPane.showMessageDialog(peer, format("Le fichier %s a été enregistré.", file.getAbsolutePath))
      true
    } catch {
      case e: Exception =>
        showSaveError(file, e)
        false
    }
  }

  private def showSaveError(file: File, e: Exception) {
    new ErrorMessageDialog(
      peer,
      "Impossible d'enregistrer",
      "Impossible d'écrire dans le fichier " + file.getAbsolutePath(),
      e).setVisible(true)
  }

  private def showLoadError(file: File, e: Exception) {
    new ErrorMessageDialog(
      peer,
      "Impossible de charger",
      "Impossible de lire le fichier " + file.getAbsolutePath(),
      e).setVisible(true)
  }

  private def updateStatus() {
    val f = if (currentFile == null) "" else currentFile.getName()
    val m = if (modified) "modifié" else ""
    statusBar.text = f + " " + m
  }

  def addEntry(previousWord: String, word: String, translations: String, explanation: String) {
    val translationArray = translations.split(STRING_SEPARATOR)
    dictionary.removeWord(previousWord, firstLanguageSelected)
    dictionary.removeWord(word, firstLanguageSelected)
    dictionary.addExplaination(word, explanation, firstLanguageSelected)
    for (translation <- translationArray) {
      if (firstLanguageSelected) {
        dictionary.addTranslation(word, translation)
      } else {
        dictionary.addTranslation(translation, word)
      }
      dictionary.addExplaination(translation, explanation, !firstLanguageSelected)
    }
    if (!listModel.contains(word))
      updateList()
  }

  private def setupFrame() {
    contents = new BorderPanel {
      val splitPane = new SplitPane(Orientation.Vertical)
      add(splitPane, BorderPanel.Position.Center)
      add(statusBar, BorderPanel.Position.South)
      splitPane.leftComponent = leftPanel
      splitPane.rightComponent = entryPanel
      entryPanel.setEnabled(false)

      SwingUtilities.invokeLater(new Runnable() {
        def run() {
          updateLanguageSelected()
        }
      })
    }
    setupListeners()
    setupMenu()
  }

  private def setupMenu() {
    import info.daviot.scala.swing.MenuCreation._
    saveAction.setEnabled(false)
    menuBar = bar(
      menu("File",
        item("Nouveau dictionnaire", newDictionaryClicked),
        item("Charger", loadClicked),
        item("Enregistrer", save),
        item("Enregistrer sous", saveAsClicked),
        item("Importer", importClicked),
        item("Exporter", exportClicked),
        item("Imprimer", {})),
      menu("Entrainement", item("Nouvel entrainement", runTraining)))
  }

  private def setupListeners() {
    searchField.reactions += {
      case ValueChanged(_) => Swing.onEDT(updateList)
    }
    wordsList.addListSelectionListener(new ListSelectionListener() {
      def valueChanged(e: ListSelectionEvent) {
        if (!e.getValueIsAdjusting())
          listSelectionChanged(getSelectedWord())
      }
    })
    newButton.reactions += { case ButtonClicked(_) => newButtonClicked() }
    helpButton.reactions += { case ButtonClicked(_) => showHelp() }
    deleteButton.reactions += { case ButtonClicked(_) => deleteButtonClicked }
    reactions += {
      case WindowClosing(_) =>
        if (checkAndSave("Sauver avant de quitter ?", "Attention"))
          dispose()
    }
    //    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
    //    addWindowListener(new WindowAdapter() {
    //      override def windowClosing(e: WindowEvent) {
    //      }
    //    })
  }

  private def listSelectionChanged(selectedWord: String) {
    if (selectedWord != null && selectedWord.length() > 0) {
      if (entryPanel.modified && lastSelectedWord != null
        && !selectedWord.equals(lastSelectedWord)) {
        val response = JOptionPane.showConfirmDialog(peer,
          "Les données ont été modifiées",
          "Conserver les modifications ?",
          JOptionPane.YES_NO_CANCEL_OPTION,
          JOptionPane.QUESTION_MESSAGE)
        response match {
          case JOptionPane.YES_OPTION =>
            entryPanel.okButtonClicked()

          case JOptionPane.NO_OPTION =>

          case _ =>
            wordsList.setSelectedValue(selectedWord, false)
        }
      }
      val entry = if (firstLanguageSelected) dictionary.getFirstLanguageEntry(selectedWord)
      else dictionary.getSecondLanguageEntry(selectedWord)
      entryPanel.setDictionaryEntry(selectedWord, entry)
      entryPanel.setEnabled(true)
    } else {
      entryPanel.clear()
      entryPanel.setEnabled(false)
    }
    lastSelectedWord = selectedWord
  }

  lazy val leftPanel = new BorderPanel {
    val topBox = new BoxPanel(Orientation.Horizontal) {
      contents += newButton
      contents += Swing.HStrut(10)
      contents += deleteButton
      contents += Swing.HStrut(10)
      contents += new Label("Rechercher ")
      contents += searchField
    }
    add(topBox, BorderPanel.Position.North)
    wordsList.setFont(DictionaryConstants.FONT)
    add(new ScrollPane(Component.wrap(wordsList)), BorderPanel.Position.Center)
    setSelector(Component.wrap(languageSelector))

    def setSelector(selector: Component) { add(selector, BorderPanel.Position.South) }
  }

  def deleteButtonClicked() {
    val selectedWord = getSelectedWord()
    if (firstLanguageSelected) {
      dictionary.removeFirstLanguageWord(selectedWord)
    } else {
      dictionary.removeSecondLanguageWord(selectedWord)
    }
    listModel.removeElement(selectedWord)
    setModified(true)
  }

  def newButtonClicked() {
    entryPanel.clear()
    entryPanel.setEnabled(true)
    entryPanel.requestFocus()
  }

  private def updateLanguageSelected() {
    val currentValue = languageSelector.getCurrentValue().asInstanceOf[String]
    val languageSelectionChanged = firstLanguageSelected != firstLanguageName.equals(currentValue)
    if (languageSelectionChanged) {
      firstLanguageSelected = !firstLanguageSelected
      updateList()
    }
    entryPanel.setLanguages(if (firstLanguageSelected) firstLanguageName else secondLanguageName,
      if (firstLanguageSelected) secondLanguageName else firstLanguageName)
  }

  private def updateList() {
    val listData = new ArrayList[String](dictionary.getSortedEntries(firstLanguageSelected))
    var searchFilter = searchField.text.trim()
    if (searchFilter.length() > 0) {
      searchFilter = ".*" + searchFilter + ".*"
      val i = listData.iterator()
      while (i.hasNext()) {
        val word = i.next()
        if (!word.matches(searchFilter))
          i.remove()
      }
    }
    listModel.removeAllElements()
    for (word <- listData) {
      listModel.addElement(word)
    }
    wordsList.setSelectedIndex(0)
  }

  private def getSelectedWord() = wordsList.getSelectedValue().asInstanceOf[String]

  val filter = new FileFilter() {
    override def accept(f: File) = f.isDirectory() || f.getName().endsWith(EXTENSION)
    override def getDescription() = "*." + EXTENSION
  }

  def setCurrentFile(currentFile: File) {
    this.currentFile = currentFile
    if (currentFile == null) {
      saveAction.setEnabled(false)
    } else {
      saveAction.setEnabled(true)
      updatePropertiesFile(currentFile)
    }
  }

  def newDictionaryClicked() {
    if (checkAndSave("Attention aux donnèes en cours", "Sauver les données en cours ?")) {
      val firstLanguage = JOptionPane.showInputDialog(peer, "Première langue ?", null, JOptionPane.INFORMATION_MESSAGE,
        null, Locale.getISOLanguages().asInstanceOf[Array[Object]], "fr").asInstanceOf[String]
      val secondLanguage = JOptionPane.showInputDialog(peer,
        "Deuxième langue ?", null, JOptionPane.INFORMATION_MESSAGE,
        null, Locale.getISOLanguages().asInstanceOf[Array[Object]], "en").asInstanceOf[String]
      dictionary = new TwoWayDictionary(firstLanguage, secondLanguage)
      update(null, dictionary)
    }
  }

  private def showHelp() {
    try {
      Desktop.getDesktop().browse(new URI(HELP_PAGE))
    } catch {
      case e: Exception =>
        new ErrorMessageDialog(peer, "Impossible d'ouvrir l'aide",
          "Impossible d'ouvrir l'adresse " + HELP_PAGE, e)
          .setVisible(true)
    }
  }

  def update(file: File, dictionary: TwoWayDictionary) {
    firstLanguageName = dictionary.firstLanguage
    secondLanguageName = dictionary.secondLanguage
    this.dictionary = dictionary
    setCurrentFile(file)
    updateList
    pack
    updateStatus
    visible = true
    updateTitle
    languageSelector = buildSelector
    leftPanel.setSelector(Component.wrap(languageSelector))
  }
}

object DictionaryFrame extends SimpleSwingApplication {
  val propertiesFileName = System.getProperty("user.home") + "/dict.properties"
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

  override val top = new DictionaryFrame
  top.pack
  top.load(new File(readPropertiesFile()))

  //  val frame = new DictionaryFrame("Français", "Español")
  // frame.addEntry("hola", "salut, bonjour", "Hola se�or")
  //  frame.pack()
  //  try {
  //    //    top.peer.load(new File(readPropertiesFile()))
  //  } catch {
  //    case e: IOException =>
  //      println("Premier démarrage, le fichier " + propertiesFileName + " n'existe pas encore")
  //    //      frame.setVisible(true)
  //  }

  def readPropertiesFile(): String = {
    val properties = new Properties()
    val os = new FileInputStream(propertiesFileName)
    properties.load(os)
    properties.getProperty("currentFile")
  }

  def updatePropertiesFile(currentFile: File) {
    val properties = new Properties()
    properties.put("currentFile", currentFile.getAbsolutePath())
    val os = new FileOutputStream(propertiesFileName)
    properties.store(os, "dictionary")
  }
}