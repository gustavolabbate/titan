package br.com.cpqd.titan.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * 
 * @author ggodoy
 *
 */
public class Messages  {
    private static final Logger LOGGER = Logger.getLogger(Messages.class.getName());

    private static String defineMessage(String key) {
	try
	{
	    ResourceBundle res = ResourceBundle.getBundle("messages",new Locale("en", "US"));
	    return res.getString(key);

	}catch (MissingResourceException mre){
	    LOGGER.error(mre.getMessage(), mre);
	    return null;
	
	}
    }
    
    /**
     * 
     * @param key
     * @param parameters
     * @return
     */
    public static String getMessage(String key, String[] parameters) {
	String msg = defineMessage(key);
	
	if (parameters != null){

	    for (int i=0; i < parameters.length; i++) {

		msg = msg.replace("{"+ i +"}", parameters[i]);

	    }
	}
	
	return msg;
	
    }

    /**
     * 
     * @param key
     * @return
     */
    public static String getMessage(String key) {
	return 	defineMessage(key);
    }

}
