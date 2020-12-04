package mat.shared.cql;

import mat.shared.CQLModelValidator;

import java.util.ArrayList;
import java.util.List;

public class CqlCommonValidations {

    private static final String CQL_CODE_URL_REQUIRED = "This field is required";
    private static final String UMLS_INVALID_CODE_IDENTIFIER = "Invalid code identifier. Please copy the complete URL for the code directly from VSAC and try again.";
    private static CQLModelValidator validator = new CQLModelValidator();

    public static List<String> validateCqlCodeUrl(String url) {
        List<String> errorMessages = new ArrayList<>();
        if (url.isEmpty()) {
            errorMessages.add(CQL_CODE_URL_REQUIRED);
        } else if (validator.validateForCodeIdentifier(url)) {
            errorMessages.add(UMLS_INVALID_CODE_IDENTIFIER);
        }
        return errorMessages;
    }
}
