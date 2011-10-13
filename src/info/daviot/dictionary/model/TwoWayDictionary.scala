package info.daviot.dictionary.model;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import scala.collection.JavaConversions._

class TwoWayDictionary(val firstLanguage: String, val secondLanguage: String) {

  @transient
  var firstCollator: Collator = _
  @transient
  var secondCollator: Collator = _

  val firstLanguageIndexedBySecond = new HashMap[String, DictionaryEntry]();
  val secondLanguageIndexedByFirst = new HashMap[String, DictionaryEntry]();

  def removeWord(word: String, firstLanguage: Boolean) =
    if (firstLanguage) removeFirstLanguageWord(word)
    else removeSecondLanguageWord(word);

  def removeFirstLanguageWord(word: String) {
    removeWord(word, firstLanguageIndexedBySecond, secondLanguageIndexedByFirst);
  }

  def removeSecondLanguageWord(word: String) {
    removeWord(word, secondLanguageIndexedByFirst, firstLanguageIndexedBySecond);
  }

  private def removeWord(word: String,
    firstLanguageIndexedBySecond: Map[String, DictionaryEntry],
    secondLanguageIndexedByFirst: Map[String, DictionaryEntry]) {
    if (word != null) {
      val firstEntry = secondLanguageIndexedByFirst.get(word);
      if (firstEntry != null) {
        for (translatedWord <- firstEntry.translations) {
          val secondEntry = firstLanguageIndexedBySecond.get(translatedWord);
          secondEntry.removeTranslation(word);
          if (secondEntry.translations.size() == 0) {
            firstLanguageIndexedBySecond.remove(translatedWord);
          }
        }
      }
      secondLanguageIndexedByFirst.remove(word);
    }
  }

  def addTranslation(firstLanguageWord: String, secondLanguageWord: String) {
    addTranslation(firstLanguageWord, secondLanguageWord, secondLanguageIndexedByFirst);
    addTranslation(secondLanguageWord, firstLanguageWord, firstLanguageIndexedBySecond);
  }

  def addExplaination(word: String, explanation: String, firstLanguage: Boolean) {
    if (firstLanguage) {
      addFirstLanguageExplaination(word, explanation);
    } else {
      addSecondLanguageExplaination(word, explanation);
    }
  }

  def addFirstLanguageExplaination(firstLanguageWord: String, explanation: String) {
    addExplanation(firstLanguageWord, explanation, secondLanguageIndexedByFirst);
  }

  def addSecondLanguageExplaination(secondLanguageWord: String, explanation: String) {
    addExplanation(secondLanguageWord, explanation, firstLanguageIndexedBySecond);
  }

  private def addTranslation(firstLanguageWord: String,
    secondLanguageWord: String,
    secondLanguageIndexedByFirst: Map[String, DictionaryEntry]) {
    var firstLanguageEntry = secondLanguageIndexedByFirst.get(firstLanguageWord);
    if (firstLanguageEntry == null) firstLanguageEntry = new DictionaryEntry();
    firstLanguageEntry.addTranslation(secondLanguageWord);
    secondLanguageIndexedByFirst.put(firstLanguageWord, firstLanguageEntry);
  }

  def addExplanation(languageWord: String, explanation: String, languageMap: Map[String, DictionaryEntry]) {
    var languageEntry = languageMap.get(languageWord);
    if (languageEntry == null) languageEntry = new DictionaryEntry();
    languageEntry.explaination_$eq(explanation);
    languageMap.put(languageWord, languageEntry);
  }

  def getEntry(word: String, firstLanguage: Boolean) =
    if (firstLanguage) getFirstLanguageEntry(word) else getSecondLanguageEntry(word)

  def getSecondLanguageEntry(word: String) = firstLanguageIndexedBySecond.get(word);

  def getFirstLanguageEntry(word: String) = secondLanguageIndexedByFirst.get(word);

  def getEntries(firstLanguage: Boolean) =
    if (firstLanguage) getFirstLanguageEntries() else getSecondLanguageEntries();

  def getSortedEntries(firstLanguage: Boolean): List[String] = {
    if (firstCollator == null || secondCollator == null) {
      defineCollators();
    }
    val c = if (firstLanguage) firstCollator else secondCollator;
    getEntries(firstLanguage).toList.sort(c.compare(_, _) < 0)
  }

  def defineCollators() {
    firstCollator = Collator.getInstance(new Locale(firstLanguage));
    secondCollator = Collator.getInstance(new Locale(secondLanguage));

  }

  def getFirstLanguageEntries() = secondLanguageIndexedByFirst.keySet();

  def getSecondLanguageEntries() = firstLanguageIndexedBySecond.keySet();

  def addAll(imported: TwoWayDictionary) {
    if (imported.firstLanguage.equals(firstLanguage) && imported.secondLanguage.equals(secondLanguage)) {
      addAll(imported.firstLanguageIndexedBySecond, firstLanguageIndexedBySecond);
      addAll(imported.secondLanguageIndexedByFirst, secondLanguageIndexedByFirst);
    } else if (imported.secondLanguage.equals(firstLanguage) && imported.firstLanguage.equals(secondLanguage)) {
      addAll(imported.firstLanguageIndexedBySecond, secondLanguageIndexedByFirst);
      addAll(imported.secondLanguageIndexedByFirst, firstLanguageIndexedBySecond);
    } else {
      throw new IllegalArgumentException("Les langages ne sont pas compatibles");
    }
  }

  private def addAll(importedMap: Map[String, DictionaryEntry],
    targetMap: Map[String, DictionaryEntry]) {
    for (entry <- importedMap.entrySet()) {
      val key = entry.getKey();
      var dictionaryEntry = targetMap.get(key);
      if (dictionaryEntry == null) {
        dictionaryEntry = new DictionaryEntry();
        targetMap.put(key, dictionaryEntry);
      }
      val importedDictionaryEntry = entry.getValue();
      for (t <- importedDictionaryEntry.translations) {
        dictionaryEntry.addTranslation(t);
      }
      val explaination = importedDictionaryEntry.explaination;
      if (explaination != null) {
        dictionaryEntry.explaination_$eq(explaination);
      }
    }
  }
}
