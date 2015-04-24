package mat.server.util;

import java.util.HashMap;

/**
 * The Class UMLSSessionTicket.
 */
public class UMLSSessionTicket {

	/** The Constant umlsSessionMap. */
	private static final HashMap<String, String> umlsSessionMap = new HashMap<String, String>();

	/**
	 * Put.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public static void put(String key, String value) {
		umlsSessionMap.put(key, value);
	}

	/**
	 * Removes the.
	 * 
	 * @param key
	 *            the key
	 */
	public static void remove(String key) {
		umlsSessionMap.remove(key);
	}

	/**
	 * Gets the ticket.
	 * 
	 * @param key
	 *            the key
	 * @return the ticket
	 */
	public static String getTicket(String key) {
		return umlsSessionMap.get(key);
	}
}
