package mat.server.util;

import mat.client.umls.service.VsacTicketInformation;

import java.util.HashMap;

/**
 * The Class UMLSSessionTicket.
 */
public class UMLSSessionTicket {

	/** The Constant umlsSessionMap. */
	private static final HashMap<String, VsacTicketInformation> umlsSessionMap = new HashMap<String, VsacTicketInformation	>();

	/**
	 * Put.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public static void put(String key, VsacTicketInformation value) {
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
	public static VsacTicketInformation getTicket(String key) {
		return umlsSessionMap.get(key);
	}
}
