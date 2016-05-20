package mat.shared;

import com.google.gwt.regexp.shared.RegExp;


// TODO: Auto-generated Javadoc
/**
 * The Class CQLModelValidator.
 */
public class CQLModelValidator {

	/** The regex expression. */
	private final String REGEX_EXPRESSION = "[\"<>'/]";
	
	/** The reg exp. */
	private final RegExp regExp = RegExp.compile(REGEX_EXPRESSION);
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
}
