package mat.login;

import org.junit.Test;

import mat.dao.SpringInitializationTest;
import mat.shared.ForgottenPasswordResult;

public class ForgotPasswordTest extends SpringInitializationTest{

   @Test	
   public void testForgotPasswordEmailSent(){
	   String email  = "vandavar@ifmc.org";
	   String securityQuestion = "Test Question";
	   String securityAnswer = "test answer";
	   ForgottenPasswordResult result = getUserService().requestForgottenPassword(email, securityQuestion, securityAnswer,1);
	   assertFalse("Email is not send,failed",result.isEmailSent());
   }

}
