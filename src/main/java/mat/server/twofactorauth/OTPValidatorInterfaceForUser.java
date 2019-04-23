package mat.server.twofactorauth;

public interface OTPValidatorInterfaceForUser {
	
	public boolean validateOTPForUser(String loginId, String otp);

}
