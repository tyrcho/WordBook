package info.daviot.gui.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class SortedListModel extends AbstractListModel {
    ListModel unsortedListModel;
    List indexesMap;
    
    public int getSize() {
        return unsortedListModel.getSize();
    }

    public Object getElementAt(int arg) {
        return unsortedListModel.getElementAt(this.getIndex(arg));
    }

    public SortedListModel(ListModel unsortedListModel) {
        this.unsortedListModel = unsortedListModel;
        this.updateIndex();
        this.initializeListener();
    }

    private int getIndex(int arg){
        Integer index = (Integer)indexesMap.get(arg);
        return index.intValue();
    }
    
    private Comparable getIndexedObject (Object index){
        return (Comparable)unsortedListModel.getElementAt(((Integer)index).intValue());
    }
    
    private void updateIndex(){
        //Initialisation du tableau d'index
        indexesMap = new ArrayList();
        for (int i = 0 ; i < getSize() ; i++) {
            indexesMap.add(i,new Integer(i));
        }
        Collections.sort(indexesMap,new Comparator(){
            public int compare(Object arg0, Object arg1) {
                return getIndexedObject(arg0).compareTo(getIndexedObject(arg1));
            }
        });
    }
    
    private void initializeListener(){
        final Object eventSource=this;
        unsortedListModel.addListDataListener(new ListDataListener(){
            public void intervalAdded(ListDataEvent arg0) {
                updateIndex();
                fireIntervalAdded(eventSource,arg0.getIndex0(),arg0.getIndex1());
            }
            public void intervalRemoved(ListDataEvent arg0) {
                fireIntervalRemoved(eventSource,getIndex(arg0.getIndex0()),getIndex(arg0.getIndex1()));
                updateIndex();
            }
            public void contentsChanged(ListDataEvent arg0) {
                updateIndex();
                fireContentsChanged(eventSource,0,getSize() - 1);
            }
        });
        
    }
}
