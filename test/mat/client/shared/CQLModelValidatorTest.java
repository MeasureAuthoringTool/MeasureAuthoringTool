package mat.client.shared;

import static org.junit.Assert.*;
import org.junit.Test;

import mat.shared.CQLModelValidator;

public class CQLModelValidatorTest {
	
	private CQLModelValidator validator = new CQLModelValidator();
	
	@Test
	public void testDoesAliasNameFollowCQLAliasNamingConvention() {
		testValidName();
		testInvalidName(); 
	}
	
	private void testInvalidName() {
		String startsWithNumber = "1aliasName";
		String containsSpecialCharacter = "alias*name";
		String containsSpecialCharacter1 = "aliasname!";
		
		assertEquals(false, validator.doesAliasNameFollowCQLAliasNamingConvention(startsWithNumber));
		assertEquals(false, validator.doesAliasNameFollowCQLAliasNamingConvention(containsSpecialCharacter));
		assertEquals(false, validator.doesAliasNameFollowCQLAliasNamingConvention(containsSpecialCharacter1));
	}

	private void testValidName() {
		String startsWithUnderscore = "_aliasName";
		String startsWithLetterAndContainsUnderscore = "alias_name";
		String startsWithLetterAndContainsUnderscoreAndHasNumber = "alias_name1";
		
		assertEquals(true, validator.doesAliasNameFollowCQLAliasNamingConvention(startsWithUnderscore));
		assertEquals(true, validator.doesAliasNameFollowCQLAliasNamingConvention(startsWithLetterAndContainsUnderscore));
		assertEquals(true, validator.doesAliasNameFollowCQLAliasNamingConvention(startsWithLetterAndContainsUnderscoreAndHasNumber));		
	}

}
