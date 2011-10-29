package info.daviot.dictionary

import scala.collection.JavaConversions._

object Utils {

  def simpleCompare(s1: String, s2: String, ignoredChars: String): Boolean = {
    def filterIgnored(s: Char) = if (ignoredChars == null) true else !ignoredChars.contains(s)
    s1.filter(filterIgnored) == s2.filter(filterIgnored)
  }
}
