package info.daviot.gui.component.listeditor;

import info.daviot.gui.component.JScrollPane;
import info.daviot.gui.component.JTextField;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * A JList with a search field.
 * 
 * @author tyrcho
 */
@SuppressWarnings("serial")
public class SearchableList<T> extends JPanel {
    private JTextField       searchField = new JTextField();
    private JList            list        = new JList();
    private JButton          clearButton = new JButton("Effacer");
    private RegexpFilter<T>  filter;
    private TypeSafeListModel<T> listModel   = new TypeSafeListModel<T>();

    public SearchableList(List<T> elements, RegexpFilter<T> filter) {
        this.filter = filter;
        setElements(elements);
        init();
    }

    private void init() {
        list = new JList(new FilteredListModel<T>(listModel, filter));
        setLayout(new BorderLayout());
        Box searchBox = info.daviot.gui.toolkit.Box.createHorizontalBox();
        searchBox.add(searchField);
        searchBox.add(info.daviot.gui.toolkit.Box.createHorizontalGlue());
        searchBox.add(clearButton);
        add(searchBox, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
        initListeners();
    }

    public void addElement(T element) {
        listModel.addElement(element);
    }

    private void initListeners() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private Runnable updateListRunner = new Runnable() {
                                                  public void run() {
                                                      filter.setRegexp(".*" + searchField.getText() + ".*");
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

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.clear();
                searchField.requestFocus();
            }
        });

    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<String>();
        data.addAll(Collections.nCopies(1, "aaaa"));
        data.addAll(Collections.nCopies(1, "toto"));
        data.addAll(Collections.nCopies(1, "aze"));
        Collections.shuffle(data);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SearchableList<String> panel = new SearchableList<String>(data, new RegexpFilter<String>() {
            public boolean accepts(String object) {
                return object.matches(getRegexp());
            }
        });
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

    }

    public void deleteSelectedItems() {
        for (Object selectedItem : list.getSelectedValues()) {
            listModel.removeElement((T)selectedItem);
        }
    }

    public JList getList() {
        return list;
    }

    public void setElements(List<T> elements) {
        listModel.setElements(elements);
    }

    public void updateElement(int position, T element) {
        listModel.setElementAt(element, position);
    }

    public List<T> getElements() {
        return listModel.getElements();
    }
    
    private class TypeSafeListModel<T> extends AbstractListModel
    {
        private List<T> delegate = new ArrayList<T>();

        public void setElements(List<T> elements) {
            removeAllElements();
            delegate=new ArrayList<T>(elements);
            fireIntervalAdded(this, 0, elements.size());
        }
        public List<T> getElements() {
            return delegate;
        }
        /**
         * Returns the number of components in this list.
         * <p>
         * This method is identical to <code>size</code>, which implements the 
         * <code>List</code> interface defined in the 1.2 Collections framework.
         * This method exists in conjunction with <code>setSize</code> so that
         * <code>size</code> is identifiable as a JavaBean property.
         *
         * @return  the number of components in this list
         * @see #size()
         */
        public int getSize() {
        return delegate.size();
        }

        /**
         * Returns the component at the specified index.
         * <blockquote>
         * <b>Note:</b> Although this method is not deprecated, the preferred
         *    method to use is <code>get(int)</code>, which implements the 
         *    <code>List</code> interface defined in the 1.2 Collections framework.
         * </blockquote>
         * @param      index   an index into this list
         * @return     the component at the specified index
         * @exception  ArrayIndexOutOfBoundsException  if the <code>index</code> 
         *             is negative or greater than the current size of this 
         *             list
         * @see #get(int)
         */
        public T getElementAt(int index) {
        return delegate.get(index);
        }


        /**
         * Returns the number of components in this list.
         *
         * @return  the number of components in this list
         * @see Vector#size()
         */
        public int size() {
        return delegate.size();
        }

        /**
         * Tests whether this list has any components.
         *
         * @return  <code>true</code> if and only if this list has 
         *          no components, that is, its size is zero;
         *          <code>false</code> otherwise
         * @see Vector#isEmpty()
         */
        public boolean isEmpty() {
        return delegate.isEmpty();
        }

        /**
         * Tests whether the specified object is a component in this list.
         *
         * @param   elem   an object
         * @return  <code>true</code> if the specified object 
         *          is the same as a component in this list
         * @see Vector#contains(Object)
         */
        public boolean contains(Object elem) {
        return delegate.contains(elem);
        }

        /**
         * Searches for the first occurrence of <code>elem</code>.
         *
         * @param   elem   an object
         * @return  the index of the first occurrence of the argument in this
         *          list; returns <code>-1</code> if the object is not found
         * @see Vector#indexOf(Object)
         */
        public int indexOf(Object elem) {
        return delegate.indexOf(elem);
        }

        /**
         * Returns the index of the last occurrence of <code>elem</code>.
         *
         * @param   elem   the desired component
         * @return  the index of the last occurrence of <code>elem</code>
         *          in the list; returns <code>-1</code> if the object is not found
         * @see Vector#lastIndexOf(Object)
         */
        public int lastIndexOf(Object elem) {
        return delegate.lastIndexOf(elem);
        }

        /**
         * Returns the component at the specified index.
         * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
         * is negative or not less than the size of the list.
         * <blockquote>
         * <b>Note:</b> Although this method is not deprecated, the preferred
         *    method to use is <code>get(int)</code>, which implements the 
         *    <code>List</code> interface defined in the 1.2 Collections framework.
         * </blockquote>
         *
         * @param      index   an index into this list
         * @return     the component at the specified index
         * @see #get(int)
         * @see Vector#elementAt(int)
         */
        public T elementAt(int index) {
        return delegate.get(index);
        }

        /**
         * Sets the component at the specified <code>index</code> of this 
         * list to be the specified object. The previous component at that 
         * position is discarded.
         * <p>
         * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index 
         * is invalid.
         * <blockquote>
         * <b>Note:</b> Although this method is not deprecated, the preferred
         *    method to use is <code>set(int,Object)</code>, which implements the 
         *    <code>List</code> interface defined in the 1.2 Collections framework.
         * </blockquote>
         *
         * @param      obj     what the component is to be set to
         * @param      index   the specified index
         * @see #set(int,Object)
         * @see Vector#setElementAt(Object,int)
         */
        public void setElementAt(T obj, int index) {
        delegate.set(index, obj);
        fireContentsChanged(this, index, index);
        }

        /**
         * Deletes the component at the specified index.
         * <p>
         * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index 
         * is invalid.
         * <blockquote>
         * <b>Note:</b> Although this method is not deprecated, the preferred
         *    method to use is <code>remove(int)</code>, which implements the 
         *    <code>List</code> interface defined in the 1.2 Collections framework.
         * </blockquote>
         *
         * @param      index   the index of the object to remove
         * @see #remove(int)
         * @see Vector#removeElementAt(int)
         */
        public void removeElementAt(int index) {
        delegate.remove(index);
        fireIntervalRemoved(this, index, index);
        }

        /**
         * Inserts the specified object as a component in this list at the 
         * specified <code>index</code>.
         * <p>
         * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index 
         * is invalid.
         * <blockquote>
         * <b>Note:</b> Although this method is not deprecated, the preferred
         *    method to use is <code>add(int,Object)</code>, which implements the 
         *    <code>List</code> interface defined in the 1.2 Collections framework.
         * </blockquote>
         *
         * @param      obj     the component to insert
         * @param      index   where to insert the new component
         * @exception  ArrayIndexOutOfBoundsException  if the index was invalid
         * @see #add(int,Object)
         * @see Vector#insertElementAt(Object,int)
         */
        public void insertElementAt(T obj, int index) {
        delegate.add(index, obj);
        fireIntervalAdded(this, index, index);
        }

        /**
         * Adds the specified component to the end of this list. 
         *
         * @param   obj   the component to be added
         * @see Vector#addElement(Object)
         */
        public void addElement(T obj) {
        int index = delegate.size();
        delegate.add(obj);
        fireIntervalAdded(this, index, index);
        }

        /**
         * Removes the first (lowest-indexed) occurrence of the argument 
         * from this list.
         *
         * @param   obj   the component to be removed
         * @return  <code>true</code> if the argument was a component of this
         *          list; <code>false</code> otherwise
         * @see Vector#removeElement(Object)
         */
        public boolean removeElement(T obj) {
        int index = indexOf(obj);
        boolean rv = delegate.remove(obj);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
        }


        /**
         * Removes all components from this list and sets its size to zero.
         * <blockquote>
         * <b>Note:</b> Although this method is not deprecated, the preferred
         *    method to use is <code>clear</code>, which implements the 
         *    <code>List</code> interface defined in the 1.2 Collections framework.
         * </blockquote>
         *
         * @see #clear()
         * @see Vector#removeAllElements()
         */
        public void removeAllElements() {
        int index1 = delegate.size()-1;
        delegate.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
        }


        /**
         * Returns a string that displays and identifies this
         * object's properties.
         *
         * @return a String representation of this object
         */
       public String toString() {
        return delegate.toString();
        }


        
    }
    
    
}
