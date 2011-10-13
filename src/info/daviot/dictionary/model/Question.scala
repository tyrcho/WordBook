package info.daviot.dictionary.model;

import info.daviot.dictionary.Utils

import scala.collection.JavaConversions._

class Question(val word: String, val dictionaryEntry: DictionaryEntry, ignoredChars: String) {
  var inputTranslation: String = null

  override def toString() = "Donnez la traduction de : " + word

  def isAnswerValid() = dictionaryEntry.translations.exists(Utils.simpleCompare(_, inputTranslation, ignoredChars))

  def getTranslation() = dictionaryEntry.translations.toString()
}
