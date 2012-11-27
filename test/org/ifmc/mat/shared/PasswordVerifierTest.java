package org.ifmc.mat.shared;

import mat.shared.PasswordVerifier;

import junit.framework.TestCase;


public class PasswordVerifierTest extends TestCase {
	public void testIsPasswordValid() {
		assertTrue(isValid("userid", "abcdAa_1"));
		
		assertFalse(isValid("userid", "aAa_1"));
		assertFalse(isValid("userid", "abcdAa_1234567890"));
		assertFalse(isValid("userid", "abcdAa12"));
		assertFalse(isValid("userid", "abcdAbcda"));
		assertFalse(isValid("userid", "abcdaa_1"));
		assertFalse(isValid("userid", "ABCDABCA_1"));
		assertFalse(isValid("userid", "aaaaAa_1"));
		assertFalse(isValid("userid", "useridAa_1"));
		assertFalse(isValid("UserId", "useridAa_1"));
	}
	
	private boolean isValid(String userid, String password) {
		PasswordVerifier v = new PasswordVerifier(userid, password, password);
		return v.isValid();
	}
	
	public void testIsPasswordValidSpecialChars() {
		for(int i = 0; i < PasswordVerifier.getAllowedSpecialChars().length; i++) {
			String test = "Abcabca1" + PasswordVerifier.getAllowedSpecialChars()[i];
			assertTrue(test,isValid("userid", test));
		}
		
	}
	
	public void testTTP17() {
		String password = "Passw0*";
		PasswordVerifier v = new PasswordVerifier("test", password, password);
		assertTrue(v.isContainsUpper());
		for(String message : v.getMessages()) {
			assertFalse(message.contains("upper"));
		}
	}
}
