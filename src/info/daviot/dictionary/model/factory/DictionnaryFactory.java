package info.daviot.dictionary.model.factory;

import info.daviot.dictionary.model.TwoWayDictionary;

public interface DictionnaryFactory {
    public TwoWayDictionary load() throws DictionnaryFactoryException ;
    
    public void setFileName(String filename);
    
    public void save(TwoWayDictionary dictionary) throws DictionnaryFactoryException;
}