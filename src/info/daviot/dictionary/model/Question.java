package info.daviot.dictionary.model;

import info.daviot.dictionary.Utils;

public class Question {
	private String inputTranslation;

	private DictionaryEntry dictionaryEntry;

	private String word;

	private final String ignoredChars;

	public Question(String word, DictionaryEntry dictionaryEntry,
			String ignoredChars) {
		this.ignoredChars = ignoredChars;
		setWord(word);
		setDictionaryEntry(dictionaryEntry);
	}

	// Red�finition de la m�thode de l'objet Object
	public String toString() {
		return "Donnez la traduction de : " + word;
	}

	// Ajout de la traduction fourni par l'utilisateur
	public void setInputTranslation(String inputTranslation) {
		this.inputTranslation = inputTranslation;
	}

	// Test si la traduction propos�e par l'utilisateur est �gale � une des
	// traductions possibles
	public boolean isAnswerValid() {
		for (String translation : dictionaryEntry.getTranslations()) {
			if (Utils.simpleCompare(translation, inputTranslation, ignoredChars)) {
				return true;
			}
		}
		return false;
	}

	// Retourne la ou les traductions possibles
	public String getTranslation() {
		return dictionaryEntry.getTranslations().toString();
	}

	public void setDictionaryEntry(DictionaryEntry dictionaryEntry) {
		this.dictionaryEntry = dictionaryEntry;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public DictionaryEntry getDictionaryEntry() {
		return dictionaryEntry;
	}

	public String getWord() {
		return word;
	}
}
