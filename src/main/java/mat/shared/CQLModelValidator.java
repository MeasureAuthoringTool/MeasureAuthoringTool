package mat.shared;

import com.google.gwt.regexp.shared.RegExp;


/**
 * The Class CQLModelValidator.
 */
public class CQLModelValidator {
    private RegExp FHIR_LIB_NAME_REGEX = RegExp.compile("^[A-Z]([A-Za-z0-9]{0,254})$");

    /**
     * The regex expression.
     */
    private final String REGEX_EXPRESSION = "[\"<>'/]";

    /**
     * The reg exp.
     */
    private final RegExp regExp = RegExp.compile(REGEX_EXPRESSION);

    /**
     * The code regex expression.
     */
    private final String CODE_REGEX_EXPRESSION = "^(CODE:/CodeSystem/)([^/]*)" +
            "(/Version/)([^/]*)(/Code/)([\\S]*)(/Info)$";

    /**
     * The code reg exp.
     */
    private final RegExp codeRegExp = RegExp.compile(CODE_REGEX_EXPRESSION);

    /**
     * The comment regex expression.
     */
    private final String COMMENT_REGEX_EXPRESSION = "(\\*/)|(/\\*)";

    /**
     * The comment reg exp.
     */
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
    public boolean isValidQDMName(String identifierName) {
        boolean result = true;
        if (identifierName != null && identifierName.length() > 0) {
            char firstChar = identifierName.charAt(0);
            if (!Character.isLetter(firstChar) && firstChar != '_') {
                result = false;
            }

            if (result) {
                for (int i = 1; i < identifierName.length(); i++) {
                    char ch = identifierName.charAt(i);
                    if (!Character.isDigit(ch) && !Character.isLetter(ch) && ch != '_') {
                        result = false;
                        break;
                    }
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Validate for code identifier.
     *
     * @param url the url
     * @return true, if successful
     */
    public boolean validateForCodeIdentifier(String url) {
        boolean isValidCodeIdentifier = codeRegExp.test(url);
        return !isValidCodeIdentifier;
    }

    public boolean isCommentMoreThan250Characters(String comment) {
        return comment.length() > 250;
    }

    public boolean doesCommentContainInvalidCharacters(String comment) {
        return commentRegExp.test(comment);
    }

    /**
     * Validate for comment text area.
     *
     * @param comment the comment
     * @return true, if successful
     */
    public boolean isCommentTooLongOrContainsInvalidText(String comment) {
        boolean isInValid = false;

        if (comment.length() > 250 || commentRegExp.test(comment)) {
            isInValid = true;
        }
        return isInValid;
    }

    public boolean isCommentMoreThan2500Characters(String comment) {
        return comment.length() > 2500;
    }

    public boolean isLibraryNameMoreThan500Characters(String name) {
        return name.length() > 500;
    }

    public boolean isValidFhirCqlName(String name) {
        return FHIR_LIB_NAME_REGEX.test(name);
    }


}
