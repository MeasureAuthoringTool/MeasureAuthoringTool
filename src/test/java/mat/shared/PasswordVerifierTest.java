package mat.shared;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PasswordVerifierTest {

    private boolean isValid(String password) {
        String markupRegExp = "<[^>]+>";
        String noMarkupTextPwd = password.trim().replaceAll(markupRegExp, "");
        PasswordVerifier v = new PasswordVerifier(noMarkupTextPwd, noMarkupTextPwd);
        return v.isValid();
    }

    @Test
    public void testMinPasswordLength() {
        assertFalse(isValid("qQ5%"));
        assertFalse(isValid("aqQ5%"));
        assertFalse(isValid("aaaaqQ5%"));
        assertFalse(isValid("qqqqqqqqqqqQ5%"));
        assertTrue(isValid("qqqqqqqqqqqaQ5%"));
    }

    @Test
    public void testMaxPasswordLength() {
        assertTrue(isValid("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789qQ5%"));
        assertFalse(isValid("01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678912345qQ5%"));
    }

    @Test
    public void testDictionaryWordsAllowed() {
        assertTrue(isValid("password12345%P"));
    }

    @Test
    public void testUserIdAllowed() {
        assertTrue(isValid("userIdrd12345%P"));
    }

    public void testIsPasswordValid() {
        assertTrue(isValid("qqweerrabcdAa_1"));
        assertTrue(isValid("abcdAa_1234567890"));
        assertFalse(isValid("qqqqqqqqqqaaa_1"));
        assertFalse(isValid("qqqqqqqabcdAa12"));
        assertFalse(isValid("abcdAbcdaqqqqqq"));
        assertFalse(isValid("abcdaa_1qqqqqqq"));
        assertFalse(isValid("ABCDABCA_1AAAAA"));
    }

    @Test
    public void testIsPasswordValidSpecialChars() {
        for (int i = 0; i < PasswordVerifier.getAllowedSpecialChars().length; i++) {
            String test = "Abcabca1fifteen" + PasswordVerifier.getAllowedSpecialChars()[i];
            assertTrue(isValid(test));
        }

    }

    @Test
    public void testTTP17() {
        String password = "Passw0*";
        PasswordVerifier v = new PasswordVerifier(password, password);
        assertTrue(v.isContainsUpper());
        for (String message : v.getMessages()) {
            assertFalse(message.contains("upper"));
        }
    }
}
