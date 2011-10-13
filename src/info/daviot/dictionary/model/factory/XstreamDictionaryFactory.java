package info.daviot.dictionary.model.factory;

import info.daviot.dictionary.DictionaryConstants;
import info.daviot.dictionary.model.DictionaryEntry;
import info.daviot.dictionary.model.TwoWayDictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XstreamDictionaryFactory implements DictionnaryFactory {
	private XStream xstream;

	private String fileName;

	// Initialisation du flux XML
	private void setupXStream() {
		if (xstream == null) {
			xstream = new XStream(new DomDriver());
			xstream.alias("dictionary", TwoWayDictionary.class);
			xstream.alias("entry", DictionaryEntry.class);
		}
	}

	// Renvoie un objet TwoWayDictionary � partir d'un fichier XML
	public TwoWayDictionary load() throws DictionnaryFactoryException {
		try {
			Reader reader = new InputStreamReader(
					new FileInputStream(fileName), DictionaryConstants.CHARSET);
			setupXStream();
			return (TwoWayDictionary) xstream.fromXML(reader);
		} catch (FileNotFoundException e) {
			throw new DictionnaryFactoryException(e);
		} catch (RuntimeException e) {
			throw new DictionnaryFactoryException(e);
		}
	}

	public XstreamDictionaryFactory() {
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void save(TwoWayDictionary dictionary)
			throws DictionnaryFactoryException {
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(fileName),
					DictionaryConstants.CHARSET);
			setupXStream();
			xstream.toXML(dictionary, writer);
		} catch (FileNotFoundException e) {
			throw new DictionnaryFactoryException(e);
		} catch (RuntimeException e) {
			throw new DictionnaryFactoryException(e);
		}

	}

}
