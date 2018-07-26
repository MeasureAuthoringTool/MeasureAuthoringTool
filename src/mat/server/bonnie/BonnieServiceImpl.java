package mat.server.bonnie;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.client.bonnie.BonnieService;
import mat.dao.UserBonnieAccessInfoDAO;
import mat.dao.UserDAO;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;
import mat.server.bonnie.api.BonnieAPI;
import mat.server.bonnie.api.BonnieAPIv1;
import mat.shared.BonnieOAuthResult;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.result.BonnieUserInformationResult;

@SuppressWarnings("serial")
@Service
public class BonnieServiceImpl extends SpringRemoteServiceServlet implements BonnieService {
	
	@Autowired
	private BonnieAPIv1 bonnieApi; 

	/** The user DAO. */
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserBonnieAccessInfoDAO userBonnieAccessInfoDAO;
	
	private String getResponseType() {
		return System.getProperty("BONNIE_RESPONSE_TYPE");
	}

	private String getClientId() {
		return System.getProperty("BONNIE_CLIENT_ID");
	}

	private String getRedirectURI() {
		return System.getProperty("BONNIE_REDIRECT_URI");
	}
	private String getClientSecret() {
		return System.getProperty("BONNIE_CLIENT_SECRET");
	}
	
	public String getBonnieBaseURL() {
		return "https://bonnie-prior.ahrqstg.org";
	}

	@Override
	public String getBonnieAccessLink() {
		String baseURL = getBonnieBaseURL();
		String responseType = getResponseType();
		String clientId = getClientId();
		String redirectURI = getRedirectURI();
		
		if(redirectURI == null && clientId == null && responseType == null) {
			return "";
		} else {
			return baseURL + "/oauth/authorize?response_type="
					+ responseType + "&client_id=" + clientId + "&redirect_uri=" + redirectURI;
		}
	}

	public BonnieOAuthResult refreshBonnieTokens() {
		BonnieOAuthResult result = null;
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		result = getBonnieRefreshResult(user.getUserBonnieAccessInfo());
		SaveBonnieAccessInfo(result);
		
		return result;
	}
	
	private BonnieOAuthResult getBonnieRefreshResult(UserBonnieAccessInfo userBonnieAccessInfo) {
		try {			
			 OAuthClient client = new OAuthClient(new URLConnectionClient());
			
	         OAuthClientRequest request =
	                 OAuthClientRequest.tokenLocation(getBonnieBaseURL() + "/oauth/token")
	                 .setClientId(getClientId())
	                 .setGrantType(GrantType.AUTHORIZATION_CODE)
	                 .setClientSecret(getClientSecret())
	                 .setRefreshToken(userBonnieAccessInfo.getRefreshToken())
	                 .setRedirectURI(getRedirectURI())
	                 .buildQueryMessage();
	         request.addHeader(name, header);
	         
	         OAuthJSONAccessTokenResponse token =
	                 client.accessToken(request, OAuthJSONAccessTokenResponse.class);
	         BonnieOAuthResult result = new BonnieOAuthResult(token.getAccessToken(), token.getRefreshToken(), token.getExpiresIn(), token.getBody());
	         return result;
			}
			catch(Exception exn) {
				exn.printStackTrace();
				return null;
			}
	}

	@Override
	public BonnieOAuthResult exchangeCodeForTokens(String code) {     
		System.out.println("EXCHANING!");
		BonnieOAuthResult result = null;
		try {
			result = getBonnieOAuthResult(code);
			SaveBonnieAccessInfo(result);
            
        } catch (Exception exn) {
            exn.printStackTrace();
            return result;
        }
		
		return result;
	}
	
	private BonnieOAuthResult getBonnieOAuthResult(String code) {
		try {
		 OAuthClient client = new OAuthClient(new URLConnectionClient());

         OAuthClientRequest request =
                 OAuthClientRequest.tokenLocation("https://bonnie-prior.ahrqstg.org/oauth/token")
                 .setClientId(getClientId())
                 .setGrantType(GrantType.AUTHORIZATION_CODE)
                 .setClientSecret(getClientSecret())
                 .setCode(code)
                 .setRedirectURI(getRedirectURI())
                 .buildQueryMessage();
         
         
         OAuthJSONAccessTokenResponse token =
                 client.accessToken(request, OAuthJSONAccessTokenResponse.class);
         BonnieOAuthResult result = new BonnieOAuthResult(token.getAccessToken(), token.getRefreshToken(), token.getExpiresIn(), token.getBody());
         return result;
		}
		catch(Exception exn) {
			exn.printStackTrace();
			return null;
		}
	}
	
	private void SaveBonnieAccessInfo(BonnieOAuthResult result) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		UserBonnieAccessInfo userBonnieAccessInfo = user.getUserBonnieAccessInfo();
		if(userBonnieAccessInfo == null) {
			userBonnieAccessInfo = new UserBonnieAccessInfo();
			userBonnieAccessInfo.setAccessToken(result.getAccessToken());
			userBonnieAccessInfo.setRefreshToken(result.getRefreshToken());
			userBonnieAccessInfo.setUser(user);
		} else {
			userBonnieAccessInfo.setAccessToken(result.getAccessToken());
			userBonnieAccessInfo.setRefreshToken(result.getRefreshToken());
		}
        
        userBonnieAccessInfoDAO.save(userBonnieAccessInfo);
	}
	
	public BonnieUserInformationResult getBonnieUserInformationForUser(String userId) throws BonnieUnauthorizedException, BonnieServerException, IOException {	
		UserBonnieAccessInfo bonnieAccessInfo = userBonnieAccessInfoDAO.findByUserId(userId);
		if(bonnieAccessInfo == null) {
			// if they have no credentials in the database, then they are not authorized with bonnie
			throw new BonnieUnauthorizedException();
		}
		
		try {
			BonnieUserInformationResult bonnieInformationResult = bonnieApi.getUserInformationByToken(bonnieAccessInfo.getAccessToken());
			return bonnieInformationResult;
		} catch (BonnieUnauthorizedException e) {
			// if an unauthorized exception is thrown and the user had credentials in the database, delete them because they 
			// are invalid, and the surface the error
			userBonnieAccessInfoDAO.delete(Integer.toString(bonnieAccessInfo.getId()));
			throw e; 
		}
	}

	public BonnieAPI getBonnieApi() {
		return bonnieApi;
	}

	public void setBonnieApi(BonnieAPIv1 bonnieApi) {
		this.bonnieApi = bonnieApi;
	}
}
