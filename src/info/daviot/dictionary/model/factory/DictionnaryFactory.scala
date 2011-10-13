package info.daviot.dictionary.model.factory;

import info.daviot.dictionary.model.TwoWayDictionary;

trait DictionnaryFactory {
  def load(): TwoWayDictionary

  var fileName: String = _

  def save(dictionary: TwoWayDictionary)
}