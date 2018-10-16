package mat.server.twofactorauth;

//import java.math.BigInteger;
//import java.net.MalformedURLException;
//import java.rmi.RemoteException;
//
//import javax.xml.rpc.ServiceException;
//
//import com.symantec.vip.schemas._2006._08.vipservice.GetServerTimeResponseType;
//import com.symantec.vip.schemas._2006._08.vipservice.GetServerTimeType;
//import com.symantec.vip.schemas._2006._08.vipservice.GetTokenInformationResponseType;
//import com.symantec.vip.schemas._2006._08.vipservice.GetTokenInformationType;
//import com.symantec.vip.schemas._2006._08.vipservice.TokenIdType;
//import com.symantec.vip.schemas._2006._08.vipservice.ValidateMultipleResponseType;
//import com.symantec.vip.schemas._2006._08.vipservice.ValidateMultipleType;
//import com.symantec.vip.schemas._2006._08.vipservice.ValidateResponseType;
//import com.symantec.vip.schemas._2006._08.vipservice.ValidateType;
//import com.symantec.vip.schemas._2006._08.vipservice.VipSoapInterface;
//import com.symantec.vip.schemas._2006._08.vipservice.VipSoapInterfaceService;
//import com.symantec.vip.schemas._2006._08.vipservice.VipSoapInterfaceServiceLocator;

public class Credential {
	
