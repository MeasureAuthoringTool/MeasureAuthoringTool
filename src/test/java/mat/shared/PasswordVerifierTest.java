package mat.shared;

import org.junit.Test;

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
		assertTrue(!isValid("userId","qQ5%"));
		assertTrue(!isValid("userId","aqQ5%"));
		assertTrue(!isValid("userId","aaaaqQ5%"));
		assertTrue(!isValid("userId","qqqqqqqqqqqQ5%"));
		assertTrue(isValid("userId","qqqqqqqqqqqaQ5%"));
	}

	@Test
	public void testMaxPasswordLength() {
		assertTrue(isValid("userId","012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789qQ5%"));
		assertTrue(!isValid("userId","01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678912345qQ5%"));
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
		assertTrue(!isValid("userid", "qqqqqqqqqqaaa_1"));
		assertTrue(!isValid("userid", "qqqqqqqabcdAa12"));
		assertTrue(!isValid("userid", "abcdAbcdaqqqqqq"));
		assertTrue(!isValid("userid", "abcdaa_1qqqqqqq"));
		assertTrue(!isValid("userid", "ABCDABCA_1AAAAA"));
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
			assertTrue(!message.contains("upper"));
		}
	}
}
