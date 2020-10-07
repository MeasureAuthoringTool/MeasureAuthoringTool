package vsac.util;

import java.io.InputStream;
import java.net.URL;


/**
 * The Class ResourceLoader.
 */
public class ResourceLoader {
	
	/**
	 * Loads the file from /WEB-INF/classes.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the URL object of the File; null if File doesn't exists
	 */
	public URL getResourceAsURL(String fileName){
		return this.getClass().getClassLoader().getResource(fileName);
	}
	
	/**
	 * Loads the file from /WEB-INF/classes.
	 * 
	 * @param fileName
	 *            the file name
	 * @return InputStream object of the File; null if File doesn't exists
	 */
	public InputStream getResourceAsStream(String fileName){
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}
	
}
