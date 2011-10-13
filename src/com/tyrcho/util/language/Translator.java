package com.tyrcho.util.language;

import java.util.Locale;


/**
 * Interface to get the translation for a String based on the locale resources.
 * 
 * @author MDA
 * @version NP
 */
public interface Translator
{
   /**
    * Return the locale String for the key.
    *
    * @param key the string to translate
    * @param resource the name of the resource to ask for translation
    * @return the translation for this key, or the key itself if no translation is found
    */
   public String getString(String key, String resource);
   
   /**
    * Changes the locale and thus the language of the translator.
    */
   public void setLocale(Locale locale);
}