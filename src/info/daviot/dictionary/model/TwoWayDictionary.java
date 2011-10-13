package info.daviot.dictionary.model;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TwoWayDictionary {
	private String firstLanguage = "";

	private String secondLanguage = "";

	private transient Collator firstCollator;

	private transient Collator secondCollator;

	private Map<String, DictionaryEntry> firstLanguageIndexedBySecond = new HashMap<String, DictionaryEntry>();

	private Map<String, DictionaryEntry> secondLanguageIndexedByFirst = new HashMap<String, DictionaryEntry>();

	public TwoWayDictionary(String firstLanguage, String secondLanguage) {
		setFirstLanguage(firstLanguage);
		setSecondLanguage(secondLanguage);
	}

	public void removeWord(String word, boolean firstLanguage) {
		if (firstLanguage) {
			removeFirstLanguageWord(word);
		} else {
			removeSecondLanguageWord(word);
		}
	}

	public void removeFirstLanguageWord(String word) {
		removeWord(word, firstLanguageIndexedBySecond,
				secondLanguageIndexedByFirst);
	}

	public void removeSecondLanguageWord(String word) {
		removeWord(word, secondLanguageIndexedByFirst,
				firstLanguageIndexedBySecond);
	}

	private static void removeWord(String word,
			Map<String, DictionaryEntry> firstLanguageIndexedBySecond,
			Map<String, DictionaryEntry> secondLanguageIndexedByFirst) {
		if (word != null) {
			DictionaryEntry firstEntry = secondLanguageIndexedByFirst.get(word);
			if (firstEntry != null) {
				for (String translatedWord : firstEntry.getTranslations()) {
					DictionaryEntry secondEntry = firstLanguageIndexedBySecond
							.get(translatedWord);
					secondEntry.removeTranslation(word);
					if (secondEntry.getTranslations().size() == 0) {
						firstLanguageIndexedBySecond.remove(translatedWord);
					}
				}
			}
			secondLanguageIndexedByFirst.remove(word);
		}
	}

	public void addTranslation(String firstLanguageWord,
			String secondLanguageWord) {
		addTranslation(firstLanguageWord, secondLanguageWord,
				secondLanguageIndexedByFirst);
		addTranslation(secondLanguageWord, firstLanguageWord,
				firstLanguageIndexedBySecond);
	}

	public void addExplaination(String word, String explanation,
			boolean firstLanguage) {
		if (firstLanguage) {
			addFirstLanguageExplaination(word, explanation);
		} else {
			addSecondLanguageExplaination(word, explanation);
		}
	}

	public void addFirstLanguageExplaination(String firstLanguageWord,
			String explanation) {
		addExplanation(firstLanguageWord, explanation,
				secondLanguageIndexedByFirst);
	}

	public void addSecondLanguageExplaination(String secondLanguageWord,
			String explanation) {
		addExplanation(secondLanguageWord, explanation,
				firstLanguageIndexedBySecond);
	}

	private static void addTranslation(String firstLanguageWord,
			String secondLanguageWord,
			Map<String, DictionaryEntry> secondLanguageIndexedByFirst) {
		DictionaryEntry firstLanguageEntry = secondLanguageIndexedByFirst
				.get(firstLanguageWord);
		if (firstLanguageEntry == null)
			firstLanguageEntry = new DictionaryEntry();
		firstLanguageEntry.addTranslation(secondLanguageWord);
		secondLanguageIndexedByFirst.put(firstLanguageWord, firstLanguageEntry);
	}

	private static void addExplanation(String languageWord, String explanation,
			Map<String, DictionaryEntry> languageMap) {
		DictionaryEntry languageEntry = languageMap.get(languageWord);
		if (languageEntry == null)
			languageEntry = new DictionaryEntry();
		languageEntry.setExplaination(explanation);
		languageMap.put(languageWord, languageEntry);
	}

	public DictionaryEntry getEntry(String word, boolean firstLanguage) {
		return firstLanguage ? getFirstLanguageEntry(word)
				: getSecondLanguageEntry(word);
	}

	public DictionaryEntry getSecondLanguageEntry(String wordInSecondLanguage) {
		return firstLanguageIndexedBySecond.get(wordInSecondLanguage);
	}

	public DictionaryEntry getFirstLanguageEntry(String wordInFirstLanguage) {
		return secondLanguageIndexedByFirst.get(wordInFirstLanguage);
	}

	public Set<String> getEntries(boolean firstLanguage) {
		return firstLanguage ? getFirstLanguageEntries()
				: getSecondLanguageEntries();
	}
	
	public List<String> getSortedEntries(boolean firstLanguage) {
		if(firstCollator==null || secondCollator==null) {
			defineCollators();
		}
		Collator c=firstLanguage?firstCollator:secondCollator;
		ArrayList<String> entries = new ArrayList<String>(getEntries(firstLanguage));
		Collections.sort(entries, c);
		return entries;
	}

	private void defineCollators() {
		firstCollator=Collator.getInstance(new Locale(firstLanguage));
		secondCollator=Collator.getInstance(new Locale(secondLanguage));
		
	}

	public Set<String> getFirstLanguageEntries() {
		return secondLanguageIndexedByFirst.keySet();
	}

	public Set<String> getSecondLanguageEntries() {
		return firstLanguageIndexedBySecond.keySet();
	}

	public String getFirstLanguage() {
		return firstLanguage;
	}

	public void setFirstLanguage(String firstLanguage) {
		this.firstLanguage = firstLanguage;
	}

	public String getSecondLanguage() {
		return secondLanguage;
	}

	public void setSecondLanguage(String secondLanguage) {
		this.secondLanguage = secondLanguage;
	}

	public void addAll(TwoWayDictionary imported) {
		if (imported.firstLanguage.equals(firstLanguage)
				&& imported.secondLanguage.equals(secondLanguage)) {
			addAll(imported.firstLanguageIndexedBySecond,
					firstLanguageIndexedBySecond);
			addAll(imported.secondLanguageIndexedByFirst,
					secondLanguageIndexedByFirst);
		} else if (imported.secondLanguage.equals(firstLanguage)
				&& imported.firstLanguage.equals(secondLanguage)) {
			addAll(imported.firstLanguageIndexedBySecond,
					secondLanguageIndexedByFirst);
			addAll(imported.secondLanguageIndexedByFirst,
					firstLanguageIndexedBySecond);
		} else {
			throw new IllegalArgumentException(
					"Les langages ne sont pas compatibles");
		}
	}

	private void addAll(Map<String, DictionaryEntry> importedMap,
			Map<String, DictionaryEntry> targetMap) {
		for (Entry<String, DictionaryEntry> entry : importedMap.entrySet()) {
			String key = entry.getKey();
			DictionaryEntry dictionaryEntry = targetMap.get(key);
			if (dictionaryEntry == null) {
				dictionaryEntry = new DictionaryEntry();
				targetMap.put(key, dictionaryEntry);
			}
			DictionaryEntry importedDictionaryEntry = entry.getValue();
			for (String t : importedDictionaryEntry.getTranslations()) {
				dictionaryEntry.addTranslation(t);
			}
			String explaination = importedDictionaryEntry.getExplaination();
			if (explaination != null) {
				dictionaryEntry.setExplaination(explaination);
			}
		}
	}
}
