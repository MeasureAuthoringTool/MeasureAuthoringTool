package mat.shared.validator.measure;

import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.shared.CQLModelValidator;
import mat.shared.MatConstants;
import mat.shared.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class CommonMeasureValidator {
	private static final String MEASURE_NAME_REQUIRED = "A measure name is required.";
	private static final String LIBRARY_NAME_REQUIRED = "A CQL Library name is required.";
	private static final String MEASURE_NAME_LETTER_REQUIRED = "A measure name must contain at least one letter.";
	private static final String MEASURE_NAME_LENGTH_ERROR = "A measure name cannot be more than 500 characters.";
	private static final String ECQM_ABBR_TITLE_REQUIRED_ERROR = "An eCQM abbreviated title is required.";
	private static final String ECQM_ABBR_TITLE_LENGTH_ERROR = "An eCQM abbreviated title cannot be more than 32 characters.";
	private static final String MEASURE_SCORE_REQUIRED = "Measure Scoring is required.";
	private static final String MEASURE_NAME_UNDERSCORE_ERROR = "Measure Name must not contain '_' (underscores).";

	public static boolean isValidValue(String value) {
		return !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}
	
    private static boolean hasAtleastOneLetter(String s) {
        for (int i = 0; i < s.length(); i++) {
        	char curChar = s.charAt(i);
        	if (String.valueOf(curChar).matches("[a-zA-Z]")) {
                return true;
        	}
        }
        return false;
    }

    public static List<String> validateFhirLibraryName(String libraryName) {
		CQLModelValidator validator = new CQLModelValidator();
		List<String> errorMessages = new ArrayList<>();
		if (StringUtility.isEmptyOrNull(libraryName)) {
			errorMessages.add(LIBRARY_NAME_REQUIRED);
		} else if (!validator.isValidFhirCqlName(libraryName)) {
			errorMessages.add(MatContext.get().getMessageDelegate().getFhirCqlLibyNameError());
		}
		return errorMessages;
	}
    
    public static List<String> validateQDMName(String libraryName) {
    	CQLModelValidator validator = new CQLModelValidator();
		List<String> errorMessages = new ArrayList<>();
		if (StringUtility.isEmptyOrNull(libraryName)) {
			errorMessages.add(LIBRARY_NAME_REQUIRED);
		} else if (!validator.isValidQDMName(libraryName) ||
				validator.isCommentMoreThan2500Characters(libraryName)) {
			errorMessages.add(MatContext.get().getMessageDelegate().getQDMCqlLibyNameError());
		} 
		
		return errorMessages;
    }

    public static List<String> validateFhirMeasureName(String measureName) {
        List<String> errorMessages = validateMeasureName(measureName);
        if (measureName != null && measureName.contains("_")) {
            errorMessages.add(MEASURE_NAME_UNDERSCORE_ERROR);
        }

        return errorMessages;
    }
    
	public static List<String> validateMeasureName(String measureName) {
		List<String> errorMessages = new ArrayList<>();
		if (StringUtility.isEmptyOrNull(measureName)) {
			errorMessages.add(MEASURE_NAME_REQUIRED);
		} else if (!hasAtleastOneLetter(measureName)) {
			errorMessages.add(MEASURE_NAME_LETTER_REQUIRED);			
		} else if (measureName.length() > 500) {
			errorMessages.add(MEASURE_NAME_LENGTH_ERROR);
		}
		
		return errorMessages;
	}
	
	public static List<String> validateECQMAbbreviation(String abbrName) {
		List<String> errorMessages = new ArrayList<>();
		if ((abbrName == null) || "".equals(abbrName.trim())) {
			errorMessages.add(ECQM_ABBR_TITLE_REQUIRED_ERROR);
		} else if (abbrName.length() > 32) {
			errorMessages.add(ECQM_ABBR_TITLE_LENGTH_ERROR);
		}
		
		return errorMessages;
	}

	public static List<String> validateMeasureScore(String measureScore){
		List<String> errorMessages = new ArrayList<>();
		if ((measureScore == null) || !isValidValue(measureScore)) {
			errorMessages.add(MEASURE_SCORE_REQUIRED);
		}
		return errorMessages;
	}

	public static List<String> validatePatientBased(String scoring, boolean patientBased) {
		List<String> errorMessages = new ArrayList<>();
		// MAT-8602 Continous Variable measures must be patient based.
		if ((MatConstants.CONTINUOUS_VARIABLE.equalsIgnoreCase(scoring) && patientBased)) {
			MatContext.get().getMessageDelegate();
			errorMessages.add(MessageDelegate.CONTINUOUS_VARIABLE_IS_NOT_PATIENT_BASED_ERROR);
		}
		return errorMessages;
	}
}
