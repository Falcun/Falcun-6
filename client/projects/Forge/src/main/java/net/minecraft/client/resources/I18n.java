package net.minecraft.client.resources;

import java.util.Map;

import net.mattbenson.Wrapper;

public class I18n
{
    private static Locale i18nLocale;

    static void setLocale(Locale i18nLocaleIn)
    {
        i18nLocale = i18nLocaleIn;
    }

    /**
     * format(a, b) is equivalent to String.format(translate(a), b). Args: translationKey, params...
     */
    public static String format(String translateKey, Object... parameters)
    {
    	String msg = i18nLocale.formatMessage(translateKey, parameters);
    	
    	if(msg.equals(translateKey)) {
    		return String.format(Wrapper.getInstance().langFiles(Wrapper.getInstance().langFiles(translateKey)), parameters);
    	}
    	
        return msg;
    }

    public static Map getLocaleProperties()
    {
        return i18nLocale.properties;
    }
}