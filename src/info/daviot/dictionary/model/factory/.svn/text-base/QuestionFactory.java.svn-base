//package com.tyrcho.dictionary.model.factory;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
//import com.tyrcho.dictionary.model.DictionaryEntry;
//import com.tyrcho.dictionary.model.Question;
//import com.tyrcho.dictionary.model.TwoWayDictionary;
//
//public class QuestionFactory {
//    private static List<Integer> position;
//    private static int rank;
//    
//    public static void resetPositions(int entryNumber){
//        position = new ArrayList<Integer>();
//        for(int i=0;i<entryNumber;i++){
//            position.add(i);
//        }
//        Collections.shuffle(position);
//        rank = 0;
//    }
//
//    public static Question buildQuestion(TwoWayDictionary twoWayDictionary,boolean firstLanguage){
//        Question question = new Question();
//        String[] entriesSet;
//        
//        if (firstLanguage) {
//            entriesSet = twoWayDictionary.getFirstLanguageEntries().toArray(new String[0]);
//        } else {
//            entriesSet = twoWayDictionary.getSecondLanguageEntries().toArray(new String[0]);
//        }
//        
//        if (position==null) resetPositions(entriesSet.length);
//        
//        String word = entriesSet[position.get(rank)];
//        rank ++;
//        question.setWord(word);
//
//        DictionaryEntry dictionaryEntry;
//        if (firstLanguage) {
//            dictionaryEntry = twoWayDictionary.getFirstLanguageEntry(word);
//        } else {
//            dictionaryEntry = twoWayDictionary.getSecondLanguageEntry(word);
//        }
//        question.setDictionaryEntry(dictionaryEntry);
//        return question;
//    }
//}
