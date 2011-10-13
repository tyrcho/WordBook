package info.daviot.dictionary;

import scala.collection.JavaConversions._

object Utils {

  def simpleCompare(s1: String, s2: String, ignoredChars: String): Boolean =
    s1.filter(!ignoredChars.contains(_)) == s2.filter(!ignoredChars.contains(_))

}
