package mat.server.util;

/**
 * The Class UuidUtility.
 */
public class UuidUtility {

	/**
	 * Id to uuid.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	public static String idToUuid(String id){
		StringBuffer sb = new StringBuffer(id);
		sb.insert(8,'-');
		sb.insert(13,'-');
		sb.insert(18,'-');
		sb.insert(23,'-');
		return sb.toString();
	}
	
}
