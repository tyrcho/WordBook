package info.daviot.wordbook.model

class Translation(val native: String, val other: String, phonetic: String = "", notes: String = "", tags: Seq[String] = Seq.empty) {
  override def toString = native + " -> " + other
}
