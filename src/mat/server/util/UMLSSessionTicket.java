package mat.server.util;

import java.util.HashMap;

public class UMLSSessionTicket {
	
	private static final HashMap<String, String> umlsSessionMap = new HashMap<String, String>();

	public static HashMap<String, String> getUmlssessionmap() {
		return umlsSessionMap;
	}

	public static void put(String key, String value){
		umlsSessionMap.put(key, value);
	}

}
