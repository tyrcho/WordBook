package info.daviot.dictionary.view;

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
import java.awt.Desktop;
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

import javax.swing.AbstractAction;
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

/**
 * @author ALEXIS
 */
public class DictionaryFrame extends JFrame {
	private static final String HELP_PAGE = "https://sites.google.com/site/micheldaviot/hobbies/apprentissage-du-chinois/logiciel";

	public static final String STRING_SEPARATOR = ",( )*";

	public static final String EXTENSION = "dict";

	public static final String propertiesFileName = System
			.getProperty("user.home") + "/dict.properties";

	private String firstLanguageName;

	private String secondLanguageName;

	private TwoWayDictionary dictionary;

	private DefaultListModel listModel = new DefaultListModel();

	private WordsListDisplay wordsList = new WordsDisplayAsList(listModel);

	private JButton newButton = new JButton("Nouveau");
	private JButton helpButton = new JButton("Aide");

	private JButton deleteButton = new JButton("Supprimer");

	private JTextField searchField = new JTextField(10);

	private RadioButtonGroup languageSelector;

	private boolean firstLanguageSelected;

	private EntryPanel entryPanel = new EntryPanel(this);

	private File currentFile;

	private boolean modified;

	private String lastSelectedWord;

	private Action saveAction = new AbstractAction("Enregistrer") {
		public void actionPerformed(ActionEvent e) {
			saveClicked();
		}
	};

	private Action exportAction = new AbstractAction("Exporter") {
		public void actionPerformed(ActionEvent e) {
			exportClicked();
		}
	};

	private Action trainingAction = new AbstractAction("Entrainement") {
		public void actionPerformed(ActionEvent e) {
			runTraining();
		}
	};

	private Action printAction = new AbstractAction("Imprimer") {
		public void actionPerformed(ActionEvent e) {
			print();
		}
	};

	private Action saveAsAction = new AbstractAction("Enregistrer sous") {
		public void actionPerformed(ActionEvent e) {
			saveAsClicked();
		}
	};

	private Action loadAction = new AbstractAction("Charger") {
		public void actionPerformed(ActionEvent e) {
			loadClicked();
		}
	};

	private Action helpAction = new AbstractAction("Aide") {
		public void actionPerformed(ActionEvent e) {
			showHelp();
		}
	};

	private Action importAction = new AbstractAction("Importer") {
		public void actionPerformed(ActionEvent e) {
			importClicked();
		}
	};

	private Action newDictionaryAction = new AbstractAction(
			"Nouveau dictionnaire") {
		public void actionPerformed(ActionEvent e) {
			newDictionaryClicked();
		}
	};

	private JLabel statusBar = new JLabel();

	private DictionnaryFactory factory = new XstreamDictionaryFactory();

	private String ignoredChars;

	public DictionaryFrame(String firstLanguageName, String secondLanguageName) {
		dictionary = new TwoWayDictionary(firstLanguageName, secondLanguageName);
		this.firstLanguageName = firstLanguageName;
		this.secondLanguageName = secondLanguageName;
		updateTitle();
		setupFrame();
	}

	private void runTraining() {
		SessionParameters parameters = SessionParametersPanel
				.showSessionParametersDialog(this,
						"Choisir la configuration de l'exercice", dictionary);
		if (parameters != null) {
			parameters.ignoredChars_$eq(ignoredChars);
			Session session = new Session(parameters);
			runSession(session);
			setModified(true);
		}
	}

