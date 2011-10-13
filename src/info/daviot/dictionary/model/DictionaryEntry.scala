package info.daviot.dictionary.model

import info.daviot.dictionary.Utils

import java.util.Collection
import java.util.Date
import java.util.HashSet

import scala.collection.JavaConversions._

class DictionaryEntry {
  var translations = new HashSet[String]()
  var explaination: String = _
  var goodAnswers: Int = _
  var wrongAnswers: Int = _
  var rating: Float = _
  var creation: Date = _
  var modification: Date = _
  var lastGoodAnswer: Date = _

  def getRating() = if (rating < 0.0000001) getTempRating() else rating

  def getTempRating() = (1f + goodAnswers) / (1f + wrongAnswers)
  def getTotalAnswers() = wrongAnswers + goodAnswers

  /**
   * Gets the ratio in order to sort on it.
   *
   * @return the ratio, or -1 if only wrong answers, or 0 if no answer has
   *         been recorded
   */
  def getGoodAnswerProportion(): Float = {
    val total = getTotalAnswers()
    if (total == 0) {
      0
    } else if (goodAnswers == 0) {
      return -1
    } else {
      return (goodAnswers.floatValue()) / total
    }
  }

  def addTranslation(translation: String) { translations.add(translation); }
  def removeTranslation(translation: String) { translations.remove(translation); }

  def incrementGoodAnswers = goodAnswers += 1
  def incrementWrongAnswers = wrongAnswers += 1

  def firstTranslation() = translations.firstOption.getOrElse("")

  def displayRating() = format("(%.0f)", rating * 100)
}
