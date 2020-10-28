package mat.shared.validator.measure;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommonMeasureValidatorTest {
	
	CommonMeasureValidator validator = new CommonMeasureValidator();
	
	@Test
	public void testAllMethods() {
		testValidateQDMLibraryName();
		testValidateFhirLibraryName();
		testValidateMeasureName();
		testValidateECQMAbbreviation();
		testValidateMeasureScore();
		testValidatePatientBased();
	}

	private void testValidateFhirLibraryName() {
		List<String> noErrorList = Arrays.asList();
		List<String> emptyList = Arrays.asList("A CQL Library name is required.");
		List<String> invalidLibraryNameList = Arrays.asList("Invalid Library Name. Library names must start with an upper case letter, followed by an alpha-numeric character(s) and must not contain spaces or  '_' (underscores).");

		assertEquals(noErrorList, validator.validateFhirLibraryName("Hey"));
		assertEquals(noErrorList, validator.validateFhirLibraryName("H123"));
		assertEquals(noErrorList, validator.validateFhirLibraryName("H98237498234123"));
		assertEquals(invalidLibraryNameList, validator.validateFhirLibraryName("hey"));
		assertEquals(emptyList, validator.validateFhirLibraryName(""));
		assertEquals(invalidLibraryNameList, validator.validateFhirLibraryName("!"));
		assertEquals(invalidLibraryNameList, validator.validateFhirLibraryName("?library"));
		assertEquals(invalidLibraryNameList, validator.validateFhirLibraryName("_library"));
		assertEquals(invalidLibraryNameList, validator.validateFhirLibraryName("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
	}
	
	private void testValidateQDMLibraryName() {
		List<String> noErrorList = Arrays.asList();    
		List<String> emptyList = Arrays.asList("A CQL Library name is required.");
		List<String> invalidLibraryNameList = Arrays.asList("Invalid Library Name. Library names must start with an alpha-character or underscore, followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.");
		
		assertEquals(noErrorList, validator.validateQDMName("hey"));
		assertEquals(emptyList, validator.validateQDMName(""));
		assertEquals(invalidLibraryNameList, validator.validateQDMName("!"));
		assertEquals(invalidLibraryNameList, validator.validateQDMName("?library"));
		assertEquals(noErrorList, validator.validateQDMName("_library"));
	}
	
	private void testValidateMeasureName() {
		List<String> noErrorList = Arrays.asList();    
		List<String> emptyList = Arrays.asList("A measure name is required.");
		List<String> invalidMeasureNameList = Arrays.asList("A measure name must contain at least one letter.");
		
		assertEquals(noErrorList, validator.validateMeasureName("hey"));
		assertEquals(emptyList, validator.validateMeasureName(""));
		assertEquals(invalidMeasureNameList, validator.validateMeasureName("!"));
		assertEquals(noErrorList, validator.validateMeasureName("k"));
	}
	
	private void testValidateECQMAbbreviation() {
		List<String> noErrorList = Arrays.asList(); 
		List<String> emptyList = Arrays.asList("An eCQM abbreviated title is required.");
		
		assertEquals(noErrorList, validator.validateECQMAbbreviation("abrName"));
		assertEquals(emptyList, validator.validateECQMAbbreviation(""));
	}
	
	private void testValidateMeasureScore() {
		List<String> noErrorList = Arrays.asList(); 
		List<String> emptyList = Arrays.asList("Measure Scoring is required.");
		
		assertEquals(noErrorList, validator.validateMeasureScore("score"));
		assertEquals(emptyList, validator.validateMeasureScore(""));
	}
	
	private void testValidatePatientBased() {
		List<String> noErrorList = Arrays.asList(); 
		List<String> errorList = Arrays.asList("Continous Variable measures must not be patient based.");
		
		assertEquals(errorList, validator.validatePatientBased("continuous Variable", true));
		assertEquals(noErrorList, validator.validatePatientBased("", true));
		assertEquals(noErrorList, validator.validatePatientBased("continuous Variable", false));
		assertEquals(noErrorList, validator.validatePatientBased("", false));

	}

}
