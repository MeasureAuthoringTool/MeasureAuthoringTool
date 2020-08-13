package mat.shared;

import mat.model.cql.CQLCode;

import java.util.List;

/**
 * The Class StringUtility.
 */
public class StringUtility {
	
	/** The nl. */
	public final String nl = "\r\n";
	
	/**
	 * for input "blah18", should return 18-1 naming convention starts at 1,
	 * code logic is zero based.
	 * 
	 * @param name
	 *            the name
	 * @return the pos
	 */
	public int getPos(String name) {
		int val = 0 ;
		try {
			int offset = 0;
			while(isInt(name.charAt(name.length()-(offset+1))))
				offset++;
			String  numVal = name.substring(name.length()-offset);
			val = (new Integer(numVal)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;//default
		}
		return val-1;
	}
	
	/**
	 * Checks if is int.
	 * 
	 * @param a
	 *            the a
	 * @return true, if is int
	 */
	private boolean isInt(char a){
		return Character.isDigit(a);
	}
	
	
	/**
	 * Checks if is empty or null.
	 * 
	 * @param str
	 *            the str
	 * @return true, if is empty or null
	 */
	public static boolean isEmptyOrNull(String str){
		return str == null || str.trim().isEmpty();
	}
	
	public static boolean isNotBlank(String str) {
		return !isEmptyOrNull(str);
	}
	
	/**
	 * Strip off number.
	 * 
	 * @param s
	 *            the s
	 * @return the string
	 */
	public String stripOffNumber(String s){
		boolean flag = true;
		int strLen = s.length();
		while(flag){
			char c = s.charAt(strLen-1);
			if(Character.isDigit(c)){
				strLen--;
				continue;
			}else{
				flag = false;
			}
		}
		return s.substring(0,strLen);
	}
	
	
	/**
	 * Trim leading zeros.
	 * 
	 * @param numberStr
	 *            the number str
	 * @return the string
	 */
	public String trimLeadingZeros(String numberStr){
		return numberStr.replaceFirst("^0+(?!$)", "");
	}
	

	/**
	 * Removes escaped characters, double quotes, single quotes, and backslashes (" ' \), from given string 
	 * @param word 
	 * 			given word to remove escaped characters from
	 * @return new word without escaped characters
	 */
	public static String removeEscapedCharsFromString(String word) {
		word = word.replaceAll("\'", "");
		word = word.replaceAll("\\"+"\\", " ");
		word = word.replaceAll('\\'+"\"", "");
		return word;
	}
	
	/**
	 * Iterates through given CQLCode list and calls removeEscapedCharsFromString() to
	 * remove escaped characters from the code display names
	 * @param list
	 * 		list of codes with names to be updated
	 * 
	 * @return list after it has been updated
	 */
	public static List<CQLCode> removeEscapedCharsFromList(List<CQLCode> list) {
		for(CQLCode code : list) {
			code.setDisplayName(removeEscapedCharsFromString(code.getDisplayName()));
			code.setName(code.getDisplayName());
		}
		return list;
	}
	
	public static String trimTextToSixtyChars(String textToTrim) {
		return textToTrim.length()>60 ? textToTrim.substring(0, 59) : textToTrim;
	}

	public static String doTrim(String str) {
		return (str != null) && (str.trim().length() > 0) ? str.trim() : null;
	}
	
	/**
	 * Escape html  method to escape special characters like 
	 * "&", "<", ">", "/", "\", "'" inside Html tag.
	 *
	 * @param html the html
	 * @return the string
	 */
	public static String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&#39;");
	}
}
