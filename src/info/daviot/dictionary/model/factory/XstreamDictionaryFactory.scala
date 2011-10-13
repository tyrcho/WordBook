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

class XstreamDictionaryFactory extends DictionnaryFactory {

  var xstream = new XStream(new DomDriver());
  xstream.alias("dictionary", classOf[TwoWayDictionary]);
  xstream.alias("entry", classOf[DictionaryEntry]);

  def load() = {
    val reader = new InputStreamReader(new FileInputStream(fileName), DictionaryConstants.CHARSET);
    xstream.fromXML(reader).asInstanceOf[TwoWayDictionary];
  }

  def save(dictionary: TwoWayDictionary) {
    val writer = new OutputStreamWriter(new FileOutputStream(fileName), DictionaryConstants.CHARSET);
    xstream.toXML(dictionary, writer);
  }

}
