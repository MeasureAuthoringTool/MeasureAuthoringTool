package mat.shared;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ValidationUtility {

	/** The good chars. */
	private final String goodChars = "`~1!2@3#4$5%6^7&8*9(0)-_=+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?";
	
	/** The allowed chars. */
	private Set<Character> allowedChars = new HashSet<Character>();


	/**
	 * Instantiates a new validation utility.
	 */
	public ValidationUtility(){
		for(int i=0;i<goodChars.length();i++){
			allowedChars.add(goodChars.charAt(i));
		}

	}
	
	/**
	 * Checks for illegal chars.
	 * 
	 * @param s
	 *            the s
	 * @return true, if successful
	 */
	private boolean hasIllegalChars(String s){
		if(s == null)
			return false;
		for(int i=0;i<s.length();i++){
			if(!allowedChars.contains(s.charAt(i))){
				if(!Character.isWhitespace(s.charAt(i)))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if is illegal validation.
	 * 
	 * @param s
	 *            the s
	 * @return true, if is illegal validation
	 */
	public boolean isIllegalValidation(String s){
		if(hasIllegalChars(s))
			return true;
		return false;
	}

	/**
	 * Validate.
	 * 
	 * @param metadata
	 *            the metadata
	 * @param key
	 *            the key
	 */
	public void validate(Map<String, String> metadata,String key){
		String s = metadata.get(key);
		if(this.isIllegalValidation(s)){
			int len = 60;
			if(key.length()<len)
				len = key.length();
			log("Validation found illegal text in "+ key.substring(0, len) +".");
		}
	}
	
	/**
	 * Log.
	 * 
	 * @param logstr
	 *            the logstr
	 */
	private void log(String logstr){
		System.out.println(logstr);
	}
	
	
}