	private void print() {
		Boolean firstLanguage = popupChooseLanguage();
		if (firstLanguage != null) {
			try {
				JasperReport report = JasperCompileManager
						.compileReport(getClass().getClassLoader()
								.getResourceAsStream("dictionnaire.jrxml"));
				Collection<Map<String, String>> data = new ArrayList<Map<String, String>>();
				List<String> entries = dictionary
						.getSortedEntries(firstLanguage);
				for (String word : entries) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("a", word);
					DictionaryEntry entry = dictionary.getEntry(word,
							firstLanguage);
					Collection<String> translations = entry.translations();
					StringBuffer buffer = new StringBuffer();
					for (Iterator<String> i = translations.iterator(); i
							.hasNext();) {
						buffer.append(i.next());
						if (i.hasNext()) {
							buffer.append(", ");
						}
					}
					map.put("b", buffer.toString());
					map.put("c", entry.explaination());
					data.add(map);
				}
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("LANGAGE1", firstLanguage ? firstLanguageName
						: secondLanguageName);
				parameters.put("LANGAGE2", firstLanguage ? secondLanguageName
						: firstLanguageName);
				JasperPrint print = JasperFillManager.fillReport(report,
						parameters, new JRMapCollectionDataSource(data));
				JasperViewer.viewReport(print, false);
			} catch (JRException e) {
				JOptionPane.showMessageDialog(this,
						"Erreur Jasper Print" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private Boolean popupChooseLanguage() {
		String[] choices = new String[] { firstLanguageName, secondLanguageName };
		Object choice = JOptionPane.showInputDialog(this,
				"Choisir le langage de tri", "", JOptionPane.QUESTION_MESSAGE,
				null, choices, choices[0]);
		boolean firstLanguage;
		if (choice == null) {
			return null;
		} else {
			firstLanguage = choice == choices[0];
		}
		return firstLanguage;
	}

	private void runSession(final Session session) {
		final VocabularyTestFrame frame = new VocabularyTestFrame(
				"Entrainement", session);
		// final JFrame mainFrame=this;
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addSessionCompleteListener(new SessionCompleteListener() {
			public void sessionComplete(final SessionCompleteEvent e) {
				DictionaryFrame.this.sessionComplete(e.score(), session, frame);
			}
		});
		frame.runSession();
	}

	private void sessionComplete(final String score, final Session session,
			final VocabularyTestFrame frame) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				// String otherChangeLanguage= "Autre exercice
				// ("+(session.firstLanguage?secondLanguageName:firstLanguageName)+")";
				String[] options = { "Recommencer (m�mes mots)",
						"Autre exercice", "Changer de langue ", "Retour",
						"M�me type d'exercice" };
				String message = "Votre score est de " + score + ".\n "
						+ frame.getErrors() + "\nEt maintenant ?";
				int choice = JOptionPane
						.showOptionDialog(frame, message, "Session termin�e",
								JOptionPane.CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[4]);
				frame.dispose();
				switch (choice) {
				case 3:
				default:
					break;
				case 0:
					session.resetScore();
					runSession(session);
					break;
				case 1:
					runTraining();
					break;
				case 2:
					session.switchLanguage();
					session.resetScore();
					runSession(session);
					break;
				case 4:
					session.resetQuestions();
					session.resetScore();
					runSession(session);
					break;
				}
			}
		});

	}

	private void updateTitle() {
		setTitle("Dictionnaire " + firstLanguageName + "-" + secondLanguageName);
	}

	private void loadClicked() {
		if (!isSaved("Attention aux donn�es en cours",
				"Sauver les donn�es en cours ?"))
			return;
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(this)) {
			File file = fileChooser.getSelectedFile();
			load(file);
		}
	}

	private void importClicked() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(this)) {
			File file = fileChooser.getSelectedFile();
			importFile(file);
		}
	}

	private void updateFrame(File file, TwoWayDictionary dictionary) {
		DictionaryFrame frame = new DictionaryFrame(dictionary.firstLanguage(),
				dictionary.secondLanguage());
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

	private void load(File file) {
		try {
			factory.fileName_$eq(file.getAbsolutePath());
			dictionary = factory.load();
			updateFrame(file, dictionary);
		} catch (RuntimeException e) {
			new ErrorMessageDialog(this, "Fichier non valide",
					"Ce fichier n'a pas pu �tre lu " + file.getAbsolutePath(),
					e).setVisible(true);
			updateFrame(file, new TwoWayDictionary("", ""));
		} catch (Exception e) {
			new ErrorMessageDialog(this, "Fichier non valide",
					"Ce fichier n'a pas pu �tre lu " + file.getAbsolutePath(),
					e).setVisible(true);
			updateFrame(file, new TwoWayDictionary("", ""));
		}
	}

	private void importFile(File file) {
		try {
			factory.fileName_$eq(file.getAbsolutePath());
			TwoWayDictionary imported = factory.load();
			modified = true;
			dictionary.addAll(imported);
			updateFrame(currentFile, dictionary);
		} catch (RuntimeException e) {
			new ErrorMessageDialog(this, "Fichier non valide",
					"Ce fichier n'a pas pu �tre lu " + file.getAbsolutePath(),
					e).setVisible(true);
		} catch (Exception e) {
			new ErrorMessageDialog(this, "Fichier non valide",
					"Ce fichier n'a pas pu �tre lu " + file.getAbsolutePath(),
					e).setVisible(true);
		}
	}

	private void exportClicked() {
		Boolean firstLanguage = popupChooseLanguage();
		if (firstLanguage != null) {
			final JFileChooser fileChooser = new JFileChooser();
			if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
				File file = fileChooser.getSelectedFile();
				if (!(file.getName().indexOf('.') > 0)) {
					file = new File(file.getAbsolutePath() + ".csv");
				}
				if (!file.exists()
						|| JOptionPane.YES_OPTION == JOptionPane
								.showConfirmDialog(this, "Ecraser le fichier "
										+ file.getAbsolutePath(), "Ecraser ?",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE)) {
					export(file, firstLanguage);
				}
			}
		}
	}

	private void export(File file, boolean firstLanguage) {
		try {
			Writer fileWriter = new OutputStreamWriter(new FileOutputStream(
					file), Charset.forName("UTF-8"));
			for (String e : dictionary.getSortedEntries(firstLanguage)) {
				fileWriter.append(e + ";");
				DictionaryEntry entry = dictionary.getEntry(e, firstLanguage);
				for (Iterator<String> i = entry.translations().iterator(); i
						.hasNext();) {
					fileWriter.append(i.next());
					if (i.hasNext()) {
						fileWriter.append(",");
					}
				}
				fileWriter.append(String.format(";%s;%s/%s%n", entry
						.explaination().replaceAll("\r|\n", ""), entry
						.goodAnswers(), entry.getTotalAnswers()));
			}
			fileWriter.close();
		} catch (IOException e) {
			showSaveError(file, e);
		}
	}

	private boolean saveAsClicked() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
			File file = fileChooser.getSelectedFile();
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

	private boolean saveClicked() {
		return save(currentFile);
	}

	private boolean save(File file) {
		try {
			factory.fileName_$eq(file.getAbsolutePath());
			factory.save(dictionary);
			saveAction.setEnabled(true);
			setModified(false);
			updateStatus();
			JOptionPane.showMessageDialog(this,
					"Le fichier " + file.getAbsolutePath()
							+ " a �t� enregistr�.");
			return true;
		} catch (Exception e) {
			showSaveError(file, e);
			return false;
		}
	}

	private void showSaveError(File file, Exception e) {
		new ErrorMessageDialog(
				this,
				"Impossible d'enregistrer",
				"Impossible d'�crire dans le fichier " + file.getAbsolutePath(),
				e).setVisible(true);
	}

	private void updateStatus() {
		statusBar.setText((currentFile == null ? "" : currentFile.getName())
				+ " " + (isModified() ? "modifi�" : ""));
	}

	private boolean isSaved(String title, String message) {
		if (!modified)
			return true;
		int response = JOptionPane.showConfirmDialog(this, message, title,
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		switch (response) {
		case JOptionPane.YES_OPTION:
			if (currentFile == null) {
				return saveAsClicked();
			} else {
				return saveClicked();
			}

		case JOptionPane.NO_OPTION:
			return true;

		case JOptionPane.CANCEL_OPTION:
		default:
			return false;
		}
	}

	public void addEntry(String previousWord, String word, String translations,
			String explanation) {
		String[] translationArray = translations.split(STRING_SEPARATOR);
		dictionary.removeWord(previousWord, firstLanguageSelected);
		dictionary.removeWord(word, firstLanguageSelected);
		dictionary.addExplaination(word, explanation, firstLanguageSelected);
		for (String translation : translationArray) {
			if (firstLanguageSelected) {
				dictionary.addTranslation(word, translation);
			} else {
				dictionary.addTranslation(translation, word);
			}
			dictionary.addExplaination(translation, explanation,
					!firstLanguageSelected);
		}
		if (!listModel.contains(word))
			updateList();
	}

	private void setupFrame() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		contentPane.add(statusBar, BorderLayout.SOUTH);
		splitPane.setLeftComponent(buildLeftPanel());
		splitPane.setRightComponent(entryPanel);
		entryPanel.setEnabled(false);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateLanguageSelected();
			}
		});
		setupListeners();
		languageSelector.setCurrentValue(secondLanguageName);
		setupMenu();
	}

	private void setupMenu() {
		JMenuBar menuBar = new JMenuBar();
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

	private void setupListeners() {
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			private Runnable updateListRunner = new Runnable() {
				public void run() {
					updateList();
				}
			};

			public void insertUpdate(DocumentEvent e) {
				SwingUtilities.invokeLater(updateListRunner);
			}

			public void removeUpdate(DocumentEvent e) {
				SwingUtilities.invokeLater(updateListRunner);
			}

			public void changedUpdate(DocumentEvent e) {
				SwingUtilities.invokeLater(updateListRunner);
			}
		});
		wordsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting())
					listSelectionChanged(getSelectedWord());
			}
		});
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newButtonClicked();
			}
		});
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHelp();
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteButtonClicked();
			}
		});
		languageSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						updateLanguageSelected();
					}
				});
			}
		});
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isSaved("Confirmer la sortie", "Sauver avant de quitter ?"))
					dispose();
			}
		});
	}

	private void listSelectionChanged(String selectedWord) {
		if (selectedWord != null && selectedWord.length() > 0) {
			if (entryPanel.modified() && lastSelectedWord != null
					&& !selectedWord.equals(lastSelectedWord)) {
				int response = JOptionPane.showConfirmDialog(this,
						"Les donn�es ont �t� modifi�es",
						"Conserver les modifications ?",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				switch (response) {
				case JOptionPane.YES_OPTION:
					entryPanel.okButtonClicked();
					break;

				case JOptionPane.NO_OPTION:
					break;

				case JOptionPane.CANCEL_OPTION:
				default:
					wordsList.setSelectedValue(selectedWord, false);
					return;
				}
			}
			DictionaryEntry entry = firstLanguageSelected ? dictionary
					.getFirstLanguageEntry(selectedWord) : dictionary
					.getSecondLanguageEntry(selectedWord);
			entryPanel.setDictionaryEntry(selectedWord, entry);
			entryPanel.setEnabled(true);
		} else {
			entryPanel.clear();
			entryPanel.setEnabled(false);
		}
		lastSelectedWord = selectedWord;
	}

	private Component buildLeftPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		Box topBox = Box.createHorizontalBox();
		topBox.add(newButton);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(deleteButton);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(new JLabel("Rechercher "));
		topBox.add(searchField);
		panel.add(topBox, BorderLayout.NORTH);
		wordsList.setFont(DictionaryConstants.FONT());
		panel.add(new JScrollPane((Component) wordsList), BorderLayout.CENTER);
		Map<String, String> languages = new HashMap<String, String>();
		languages.put(firstLanguageName, firstLanguageName);
		languages.put(secondLanguageName, secondLanguageName);
		languageSelector = new RadioButtonGroup(RadioButtonGroup.HORIZONTAL,
				languages);
		panel.add(languageSelector, BorderLayout.SOUTH);
		return panel;
	}

	protected void deleteButtonClicked() {
		String selectedWord = getSelectedWord();
		if (firstLanguageSelected) {
			dictionary.removeFirstLanguageWord(selectedWord);
		} else {
			dictionary.removeSecondLanguageWord(selectedWord);
		}
		listModel.removeElement(selectedWord);
		setModified(true);
	}

	protected void newButtonClicked() {
		entryPanel.clear();
		entryPanel.setEnabled(true);
		entryPanel.requestFocus();
	}

	private void updateLanguageSelected() {
		String currentValue = (String) languageSelector.getCurrentValue();
		boolean languageSelectionChanged = firstLanguageSelected != firstLanguageName
				.equals(currentValue);
		if (languageSelectionChanged) {
			firstLanguageSelected = !firstLanguageSelected;
			updateList();
		}
		entryPanel.setLanguages(firstLanguageSelected ? firstLanguageName
				: secondLanguageName,
				firstLanguageSelected ? secondLanguageName : firstLanguageName);
	}

	private void updateList() {
		List<String> listData = new ArrayList<String>(
				dictionary.getSortedEntries(firstLanguageSelected));
		String searchFilter = searchField.getText().trim();
		if (searchFilter.length() > 0) {
			searchFilter = ".*" + searchFilter + ".*";
			for (Iterator<String> i = listData.iterator(); i.hasNext();) {
				String word = i.next();
				if (!word.matches(searchFilter))
					i.remove();
			}
		}
		listModel.removeAllElements();
		for (String word : listData) {
			listModel.addElement(word);
		}
		wordsList.setSelectedIndex(0);
	}

	private String getSelectedWord() {
		return (String) wordsList.getSelectedValue();
	}

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		DictionaryFrame frame = new DictionaryFrame("Français", "Español");
		// frame.addEntry("hola", "salut, bonjour", "Hola se�or");
		frame.pack();
		try {
			frame.load(new File(readPropertiesFile()));
		} catch (IOException e) {
			System.out.println("Premier démarrage, le fichier "
					+ propertiesFileName + " n'existe pas encore");
			frame.setVisible(true);
		}
	}

	/**
	 * @return Returns the modified.
	 * @uml.property name="modified"
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified
	 *            The modified to set.
	 * @uml.property name="modified"
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
		updateStatus();
	}

	private static FileFilter filter = new FileFilter() {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().endsWith(EXTENSION);
		}

		@Override
		public String getDescription() {
			return "*." + EXTENSION;
		}

	};

	public File getCurrentFile() {
		return currentFile;
	}

	public static void updatePropertiesFile(File currentFile) {
		Properties properties = new Properties();
		properties.put("currentFile", currentFile.getAbsolutePath());
		try {
			FileOutputStream os = new FileOutputStream(propertiesFileName);
			properties.store(os, "dictionary");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readPropertiesFile() throws IOException {
		Properties properties = new Properties();
		FileInputStream os = new FileInputStream(propertiesFileName);
		properties.load(os);
		return properties.getProperty("currentFile");
	}

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
		if (currentFile == null) {
			saveAction.setEnabled(false);
		} else {
			saveAction.setEnabled(true);
			updatePropertiesFile(currentFile);
		}
	}

	public void newDictionaryClicked() {
		if (!isSaved("Attention aux donn�es en cours",
				"Sauver les donn�es en cours ?"))
			return;

		String firstLanguage = (String) JOptionPane.showInputDialog(this,
				"Premi�re langue ?", null, JOptionPane.INFORMATION_MESSAGE,
				null, Locale.getISOLanguages(), "fr");
		String secondLanguage = (String) JOptionPane.showInputDialog(this,
				"Deuxi�me langue ?", null, JOptionPane.INFORMATION_MESSAGE,
				null, Locale.getISOLanguages(), "en");
		dictionary = new TwoWayDictionary(firstLanguage, secondLanguage);
		updateFrame(null, dictionary);
	}

	private void showHelp() {
		try {
			Desktop.getDesktop().browse(new URI(HELP_PAGE));
		} catch (Exception e) {
			new ErrorMessageDialog(this, "Impossible d'ouvrir l'aide",
					"Impossible d'ouvrir l'adresse " + HELP_PAGE, e)
					.setVisible(true);
		}
	}
}
