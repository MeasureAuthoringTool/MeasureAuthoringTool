package mat.shared.validator.measure;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class CommonMeasureValidatorTest {
	
	CommonMeasureValidator validator = new CommonMeasureValidator();
	
	@Test
	public void testAllMethods() {
		testValidateLibraryName();
		testValidateMeasureName();
		testValidateECQMAbbreviation();
		testValidateMeasureScore();
		testValidatePatientBased();
	}
	
	private void testValidateLibraryName() {
		List<String> noErrorList = Arrays.asList();    
		List<String> emptyList = Arrays.asList("A CQL Library name is required.");
		List<String> invalidLibraryNameList = Arrays.asList("Invalid Library Name. Library names must start with an alpha-character or underscore, followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.");
		
		assertEquals(noErrorList, validator.validateLibraryName("hey"));
		assertEquals(emptyList, validator.validateLibraryName(""));
		assertEquals(invalidLibraryNameList, validator.validateLibraryName("!"));
		assertEquals(invalidLibraryNameList, validator.validateLibraryName("?library"));
		assertEquals(noErrorList, validator.validateLibraryName("_library"));
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
