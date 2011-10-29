package info.daviot.util.language;

import java.util.*;


/**
 * Implements the Translator interface by using a properties resource file chosen according to the current Locale.
 *
 * @author MDA
 * @version NP
 */
public class PropertiesTranslator implements Translator
{
    private Locale currentLocale;
    private Hashtable resourceBundles = new Hashtable();
    private Hashtable defaultResourceBundles = new Hashtable();
    private List missingRessources = new ArrayList();

    public PropertiesTranslator(Locale locale)
    {
        setLocale(locale);
    }

    public void setLocale(Locale locale)
    {
        currentLocale = locale;
    }

    private String getDefaultString(String key, String category)
    {
        return getString(key, category, key, defaultResourceBundles,
            Locale.getDefault());
    }

    private String getString(String key, String category, String defaultValue,
        Hashtable resourceBundles, Locale locale)
    {
        ResourceBundle resourceBundle = (ResourceBundle) resourceBundles.get(category);

        if (resourceBundle == null)
        {
            try
            {
                resourceBundle = ResourceBundle.getBundle(category, locale);
                defaultResourceBundles.put(category, resourceBundle);
            }
            catch (MissingResourceException e)
            {
                if (!missingRessources.contains(category))
                {
                    System.err.println("Warning : resource not found <" +
                        category + ">");
                    missingRessources.add(category);
                }

                return defaultValue;
            }
        }

        try
        {
            String value = (String) resourceBundle.getString(key);

            if ((value == null) || value.equals(""))
            {
                System.err.println("Invalid definition for <" + key +
                    "> in resource <" + category + "> for language " +
                    locale.getLanguage());

                return defaultValue;
            }
            else
            {
                return value;
            }
        }
        catch (MissingResourceException e)
        {
            System.err.println("Warning : <" + key +
                "> not found in resource <" + category + "> for language " +
                locale.getLanguage());

            return defaultValue;
        }
    }

    public String getString(String key, String category)
    {
        if ("".equals(key))
        {
            return "";
        }

        String defaultValue = getDefaultString(key, category);

        return getString(key, category, defaultValue, resourceBundles,
            currentLocale);
    }
}
