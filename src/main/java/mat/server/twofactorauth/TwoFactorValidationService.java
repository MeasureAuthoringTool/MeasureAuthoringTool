package mat.server.twofactorauth;

import org.springframework.stereotype.Service;

@Service
public class TwoFactorValidationService {

    private OTPValidatorInterfaceForUser otpValidatorInterfaceForUser;

    public TwoFactorValidationService(OTPValidatorInterfaceForUser otpValidatorInterfaceForUser) {
        this.otpValidatorInterfaceForUser = otpValidatorInterfaceForUser;
    }

    public boolean validateOTPForUser(String loginId, String otp) {
        return this.otpValidatorInterfaceForUser.validateOTPForUser(loginId, otp);
    }

}
