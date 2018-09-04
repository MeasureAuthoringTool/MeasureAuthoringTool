package mat.server.twofactorauth.vip;

import java.math.BigInteger;
import java.rmi.RemoteException;

import mat.server.twofactorauth.OTPValidatorInterfaceForUser;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;

import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.AuthenticateUserRequest;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.AuthenticateUserRequestType;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.AuthenticateUserResponse;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.BaseRequestWithAccountIdTypeChoice_type2;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.OtpAuthDataType;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.OtpType;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.UserIdType;

public class OTPValidatorForUser implements OTPValidatorInterfaceForUser{

	@Override
	public boolean validateOTPForUser(String loginId, String otp) {
		boolean status = false;

		String authenticationServiceURL = System.getProperty("2FA_VIP_COMMUNICATION_URL");
		/*System.out.println("authenticationServiceURL:"+authenticationServiceURL);
		System.out.println("2FA Certificate file name:"+System.getProperty("javax.net.ssl.keyStore")+"!");*/
		
		/*String pathToP12File = "vip_cert.p12";
		String password = "HCIs0001";
		
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.keyStore", pathToP12File);
		System.setProperty("javax.net.ssl.keyStorePassword", password);*/
		System.out.println("Communicating with Symantec VIP now.....");
		AuthenticationServiceStub authenticationServiceStub;
		try {
			authenticationServiceStub = new AuthenticationServiceStub(authenticationServiceURL);
			
			AuthenticateUserRequest authenticateUserRequest = new AuthenticateUserRequest();
			AuthenticateUserRequestType authenticateUserRequestType = new AuthenticateUserRequestType();

			AuthenticationServiceStub.RequestIdType requestIdType = new AuthenticationServiceStub.RequestIdType();
			requestIdType.setRequestIdType("rqstId" + System.currentTimeMillis());
			authenticateUserRequestType.setRequestId(requestIdType);

			UserIdType userIdType = new UserIdType();
			userIdType.setUserIdType(loginId);
			authenticateUserRequestType.setUserId(userIdType);

			OtpAuthDataType otpAuthDataType = new OtpAuthDataType();
			OtpType otpType = new OtpType();
			otpType.setOtpType(otp);
			otpAuthDataType.setOtp(otpType);

			BaseRequestWithAccountIdTypeChoice_type2 baseRequestWithAccountIdTypeChoice_type2 = new BaseRequestWithAccountIdTypeChoice_type2();
			baseRequestWithAccountIdTypeChoice_type2.setOtpAuthData(otpAuthDataType);

			authenticateUserRequestType.setBaseRequestWithAccountIdTypeChoice_type2(baseRequestWithAccountIdTypeChoice_type2);

			authenticateUserRequest.setAuthenticateUserRequest(authenticateUserRequestType);
			
			String PROXY_HOST = System.getProperty("https.proxyHost");
			if(PROXY_HOST !=null) {
				
				int PROXY_PORT = Integer.parseInt(System.getProperty("https.proxyPort"));
				HttpTransportProperties.ProxyProperties pp = 
						new HttpTransportProperties.ProxyProperties();
					 pp.setProxyName(PROXY_HOST);
					 pp.setProxyPort(PROXY_PORT);
					 
				authenticationServiceStub._getServiceClient().getOptions().setProperty(HTTPConstants.PROXY,pp);
			}
			
			AuthenticateUserResponse authenticateUserResponse = authenticationServiceStub.authenticateUser(authenticateUserRequest);
			
			BigInteger statusCode = new BigInteger(authenticateUserResponse.getAuthenticateUserResponse().getStatus().getBytes());
			System.out.println("Status code:"+statusCode);
			if(statusCode.intValue() == 0){
				status = true;
			}
			
			System.out.println("Status:"+authenticateUserResponse.getAuthenticateUserResponse().getStatus());
			System.out.println("Status Message:"+authenticateUserResponse.getAuthenticateUserResponse().getStatusMessage());
			System.out.println("Credential ID:"+authenticateUserResponse.getAuthenticateUserResponse().getCredentialId());
			System.out.println("Credential Type:"+authenticateUserResponse.getAuthenticateUserResponse().getCredentialType());

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return status;
	}


}
