package mat.shared;

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
	public boolean isEmptyOrNull(String str){
		return str == null || str.isEmpty();
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
}
