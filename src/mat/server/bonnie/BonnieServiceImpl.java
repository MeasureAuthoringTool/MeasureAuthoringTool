package mat.server.bonnie;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;

import mat.client.bonnie.BonnieService;
import mat.dao.UserBonnieAccessInfoDAO;
import mat.dao.UserDAO;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;
import mat.shared.BonnieOAuthResult;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;

@SuppressWarnings("serial")
public class BonnieServiceImpl extends SpringRemoteServiceServlet implements BonnieService{

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
	private String getBonnieBaseURL() {
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
}
