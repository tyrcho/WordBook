package info.daviot.gui.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

@SuppressWarnings("serial")
public abstract class ListModelProxy extends AbstractListModel {
    protected ListModel listModel;
    protected List<Integer> indexesMap;
    
    public int getSize() {
        return indexesMap.size();
    }

    public Object getElementAt(int arg) {
        return listModel.getElementAt(getIndex(arg));
    }

    public ListModelProxy(ListModel listModel) {
        this.listModel = listModel;
        initializeListener();
    }

    protected int getIndex(int arg){
        return indexesMap.get(arg);
    }

    protected void fireContentsChanged() {
        int size=getSize();
        updateIndex();
        fireContentsChanged(this,0,size - 1);
    }
    
    protected abstract void updateIndex();
    
    private void initializeListener(){
        listModel.addListDataListener(new ListDataListener(){
            public void intervalAdded(ListDataEvent arg0) {
                fireContentsChanged();
            }
            public void intervalRemoved(ListDataEvent arg0) {
                fireContentsChanged();
            }
            public void contentsChanged(ListDataEvent arg0) {
                fireContentsChanged();
            }
        });
        
    }
}
