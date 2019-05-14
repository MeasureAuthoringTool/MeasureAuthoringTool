package mat.server.cqlparser;

import org.apache.commons.lang3.StringUtils;

public class CQLParserUtil {

	private CQLParserUtil() {
		throw new IllegalStateException();
	}
	
	public static String parseString(String identifier) {
		
		if(StringUtils.isEmpty(identifier)) {
			return identifier;
		}
		
		if(Character.toString(identifier.charAt(0)).equals("\"") || Character.toString(identifier.charAt(0)).equals("'")) {
			return identifier.substring(1, identifier.length() - 1);
		}
		
		if(Character.toString(identifier.charAt(identifier.length() - 1)).equals("\"") || Character.toString(identifier.charAt(0)).equals("'")) {
			return identifier.substring(0, identifier.length() - 2);
		}
		
		return identifier;
	}
}
