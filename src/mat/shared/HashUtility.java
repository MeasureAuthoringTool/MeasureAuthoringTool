package mat.shared;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtility {
	
	public static String getSecurityQuestionHash(String salt, String plainTextAnswer) {
		String hashed = hash(salt + plainTextAnswer.toUpperCase());
		return hashed;
	}
	
	/**
	 * Hash.
	 * 
	 * @param s
	 *            the s
	 * @return the string
	 */
	private static String hash(String s) {
		try {
			if(s == null) {
				s = "";
			}
			MessageDigest m=MessageDigest.getInstance("SHA-256");
			m.update(s.getBytes(),0,s.length());
			return new BigInteger(1,m.digest()).toString(16);
		}
		catch(NoSuchAlgorithmException exc) {
			throw new RuntimeException(exc);
		}
	}
	
}
