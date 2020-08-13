package mat.client.shared;

import mat.shared.CQLModelValidator;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CQLModelValidatorTest {

    private CQLModelValidator validator = new CQLModelValidator();

    @Test
    public void testInvalidName() {
        String startsWithNumber = "1aliasName";
        String containsSpecialCharacter = "alias*name";
        String containsSpecialCharacter1 = "aliasname!";

        assertEquals(false, validator.isValidQDMName(startsWithNumber));
        assertEquals(false, validator.isValidQDMName(containsSpecialCharacter));
        assertEquals(false, validator.isValidQDMName(containsSpecialCharacter1));
    }

    @Test
    public void testValidName() {
        String startsWithUnderscore = "_aliasName";
        String startsWithLetterAndContainsUnderscore = "alias_name";
        String startsWithLetterAndContainsUnderscoreAndHasNumber = "alias_name1";

        assertEquals(true, validator.isValidQDMName(startsWithUnderscore));
        assertEquals(true, validator.isValidQDMName(startsWithLetterAndContainsUnderscore));
        assertEquals(true, validator.isValidQDMName(startsWithLetterAndContainsUnderscoreAndHasNumber));
    }

    @Test
    public void testContainsSpecialCharacter1() {
        assertEquals(false, validator.hasSpecialCharacter("gabe"));
    }

    @Test
    public void testContainsSpecialCharacter2() {
        assertEquals(false, validator.hasSpecialCharacter("gabe("));
    }

    @Test
    public void testContainsSpecialCharacter3() {
        assertEquals(false, validator.hasSpecialCharacter("gab*e"));
    }

    @Test
    public void testContainsSpecialCharacter4() {
        assertEquals(true, validator.hasSpecialCharacter("gabe<"));
    }

    @Test
    public void testContainsSpecialCharacter5() {
        assertEquals(true, validator.hasSpecialCharacter("gabe>"));
    }

    @Test
    public void testContainsSpecialCharacter6() {
        assertEquals(true, validator.hasSpecialCharacter("gabe'"));
    }

    @Test
    public void testContainsSpecialCharacter7() {
        assertEquals(true, validator.hasSpecialCharacter("gabe/"));
    }
}
