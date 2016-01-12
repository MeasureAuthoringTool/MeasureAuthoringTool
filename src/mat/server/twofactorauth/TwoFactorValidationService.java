package mat.server.twofactorauth;

public class TwoFactorValidationService {
	
	private OTPValidatorInterfaceForUser otpValidatorInterfaceForUser;

	public boolean validateOTPForUser(String loginId, String otp){
		return this.otpValidatorInterfaceForUser.validateOTPForUser(loginId, otp);				
	}
	
	public OTPValidatorInterfaceForUser getOtpValidatorInterfaceForUser() {
		return otpValidatorInterfaceForUser;
	}

	public void setOtpValidatorInterfaceForUser(
			OTPValidatorInterfaceForUser otpValidatorInterfaceForUser) {
		this.otpValidatorInterfaceForUser = otpValidatorInterfaceForUser;
	}

}
