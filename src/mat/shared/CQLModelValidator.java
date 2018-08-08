package mat.shared;

import com.google.gwt.regexp.shared.RegExp;


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
	
	/** The code regex expression. */
	private final String CODE_REGEX_EXPRESSION = "^(CODE:/CodeSystem/)([^/]*)" +
	        "(/Version/)([^/]*)(/Code/)([^/]*)(/Info)$";
	
	/** The code reg exp. */
	private final RegExp codeRegExp = RegExp.compile(CODE_REGEX_EXPRESSION);
	
	/** The comment regex expression. */
	private final String COMMENT_REGEX_EXPRESSION = "(\\*/)|(/\\*)";
	
	/** The comment reg exp. */
	private final RegExp commentRegExp = RegExp.compile(COMMENT_REGEX_EXPRESSION);
	
	/**
	 * Validate for special character in CQL 
	 * Identifier Names.
	 *
	 * @param identifierName the identifier name
	 * @return true, if successful
	 */
	public boolean hasSpecialCharacter(String identifierName) {
		
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
	
	/**
	 * Validate for code identifier.
	 *
	 * @param url the url
	 * @return true, if successful
	 */
	public boolean validateForCodeIdentifier(String url){
		boolean isValidCodeIdentifier = codeRegExp.test(url);
		return !isValidCodeIdentifier;
	}
	
	/**
	 * Validate for comment text area.
	 *
	 * @param comment the comment
	 * @return true, if successful
	 */
	public boolean validateForCommentTextArea(String comment) {
		boolean isInValid = false;

		if (comment.length() > 250 || commentRegExp.test(comment)) {
			isInValid = true;
		}
		return isInValid;
	}
	
}
