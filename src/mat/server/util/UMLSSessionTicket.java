package mat.server.util;

import java.util.HashMap;

public class UMLSSessionTicket {

	private static final HashMap<String, String> umlsSessionMap = new HashMap<String, String>();

	public static void put(String key, String value) {
		umlsSessionMap.put(key, value);
	}

	public static void remove(String key) {
		umlsSessionMap.remove(key);
	}

	public static String getTicket(String key) {
		return umlsSessionMap.get(key);
	}
}
