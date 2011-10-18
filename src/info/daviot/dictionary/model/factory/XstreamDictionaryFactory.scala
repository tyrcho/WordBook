package info.daviot.dictionary.model.factory

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import com.thoughtworks.xstream.io.xml.DomDriver
import com.thoughtworks.xstream.XStream
import info.daviot.dictionary.model.TwoWayDictionary
import info.daviot.dictionary.DictionaryConstants
import info.daviot.dictionary.model.DictionaryEntry

class XstreamDictionaryFactory {
  val xstream = new XStream(new DomDriver())
  xstream.alias("dictionary", classOf[TwoWayDictionary])
  xstream.alias("entry", classOf[DictionaryEntry])

  def load(f: File) = {
    val reader = new InputStreamReader(new FileInputStream(f), DictionaryConstants.CHARSET)
    xstream.fromXML(reader).asInstanceOf[TwoWayDictionary]
  }

  def save(dictionary: TwoWayDictionary, f: File) {
    val writer = new OutputStreamWriter(new FileOutputStream(f), DictionaryConstants.CHARSET)
    xstream.toXML(dictionary, writer)
  }

}
