package mat.shared;

import com.google.gwt.regexp.shared.RegExp;


// TODO: Auto-generated Javadoc
/**
 * The Class CQLModelValidator.
 */
public class CQLModelValidator {

	/** The regex expression. */
	private final String REGEX_EXPRESSION = "[\"<>'/]";
	/*private final String REGEX_ALIAS_EXPRESSION = "[_a-zA-Z0-9]";*/
	
	/** The reg exp. */
	private final RegExp regExp = RegExp.compile(REGEX_EXPRESSION);
	/*private final RegExp regAliasExp = RegExp.compile(REGEX_ALIAS_EXPRESSION);*/
	
	/**
	 * Validate for special character in CQL 
	 * Identifier Names.
	 *
	 * @param identifierName the identifier name
	 * @return true, if successful
	 */
	public boolean validateForSpecialChar(String identifierName) {
		
		boolean isValidSpecialChar = regExp.test(identifierName);
		
		if ((identifierName == null) || "".equals(identifierName) || isValidSpecialChar) {
			return true;
		}
		return false;
	}
	
	/**
	 * Validate for alias name special char.
	 *
	 * @param identifierName the identifier name
	 * @return true, if successful
	 */
	public boolean validateForAliasNameSpecialChar(String identifierName) {

		boolean bool = false;
		for (int i = 0; i < identifierName.length(); i++) {
			char ch = identifierName.charAt(i);
			//if its not the first character
			if(i != 0){
				if (Character.isDigit(ch) || Character.isLetter(ch) || ch == '_') {
					bool = true;
				} else {
					bool = false;
					return bool;
				}
			} else{
				if (Character.isLetter(ch) || ch == '_') {
					bool = true;
				} else {
					bool = false;
					return bool;
				}
			}
			
		}

		return bool;
	}
	
}
