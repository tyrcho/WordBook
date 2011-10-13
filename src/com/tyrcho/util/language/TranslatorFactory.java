package com.tyrcho.util.language;

import java.util.Locale;


/**
 * Factory to get an instance of a Translator interface.
 * 
 * @author MDA
 * @version NP
 */
public class TranslatorFactory
{
   private static Translator defaultTranslator;

   /**
    * Prevents instanciation.
    */
   private TranslatorFactory() {}
   

   /**
    * Builds a translator based on the specified Locale.
    * 
    * @param locale the locale to define the target language
    */
   public static void initTranslator(Locale locale)
   {
      getTranslator().setLocale(locale);
   }
   
   /**
    * Gets the current translator.
    */
   public static Translator getTranslator()
   {
   	  if (defaultTranslator==null)
   	  	defaultTranslator=new PropertiesTranslator(Locale.getDefault());
   	  return defaultTranslator;
   }
}