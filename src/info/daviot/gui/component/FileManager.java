package info.daviot.gui.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * Allows to save/load a file and get the current modified state.
 * 
 * @author tyrcho
 */
public class FileManager {
    private File                     currentFile;

    private boolean                  modified;
    private JLabel                   statusBar    = new JLabel();

    @SuppressWarnings("serial")
    private Action                   saveAction   = new AbstractAction("Enregistrer") {
                                                      public void actionPerformed(ActionEvent e) {
                                                          saveClicked();
                                                      }
                                                  };

    @SuppressWarnings("serial")
    private Action                   saveAsAction = new AbstractAction("Enregistrer sous") {
                                                      public void actionPerformed(ActionEvent e) {
                                                          saveAsClicked();
                                                      }
                                                  };

    @SuppressWarnings("serial")
    private Action                   loadAction   = new AbstractAction("Charger") {
                                                      public void actionPerformed(ActionEvent e) {
                                                          loadClicked();
                                                      }
                                                  };

    private Action                   newAction    = new AbstractAction("Nouveau") {
                                                      public void actionPerformed(ActionEvent e) {
                                                          newClicked();
                                                      }
                                                  };

    private FileFilter               filter       = new FileFilter() {
                                                      public boolean accept(File f) {
                                                          return f.getName().endsWith(extension) || f.isDirectory();
                                                      }

                                                      public String getDescription() {
                                                          return "*." + extension;
                                                      }
                                                  };

    private Component                parent;

    private String                   extension;

    private final FileActionListener fileActionListener;

    public FileManager(Component parent, String extension, FileActionListener fileActionListener) {
        this.parent = parent;
        this.extension = extension;
        this.fileActionListener = fileActionListener;
        saveAction.setEnabled(false);
    }

    private void newClicked() {
        if (!isSaved("Attention aux donn�es en cours", "Sauver les donn�es en cours ?")) return;
        fileActionListener.newFile();
        setCurrentFile(null);
        setModified(false);        
    }

    private void loadClicked() {
        if (!isSaved("Attention aux donn�es en cours", "Sauver les donn�es en cours ?")) return;
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(parent)) {
            File file = fileChooser.getSelectedFile();
            fileActionListener.load(file);
            setCurrentFile(file);
            setModified(false);
        }
    }

    private boolean saveAsClicked() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(parent)) {
            File file = fileChooser.getSelectedFile();
            if (!(file.getName().indexOf('.') > 0)) {
                file = new File(file.getAbsolutePath() + "." + extension);
            }
            if (!file.exists() || JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(parent, "Ecraser le fichier " + file.getAbsolutePath(), "Ecraser ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) { 
                boolean saved = fileActionListener.save(file);
                if (saved) {
                    setCurrentFile(file);
                    JOptionPane.showMessageDialog(parent, "Le fichier "+currentFile+" a �t� enregistr�.");
                    setModified(false);
                }
                return saved;
            }
        }
        return false;
    }

    private boolean saveClicked() {
        boolean saved = fileActionListener.save(currentFile);
        if (saved) {
            JOptionPane.showMessageDialog(parent, "Le fichier "+currentFile+" a �t� enregistr�.");
            setModified(false);
        }
        return saved;
    }

    private void updateStatus() {
        statusBar.setText((currentFile == null ? "" : currentFile.getName()) + " " + (isModified() ? "modifi�" : ""));
    }

    private boolean isSaved(String title, String message) {
        if (!modified) return true;
        int response = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
        updateStatus();
    }

    public Component getParent() {
        return parent;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public String getExtension() {
        return extension;
    }

    public Action getLoadAction() {
        return loadAction;
    }

    public Action getSaveAction() {
        return saveAction;
    }

    public Action getSaveAsAction() {
        return saveAsAction;
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        saveAction.setEnabled(currentFile!=null);
        updateStatus();
    }

    public Action getNewAction() {
        return newAction;
    }
}
