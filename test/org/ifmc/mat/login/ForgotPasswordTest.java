package org.ifmc.mat.login;

import mat.shared.ForgottenPasswordResult;

import org.ifmc.mat.dao.SpringInitializationTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class ForgotPasswordTest extends SpringInitializationTest{

   @Test	
   public void testForgotPasswordEmailSent(){
	   
	   String userId = "adminuser";
	   String email  = "vandavar@ifmc.org";
	   String securityQuestion = "Test Question";
	   String securityAnswer = "test answer";
	   ForgottenPasswordResult result = getUserService().requestForgottenPassword(email, securityQuestion, securityAnswer);
	   assertFalse("Email is not send,failed",result.isEmailSent());
   }

}
