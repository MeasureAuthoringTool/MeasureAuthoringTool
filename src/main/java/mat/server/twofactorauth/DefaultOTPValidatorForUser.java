package mat.server.twofactorauth;

public class DefaultOTPValidatorForUser implements OTPValidatorInterfaceForUser {

	@Override
	/**
	 * This is a dummy validator for 2 factor authentication.
	 * This validator is basically equivalent to having "NO 2 FACTOR AUTHENTICATION".
	 * This can be used when MAT wants to turn off 2 factor authentication for various 
	 * environments like STAGING,TRAINING, QA or DEV.
	 * This can be activated by setting the "2FA_AUTH_CLASS" property in 
	 * MAT.properties to "mat.server.twofactorauth.DefaultOTPValidatorForUser"
	 */
	public boolean validateOTPForUser(String loginId, String otp) {
		return true;
	}

}
