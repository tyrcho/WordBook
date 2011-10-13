package info.daviot.gui.component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import com.sun.java.util.SpringUtilities;

@SuppressWarnings("serial")
public class SelectCategoriesPanel<T> extends JPanel {
    private SortedSet<? extends T>                 categories = new TreeSet<T>();
    private SortedMap<Integer, CategoryPanel> panels     = new TreeMap<Integer, CategoryPanel>();
    private int                               maxId;

    public SelectCategoriesPanel() {
        super(new SpringLayout());
        addCategoryPanel();
    }
    
    public void setEditable(boolean editable) {
        for (CategoryPanel panel : panels.values()) {
            panel.setEditable(editable);
        }
    }

    public void setAvailableCategories(Collection<? extends T> categories) {
        this.categories = new TreeSet<T>(categories);
        ComboBoxModel model = new DefaultComboBoxModel(categories.toArray());
        for (CategoryPanel panel : panels.values()) {
            panel.comboBox.setModel(model);
        }
    }

    public void clear() {
        panels=new TreeMap<Integer, CategoryPanel>();
        addCategoryPanel();
    }

    public void setChosenCategories(List<? extends T> categories) {
        clear();
        for (T category : categories) {
            JComboBox lastComboBox = panels.get(panels.lastKey()).comboBox;
            lastComboBox.addItem(category);
            lastComboBox.setSelectedItem(category);
        }
    }

    public List<T> getChosenCategories() {
        List<T> chosenCategories = new ArrayList<T>();
        for (CategoryPanel panel : panels.values()) {
            T choice = panel.getChoice();
            if (!"".equals(choice)) {
                chosenCategories.add(choice);
            }
        }
        return chosenCategories;
    }

    private void addPanel(CategoryPanel panel) {
        panels.put(panel.id, panel);
        add(panel);
    }

    private void removePanel(int id) {
        if (id == panels.lastKey()) {
            CategoryPanel panel = panels.get(id);
            panel.comboBox.setSelectedIndex(0);
        } else {
            CategoryPanel panel = panels.remove(id);
            panel.setDeleteAction(null);
            panel.setValueChosenAction(null);
        }
    }

    private void addCategoryPanel() {
        CategoryPanel panel = new CategoryPanel();
        final int id = panel.id;
        addPanel(panel);
        panel.setDeleteAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                removePanel(id);
                updateDisplay();
            }

        });
        panel.setValueChosenAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (id == panels.lastKey()) {
                    addCategoryPanel();
                }
            }
        });
        updateDisplay();
        panel.requestFocus();
    }

    private void updateLayout() {
        removeAll();
        for (CategoryPanel panel: panels.values()) {
            add(panel);
        }
        SpringUtilities.makeCompactGrid(this, panels.size(), 1);
    }

    public void updateDisplay() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                updateLayout();
                revalidate();
                repaint();
            }
        });
    }

    private class CategoryPanel extends JPanel {
        JComboBox      comboBox;
        JButton        deleteButton;
        private int    id;
        
        public void setEditable(boolean editable) {
            comboBox.setEnabled(editable);
            deleteButton.setEnabled(editable);            
        }

        CategoryPanel() {
            super(new SpringLayout());
            id = maxId++;
            System.out.println("creating " + id);
            Vector<T> elements = new Vector<T>(categories);
            comboBox = new JComboBox(elements);
            comboBox.insertItemAt("", 0);
            comboBox.setSelectedIndex(0);
            comboBox.setEditable(true);
            comboBox.setMaximumSize(new Dimension(200,15));
            deleteButton = new JButton("X");
            add(comboBox);
            add(deleteButton);
            SpringUtilities.makeCompactGrid(this, 1, 2);
        }

        T getChoice() {
            return (T) comboBox.getSelectedItem();
        }
        
        @Override
        public void requestFocus() {
            comboBox.requestFocus();
        }

        void setDeleteAction(Action a) {
            deleteButton.setAction(a);
            deleteButton.setText("X");
        }

        void setValueChosenAction(Action a) {
            comboBox.setAction(a);
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalizing " + id);
            super.finalize();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(new SelectCategoriesPanel()));
        frame.pack();
        frame.setVisible(true);
    }
}
