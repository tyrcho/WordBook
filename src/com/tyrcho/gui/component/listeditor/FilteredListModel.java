package com.tyrcho.gui.component.listeditor;

import java.util.ArrayList;

import javax.swing.ListModel;

import com.tyrcho.gui.misc.ListModelProxy;

@SuppressWarnings("serial")
public class FilteredListModel<T> extends ListModelProxy {
    Filter<T> filter;

    public FilteredListModel(ListModel listModel, Filter<T> filter) {
        super(listModel);
        this.filter = filter;
        updateIndex();
        filter.addFilterListener(new FilterListener() {
            public void filterModified() {
                fireContentsChanged();
            }
        });
    }

    protected void updateIndex() {
        // Initialisation du tableau d'index
        indexesMap = new ArrayList<Integer>();
        for (int i = 0; i < listModel.getSize(); i++) {
            if (filter.accepts(getIndexedObject(i))) {
                indexesMap.add(new Integer(i));
            }
        }
    }

    private T getIndexedObject(int index) {
        return (T) listModel.getElementAt(index);
    }

    public Filter<T> getFilter() {
        return filter;
    }

    public void setFilter(Filter<T> filter) {
        this.filter = filter;
    }

    public ListModel getListModel() {
        return listModel;
    }

    public void setListModel(ListModel listModel) {
        this.listModel = listModel;
    }
}
