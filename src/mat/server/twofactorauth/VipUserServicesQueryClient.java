package mat.server.twofactorauth;

import java.rmi.RemoteException;

import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.AuthenticateUserRequest;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.AuthenticateUserRequestType;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.AuthenticateUserResponse;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.BaseRequestWithAccountIdTypeChoice_type2;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.OtpAuthDataType;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.OtpType;
import com.symantec.vipuserservices.wsclient.AuthenticationServiceStub.UserIdType;
import com.symantec.vipuserservices.wsclient.QueryServiceStub;
import com.symantec.vipuserservices.wsclient.QueryServiceStub.GetServerTimeRequest;
import com.symantec.vipuserservices.wsclient.QueryServiceStub.GetServerTimeRequestType;
import com.symantec.vipuserservices.wsclient.QueryServiceStub.GetServerTimeResponse;
import com.symantec.vipuserservices.wsclient.QueryServiceStub.GetServerTimeResponseType;
import com.symantec.vipuserservices.wsclient.QueryServiceStub.RequestIdType;



public class VipUserServicesQueryClient {

	public static void main1(String[] args) throws RemoteException {
		String pathToP12File = "vip_cert.p12";
		String password = "HCIs0001";
		
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.keyStore", pathToP12File);
		System.setProperty("javax.net.ssl.keyStorePassword", password);
		
//		QueryServiceStub queryServiceStub = new QueryServiceStub("https://pilotuserservices-auth."
//				+ "vip.symantec.com/vipuserservices/QueryService_1_3");
		
		QueryServiceStub queryServiceStub = new QueryServiceStub("https://userservices-auth.vip.symantec.com/"
				+ "vipuserservices/QueryService_1_3");//AuthenticationService_1_3
		
		GetServerTimeRequest getServerTimeRequestBean = new GetServerTimeRequest();

		GetServerTimeRequestType getServerTimeRequest = new GetServerTimeRequestType();
		getServerTimeRequestBean.setGetServerTimeRequest(getServerTimeRequest);
		RequestIdType requestIdType = new RequestIdType();
		requestIdType.setRequestIdType("rqstId" + System.currentTimeMillis());
		getServerTimeRequest.setRequestId(requestIdType);
		GetServerTimeResponse getServerTimeResponseBean = queryServiceStub.getServerTime(getServerTimeRequestBean);
		GetServerTimeResponseType getServerTimeResponse = getServerTimeResponseBean.getGetServerTimeResponse();
		System.out.println("Status : " + getServerTimeResponse.getStatus());
		System.out.println("Status message : " + getServerTimeResponse.getStatusMessage());
		System.out.println("Server time is : " +getServerTimeResponse.getTimestamp().getTime());
	}
	
	public static void main(String[] args) throws RemoteException {
		/*String pathToP12File = "vip_cert.p12";
		String password = "HCIs0001";
		
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.keyStore", pathToP12File);
		System.setProperty("javax.net.ssl.keyStorePassword", password);*/
		
		AuthenticationServiceStub authenticationServiceStub = new AuthenticationServiceStub("https://userservices-auth.vip.symantec.com/"
				+ "vipuserservices/AuthenticationService_1_3");
		
		AuthenticateUserRequest authenticateUserRequest = new AuthenticateUserRequest();
		AuthenticateUserRequestType authenticateUserRequestType = new AuthenticateUserRequestType();
		
		AuthenticationServiceStub.RequestIdType requestIdType = new AuthenticationServiceStub.RequestIdType();
		requestIdType.setRequestIdType("rqstId" + System.currentTimeMillis());
		authenticateUserRequestType.setRequestId(requestIdType);
		
		UserIdType userIdType = new UserIdType();
		userIdType.setUserIdType("Chinmay");
		authenticateUserRequestType.setUserId(userIdType);
		
		OtpAuthDataType otpAuthDataType = new OtpAuthDataType();
		OtpType otpType = new OtpType();
		otpType.setOtpType("453483");
		otpAuthDataType.setOtp(otpType);
		
		BaseRequestWithAccountIdTypeChoice_type2 baseRequestWithAccountIdTypeChoice_type2 = new BaseRequestWithAccountIdTypeChoice_type2();
		baseRequestWithAccountIdTypeChoice_type2.setOtpAuthData(otpAuthDataType);
		
		authenticateUserRequestType.setBaseRequestWithAccountIdTypeChoice_type2(baseRequestWithAccountIdTypeChoice_type2);
		
		authenticateUserRequest.setAuthenticateUserRequest(authenticateUserRequestType);
		
		AuthenticateUserResponse authenticateUserResponse = authenticationServiceStub.authenticateUser(authenticateUserRequest);
		
		System.out.println("Status:"+authenticateUserResponse.getAuthenticateUserResponse().getStatus());
		System.out.println("Status Message:"+authenticateUserResponse.getAuthenticateUserResponse().getStatusMessage());
		System.out.println("Credential ID:"+authenticateUserResponse.getAuthenticateUserResponse().getCredentialId());
		System.out.println("Credential Type:"+authenticateUserResponse.getAuthenticateUserResponse().getCredentialType());
		
		
	}

}
