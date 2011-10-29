package info.daviot.dictionary.model

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.HashSet
import java.util.Iterator
import java.util.List
import java.util.Random
import java.util.Set
import scala.collection.JavaConversions._

class Session(val parameters: SessionParameters) {
  resetQuestions()

  var score: Int = _
  var questionCount: Int = _

  var entries: List[String] = _

  def resetQuestions() {
    val dictEntries = parameters.dictionnary.getEntries(parameters.firstLanguage)
    questionCount = Math.min(dictEntries.size(), parameters.questionCount)
    entries = new ArrayList[String](questionCount)
    val random = new ArrayList[String](dictEntries)
    Collections.shuffle(random)
    val badRatio = new ArrayList[String](dictEntries)
    Collections.sort(badRatio, buildRatingComparator())
    val rand = new Random()
    for (i <- 0 until questionCount) {
      val r = rand.nextInt(100)
      val list = if (r < parameters.randomPercent) random else badRatio
      addEntry(entries, list)
    }

    score = 0
    val entriesCount = entries.size()
    if (entriesCount <= questionCount) {
      this.questionCount = entriesCount
    } else {
      Collections.reverse(entries)
      val i = entries.iterator()
      while (entries.size() > questionCount) {
        i.next()
        i.remove()
      }
      Collections.reverse(entries)
    }
  }

  def averageRating(): Float = {
    var total = 0f
    var count = 0f
    for (string <- parameters.dictionnary.getEntries(parameters.firstLanguage)) {
      total += parameters.dictionnary.getEntry(string, parameters.firstLanguage).getRating()
      count += 1
    }
     total / count
  }

  private def addEntry(entries: List[String], list: List[String]) {
    var s = list.get(0)
    while (entries.contains(s)) {
      list.remove(0)
      s = list.get(0)
    }
    entries.add(s)
  }

  def buildRecentComparator() =
    new Comparator[String]() {
      def compare(s1: String, s2: String) = {
        val entry1 = parameters.dictionnary.getEntry(s1, parameters.firstLanguage)
        val entry2 = parameters.dictionnary.getEntry(s2, parameters.firstLanguage)
        entry1.getTotalAnswers() - entry2.getTotalAnswers()
      }
    }

  def buildRatingComparator() =
    new Comparator[String]() {
      def compare(s1: String, s2: String): Int = {
        val entry1 = parameters.dictionnary.getEntry(s1, parameters.firstLanguage)
        val entry2 = parameters.dictionnary.getEntry(s2, parameters.firstLanguage)
        ((entry1.getRating() - entry2.getRating()) * 100000).intValue()
      }
    }
  def buildBadRatioComparator() =
    new Comparator[String]() {
      def compare(s1: String, s2: String) = {
        val entry1 = parameters.dictionnary.getEntry(s1, parameters.firstLanguage)
        val entry2 = parameters.dictionnary.getEntry(s2, parameters.firstLanguage)
        val ratio1 = entry1.getGoodAnswerProportion()
        val ratio2 = entry2.getGoodAnswerProportion()
        val ratioDifference = ratio1 - ratio2
        if (ratioDifference > 0) 1 else if (ratioDifference == 0) 0 else -1
      }
    }

  def getPossibleConfusion(enteredString: String) = parameters.dictionnary.getEntry(enteredString, !parameters.firstLanguage)

  def switchLanguage() {
    val newEntries = new HashSet[String]()
    for (word <- entries) {
      newEntries.addAll(parameters.dictionnary.getEntry(word,
        parameters.firstLanguage).translations)
    }
    entries = new ArrayList[String](newEntries)
    Collections.shuffle(entries)
    questionCount = entries.size()
    parameters.firstLanguage = !parameters.firstLanguage
  }

  def updateScore() { score = score + 1 }

  def resetScore() {
    score = 0
  }

  def getScore() = score + "/" + questionCount

  def iterator() = new SessionIterator()

  class SessionIterator extends Iterator[Question] {
    val iterator = entries.iterator()

    def hasNext() = iterator.hasNext()

    def next() = {
      val word = iterator.next()
      new Question(word, parameters.dictionnary.getEntry(word,
        parameters.firstLanguage), parameters.ignoredChars)
    }

    def remove() {
      throw new UnsupportedOperationException()
    }

  }

  def newRatingGood(rating: Float) = {
    val p1 = rating / (averageRating + rating)
    val speed = 0.1f
    val p2 = p1 + speed * (1f - p1)
    val newRating = (rating * p2 * (1f - p1) / (p1 * (1f - p2))).floatValue()
    printf("%f -> %f%n", rating, newRating)
    newRating
  }

  def newRatingWrong(rating: Float) = {
    val loseProbability = 1f - rating / (averageRating + rating)
    val speed = 0.1f
    val newProbability = loseProbability + speed * (1f - loseProbability)
    val newRating = (rating * loseProbability / newProbability).floatValue()
    printf("%f -> %f%n", rating, newRating)
    newRating
  }

  def newRating(rating: Float, b: Boolean) = if (b) newRatingGood(rating) else newRatingWrong(rating)
}