	/*VipSoapInterfaceService service;
	VipSoapInterface port;
	String m_url;
	String version = "2.0";
	String nonce = "abcd1234";// unique per transaction
	String authAccount = null;
	String certFile = "vip_cert.p12"; // replace with your cert file
	String password = "HCIs0001"; // replace with the password for the cert

	public Credential(String url)
	{
		try{
			service =new VipSoapInterfaceServiceLocator();
			m_url = url;
			System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
			System.setProperty("javax.net.ssl.keyStore", certFile);
			System.setProperty("javax.net.ssl.keyStorePassword", password);
		}
		catch (Exception e)
		{
			System.out.println("Exception : " + e);
		}
	}


	public String getServerTime()
	{
		try{
			port = service.getvipServiceAPI(new java.net.URL(m_url+"/prov/soap"));
			GetServerTimeType x = new GetServerTimeType(version,nonce);
			GetServerTimeResponseType resp = port.getServerTime(x);
			BigInteger reason = new
					BigInteger(resp.getStatus().getReasonCode());
			if (reason.intValue() != 0){
				System.out.println("Message = " + resp.getStatus().getStatusMessage());
				System.out.println("Error Detail = " + resp.getStatus().getErrorDetail());
				return null;
			}else{
				return (resp.getTimestamp().getTime().toString());
			}
		}
		catch (Exception e)
		{
			System.out.println("GetServerTime(), Exception : " + e);
			e.printStackTrace();
			return null;
		}
	}
	
	public void getTokenInformation(String TokenId)
	{
		try{
			port = service.getvipServiceAPI(new java.net.URL(m_url+"/mgmt/soap"));
			TokenIdType tokenIDType = new TokenIdType();
			tokenIDType.set_value(TokenId);
			
			// A reseller account can perform operations on behalf of the customer account
			// specified in AuthorizerAccountId. For non-reseller accounts (the default case)
			// specify AuthorizerAccountId as null.
			GetTokenInformationType x = new
					GetTokenInformationType(version,nonce,null,tokenIDType);
			GetTokenInformationResponseType resp = port.getTokenInformation(x);
			
			BigInteger reason = new BigInteger(resp.getStatus().getReasonCode());
			if (reason.intValue() != 0){
				System.out.println("Message = " +
						resp.getStatus().getStatusMessage());
				System.out.println("Error Detail = " +
						resp.getStatus().getErrorDetail());
			}else{
				System.out.println("Result = " +
						resp.getStatus().getStatusMessage());
				System.out.println("Token Id = " +
						resp.getTokenInformation().getTokenId());
				System.out.println("Token Kind = " +
						resp.getTokenInformation().getTokenKind());
				System.out.println("Adapter = " +
						resp.getTokenInformation().getAdapter());
				System.out.println("Token Status = " +
						resp.getTokenInformation().getTokenStatus());
				System.out.println("Expiration Date = " +
						resp.getTokenInformation().getExpirationDate().getTime().
						toString());
								
				if(resp.getTokenInformation().getTempPasswordExpirationDate()
						!= null)
					System.out.println("Temp pwd expiration Date = " +
							resp.getTokenInformation().getTempPasswordExpirationDate()
							.getTime().toString());
				System.out.println("Owner = " +
						resp.getTokenInformation().getOwner().toString());
				System.out.println("Last update = " +
						resp.getTokenInformation().getLastUpdate().getTime().
						toString());
			}
		}
		catch (Exception e)
		{
			System.out.println("getTokenInformation(), Exception : " + e);
			e.printStackTrace();
		}
	}
	
	public void validateOTPSingleCredential(String TokenId, String otp){
		try {
			
			port = service.getvipServiceAPI(new java.net.URL(m_url+"/val/soap"));
			
			TokenIdType tokenIDType = new TokenIdType();
			tokenIDType.set_value(TokenId);
			
			ValidateType vt = new ValidateType("3.1", nonce, null, tokenIDType, otp);
			
			ValidateResponseType validateResponseType = port.validate(vt);
			System.out.println("Status:"+validateResponseType.getStatus().getStatusMessage());
			
			byte[] reasonCodes = validateResponseType.getStatus().getReasonCode();
			
			System.out.println("Reason codes:");
			for(int i=0;i<reasonCodes.length;i++){
				System.out.println(reasonCodes[i]);
			}
			
			BigInteger reason = new BigInteger(reasonCodes);
			if (reason.intValue() == 0){
				
				System.out.println("validateResponseType.getVersion():"+validateResponseType.getVersion());
				
				System.out.println("Category ID:"+validateResponseType.getTokenCategoryDetails().getCategoryId());
				
				System.out.println("Credential Type:"+validateResponseType.getTokenCategoryDetails().getFormFactor());
				
				System.out.println("Code Generation Method:"+validateResponseType.getTokenCategoryDetails().getMovingFactor().getValue());
				
				System.out.println("OTP Generated by:"+validateResponseType.getTokenCategoryDetails().getOtpGeneratedBy().getValue());
				
			}else{
				System.out.println("Message = " +
						validateResponseType.getStatus().getStatusMessage());
				System.out.println("Error Detail = " +
						validateResponseType.getStatus().getErrorDetail());
			}
					
		} catch (MalformedURLException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void validateOTPMultipleCredentials(String[] tokenIds, String otp){
		
		try {
			
			port = service.getvipServiceAPI(new java.net.URL(m_url+"/val/soap"));
			
			TokenIdType[] tokenIDTypeArray = new TokenIdType[tokenIds.length];
			for(int i=0;i<tokenIds.length;i++){
				TokenIdType tokenIDType = new TokenIdType();
				tokenIDType.set_value(tokenIds[i]);
				tokenIDTypeArray[i] = tokenIDType;
			}
			
			ValidateMultipleType validateMultipleType = new ValidateMultipleType("3.1", nonce, null, tokenIDTypeArray, otp, true);
			
			ValidateMultipleResponseType validateMultipleResponseType = port.validateMultiple(validateMultipleType);
			System.out.println("Status:"+validateMultipleResponseType.getStatus().getStatusMessage());
			
			byte[] reasonCodes = validateMultipleResponseType.getStatus().getReasonCode();
			
			System.out.println("validateMultipleResponseType-Reason codes:");
			for(int i=0;i<reasonCodes.length;i++){
				System.out.println(reasonCodes[i]);
			}
			
			BigInteger reason = new BigInteger(reasonCodes);
			if (reason.intValue() == 0){
				
				System.out.println("Successfull TokenId:"+validateMultipleResponseType.getSuccessfulTokenId().get_value());
				
				System.out.println("validateResponseType.getVersion():"+validateMultipleResponseType.getVersion());
				
				System.out.println("Category ID:"+validateMultipleResponseType.getTokenCategoryDetails().getCategoryId());
				
				System.out.println("Credential Type:"+validateMultipleResponseType.getTokenCategoryDetails().getFormFactor());
				
				System.out.println("Code Generation Method:"+validateMultipleResponseType.getTokenCategoryDetails().getMovingFactor().getValue());
				
				System.out.println("OTP Generated by:"+validateMultipleResponseType.getTokenCategoryDetails().getOtpGeneratedBy().getValue());
				
			}else{
				System.out.println("Message = " +
						validateMultipleResponseType.getStatus().getStatusMessage());
				System.out.println("Error Detail = " +
						validateMultipleResponseType.getStatus().getErrorDetail());
			}
			
		} catch (MalformedURLException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args)
	{
		String url = "https://services-auth.vip.symantec.com";
		//String url = "https://pilot-services-auth.vip.symantec.com";
		
		//test single token/credential
		String token_id = "VSHM15828519"; //replace with a valid Token Id
		String otp = "815513";
		Credential c = new Credential(url);
		System.out.println("Server Time = " + c.getServerTime());
		c.validateOTPSingleCredential(token_id, otp);
		
		//test multiple tokens/credentials
		String otp1 = "091418";
		String[] tokenIdArray = {"VSST11951748","VSHM15828519"};
		Credential c1 = new Credential(url);
		System.out.println("Server Time = " + c1.getServerTime());
		c1.validateOTPMultipleCredentials(tokenIdArray, otp1);
	}
	 */
	
}
