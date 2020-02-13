package mat.shared;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PasswordVerifierTest {

	private boolean isValid(String userid, String password) {
		String markupRegExp = "<[^>]+>";
		String noMarkupTextPwd = password.trim().replaceAll(markupRegExp, "");
		password = noMarkupTextPwd;
		PasswordVerifier v = new PasswordVerifier(userid, password, password);
		return v.isValid();
	}

	@Test
	public void testMinPasswordLength() {
		assertFalse(isValid("userId","qQ5%"));
		assertFalse(isValid("userId","aqQ5%"));
		assertFalse(isValid("userId","aaaaqQ5%"));
		assertFalse(isValid("userId","qqqqqqqqqqqQ5%"));
		assertTrue(isValid("userId","qqqqqqqqqqqaQ5%"));
	}

	@Test
	public void testMaxPasswordLength() {
		assertTrue(isValid("userId","012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789qQ5%"));
		assertFalse(isValid("userId","01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678912345qQ5%"));
	}

	@Test
	public void testDictionaryWordsAllowed() {
		assertTrue(isValid("userId","password12345%P"));
	}

	@Test
	public void testUserIdAllowed() {
		assertTrue(isValid("userId","userIdrd12345%P"));
	}

	public void testIsPasswordValid() {
		assertTrue(isValid("userid", "qqweerrabcdAa_1"));
		assertTrue(isValid("userid", "abcdAa_1234567890"));
		assertFalse(isValid("userid", "qqqqqqqqqqaaa_1"));
		assertFalse(isValid("userid", "qqqqqqqabcdAa12"));
		assertFalse(isValid("userid", "abcdAbcdaqqqqqq"));
		assertFalse(isValid("userid", "abcdaa_1qqqqqqq"));
		assertFalse(isValid("userid", "ABCDABCA_1AAAAA"));
	}

	@Test
	public void testIsPasswordValidSpecialChars() {
		for(int i = 0; i < PasswordVerifier.getAllowedSpecialChars().length; i++) {
			String test = "Abcabca1fifteen" + PasswordVerifier.getAllowedSpecialChars()[i];
			assertTrue(isValid("userid", test));
		}
		
	}

	@Test
	public void testTTP17() {
		String password = "Passw0*";
		PasswordVerifier v = new PasswordVerifier("test", password, password);
		assertTrue(v.isContainsUpper());
		for(String message : v.getMessages()) {
			assertFalse(message.contains("upper"));
		}
	}
}
