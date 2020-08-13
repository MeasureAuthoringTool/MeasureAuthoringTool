package mat.shared;

import junit.framework.TestCase;

public class SecurityQuestionVerifierTest extends TestCase {
	public void testValid() {
		SecurityQuestionVerifier sqv = 	
			new SecurityQuestionVerifier("Question 1", "Answer 1", 
					"Question 2", "Answer 2", "Question 3", "Answer 3");
		assertTrue(sqv.isValid());
	}
	
	public void testAnswer1TooShort() {
		SecurityQuestionVerifier sqv = 	
			new SecurityQuestionVerifier("Question 1", "A1", 
					"Question 2", "Answer 2", "Question 3", "Answer 3");
		assertFalse(sqv.isValid());
	}
	
	public void testAnswer2TooShort() {
		SecurityQuestionVerifier sqv = 	
			new SecurityQuestionVerifier("Question 1", "Answer 1", 
					"Question 2", "A2", "Question 3", "Answer 3");
		assertFalse(sqv.isValid());
	}
	
	public void testAnswer3TooShort() {
		SecurityQuestionVerifier sqv = 	
			new SecurityQuestionVerifier("Question 1", "Answer 1", 
					"Question 2", "Answer 2", "Question 3", "A3");
		assertFalse(sqv.isValid());
	}
}
